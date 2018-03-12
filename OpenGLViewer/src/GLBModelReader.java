import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.zip.DataFormatException;
import com.google.gson.*;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;

public class GLBModelReader {
    private String type;
    private JsonObject JSONData;
    private byte binData[];
    private int version, length;
    private int[] VAO, VBO, EBO;
    private ArrayList<FloatBuffer> positionBuff, normalBuff;
    private ArrayList<JsonObject> position, normal;
    public GLBModelReader(String filename) throws DataFormatException, IOException{
        positionBuff = new ArrayList<>();
        normalBuff = new ArrayList<>();
        position = new ArrayList<>();
        normal = new ArrayList<>();
        FileInputStream fileInput = new FileInputStream(new File(filename));
        byte header[] = new byte[12];
        fileInput.read(header);
        type = new String(Arrays.copyOfRange(header, 0, 4), "UTF-8");
        ByteBuffer wrapper = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN);
        if(!type.equals("glTF")){
            throw new DataFormatException("File type: " + type + " is not supported.");
        }
        wrapper.getInt();
        version = wrapper.getInt();
        length = wrapper.getInt();
        String JSONString = "";
        while(fileInput.available() != 0) {
            byte chunkHeader[] = new byte[8];
            fileInput.read(chunkHeader);
            wrapper = ByteBuffer.wrap(chunkHeader).order(ByteOrder.LITTLE_ENDIAN);
            int chunkLength = wrapper.getInt();
            int chunkType = wrapper.getInt();
            byte chunkData[] = new byte[chunkLength];
            fileInput.read(chunkData);
            switch(chunkType){
                //ASCII String "JSON"
                case 0x4E4F534A:
                    JSONString = new String(chunkData, "UTF-8");
                    break;
                //ASCII String "BIN"
                case 0x004E4942:
                    binData = chunkData.clone();
                    break;
                default:
                    break;
            }
        }
        fileInput.close();
        parseJSON(JSONString);
    }
    //TODO: Parse JSON and access binary information

    public void parseJSON(String JSONString){
        Gson gson = new Gson();
        JSONData = gson.fromJson(JSONString, JsonObject.class);
        System.out.println("Root: " + JSONData.keySet());

        int defaultSceneIndex = JSONData.get("scene").getAsInt();
        //System.out.println("Default scene index: " + defaultSceneIndex);

        JsonArray rootNodes = JSONData.getAsJsonArray("nodes");
        JsonArray rootMeshes = JSONData.getAsJsonArray("meshes");
        JsonArray rootAccessors = JSONData.getAsJsonArray("accessors");
        JsonArray rootBuffers = JSONData.getAsJsonArray("buffers");
        JsonArray rootBefferViews = JSONData.getAsJsonArray("bufferViews");
        JsonArray rootMaterials = JSONData.getAsJsonArray("materials");
        JsonArray rootScenes = JSONData.getAsJsonArray("scenes");



        JsonObject defaultScene = rootScenes.get(defaultSceneIndex).getAsJsonObject();
        System.out.println("Default scene: " + defaultScene.keySet());

        JsonArray nodes = defaultScene.getAsJsonArray("nodes");
        //System.out.println("Default scene nodes: " + nodes);

        for(int i = 0; i < nodes.size(); i++){
            int nodesIndex = nodes.get(i).getAsInt();
            //System.out.println("Nodes index: " + nodesIndex);

            JsonObject currNode = rootNodes.get(nodesIndex).getAsJsonObject();
            System.out.println("Node " + nodesIndex + ": " + currNode.keySet());

            JsonArray children = currNode.getAsJsonArray("children");
            //System.out.println("Children: " + children);
            for (int j = 0; j < children.size(); j++) {
                int childIndex = children.get(j).getAsInt();
                //System.out.println("Child index: " + childIndex);

                JsonObject child = rootNodes.get(childIndex).getAsJsonObject();
                System.out.println("Child " + childIndex + ": " + child.keySet());

                JsonArray matrix = child.getAsJsonArray("matrix");
                System.out.println("Child matrix: " + matrix);

                int meshIndex = child.get("mesh").getAsInt();
                System.out.println("Child mesh index: " + meshIndex);

                JsonObject mesh = rootMeshes.get(meshIndex).getAsJsonObject();
                System.out.println("Child mesh: " + mesh.keySet());

                JsonArray primitives = mesh.getAsJsonArray("primitives");
                //System.out.println("Child mesh primitives: " + primitives);

                for (int k = 0; k < primitives.size(); k++) {
                    JsonObject primitive = primitives.get(k).getAsJsonObject();
                    System.out.println("Primitive " + k + ": " + primitive);

                    JsonObject attributes = primitive.getAsJsonObject("attributes");
                    System.out.println("Attributes: " + attributes.keySet());

                    int positionIndex = attributes.get("POSITION").getAsInt();
                    position.add(rootAccessors.get(positionIndex).getAsJsonObject());
                    System.out.println("Position: " + position);

                    JsonObject bufferView = rootBefferViews.get(position.get(position.size() - 1).get("bufferView").getAsInt()).getAsJsonObject();
                    System.out.println("Buffer view: " + bufferView);

                    JsonObject buffer = rootBuffers.get(bufferView.get("buffer").getAsInt()).getAsJsonObject();
                    System.out.println("Buffer: " + buffer.keySet());

                    ByteBuffer byteBuff = Buffers.newDirectByteBuffer(binData, bufferView.get("byteOffset").getAsInt(), bufferView.get("byteLength").getAsInt());
                    positionBuff.add(byteBuff.asFloatBuffer().duplicate());
                    System.out.print("Positions: [\n\t" + position.get(position.size() - 1).get("count").getAsInt() + " positions...");
                    /*for (int l = 1; l <= positionBuff.limit(); l++) {
                        System.out.printf("%6.2f", positionBuff.get());
                        if(l % 3 != 0)
                            System.out.print(", ");
                        else if(l != positionBuff.limit())
                            System.out.print(",\n\t");
                        else
                            System.out.println();
                    }*/
                    System.out.println("\n]\n");


                    int normalIndex = attributes.get("NORMAL").getAsInt();
                    normal.add(rootAccessors.get(normalIndex).getAsJsonObject());
                    System.out.println("Normal: " + normal);

                    bufferView = rootBefferViews.get(normal.get(normal.size() - 1).get("bufferView").getAsInt()).getAsJsonObject();
                    System.out.println("Buffer view: " + bufferView);

                    buffer = rootBuffers.get(bufferView.get("buffer").getAsInt()).getAsJsonObject();
                    System.out.println("Buffer: " + buffer.keySet());

                    byteBuff = Buffers.newDirectByteBuffer(binData, bufferView.get("byteOffset").getAsInt(), bufferView.get("byteLength").getAsInt());
                    normalBuff.add(byteBuff.asFloatBuffer().duplicate());
                    System.out.print("Normals: [\n\t" + normal.get(normal.size() - 1).get("count").getAsInt() + " normals...");
                    /*for (int l = 1; l <= normalBuff.limit(); l++) {
                        System.out.printf("%6.2f", normalBuff.get());
                        if(l % 3 != 0)
                            System.out.print(", ");
                        else if(l != normalBuff.limit())
                            System.out.print(",\n\t");
                        else
                            System.out.println();
                    }*/
                    System.out.println("\n]\n");

                    int indicesIndex = primitive.get("indices").getAsInt();
                    JsonObject indices = rootAccessors.get(indicesIndex).getAsJsonObject();
                    System.out.println("Indices: " + indices);

                    int materialIndex = primitive.get("material").getAsInt();
                    JsonObject material = rootMaterials.get(materialIndex).getAsJsonObject();
                    System.out.println(material.keySet());
                }
            }
        }
    }

    /*public void load(GL4 gl){
        VAO = new int[];
        gl.glGenVertexArrays(1, VAO, 0);

        VBO = new int[1];
        gl.glGenBuffers(1, VBO, 0);

        EBO = new int[1];
        gl.glGenBuffers(1, EBO, 0);

        gl.glBindVertexArray(VAO[0]);

        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, VBO[0]);
        gl.glBufferData(GL4.GL_ARRAY_BUFFER,
                verts.limit() * Buffers.SIZEOF_FLOAT,
                verts, GL4.GL_STATIC_DRAW);

        //gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, EBO[0]);
        /*gl.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER,
                indices.limit() * Buffers.SIZEOF_INT,
                indices, GL4.GL_STATIC_DRAW);*/
        /*

        gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false,
                8 * Buffers.SIZEOF_FLOAT, 0);
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(1, 3, GL4.GL_FLOAT, false,
                8 * Buffers.SIZEOF_FLOAT, 3 * Buffers.SIZEOF_FLOAT);
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(2, 2, GL4.GL_FLOAT, false,
                8 * Buffers.SIZEOF_FLOAT, 6 * Buffers.SIZEOF_FLOAT);
        gl.glEnableVertexAttribArray(2);

        gl.glBindVertexArray(VAO[1]);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, VBO[0]);
        //gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, EBO[0]);
        gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT,
                false, 8 * Buffers.SIZEOF_FLOAT,
                0);
        gl.glEnableVertexAttribArray(0);
    }*/

}
