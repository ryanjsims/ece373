import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class GLMesh {
    private int[] vao, vbo, ebo;
    private ArrayList<FloatBuffer> vertices;
    private ArrayList<IntBuffer> indices;
    //private ArrayList<GLTexture> textures;
    private String name;

    public GLMesh(JsonObject mesh, JsonObject root, byte[] data, GL4 gl){
        name = mesh.get("name").getAsString();
        JsonArray primitives = mesh.getAsJsonArray("primitives");
        for (int i = 0; i < primitives.size(); i++) {
            JsonObject prim = primitives.get(i).getAsJsonObject();
            int indicesIndex = prim.get("indices").getAsInt();
            indices.add(getByteBuffer(indicesIndex, root, data).asIntBuffer());

            int positionsIndex = prim.get("attributes").getAsJsonObject().get("POSITION").getAsInt();
            FloatBuffer pos = getByteBuffer(positionsIndex, root, data).asFloatBuffer();

            int normalsIndex = prim.get("attributes").getAsJsonObject().get("NORMAL").getAsInt();
            FloatBuffer norm = getByteBuffer(normalsIndex, root, data).asFloatBuffer();

            FloatBuffer verts = FloatBuffer.allocate(pos.limit() + norm.limit());
            while(norm.position() != norm.limit()){
                for(int j = 0; j < 3; j++)
                    verts.put(pos.get());
                for(int j = 0; j < 3; j++)
                    verts.put(norm.get());
            }
            for(int j = 0; j < verts.limit(); j++){
                System.out.println(verts.get());
            }
            verts.rewind();
            vertices.add(verts);
        }

        setUpMesh(gl);
    }

    private void setUpMesh(GL4 gl){
        vao = new int[1];
        gl.glGenVertexArrays(1, vao, 0);

        vbo = new int[vertices.size()];
        gl.glGenBuffers(vbo.length, vbo, 0);

        ebo = new int[indices.size()];
        gl.glGenBuffers(ebo.length, ebo, 0);

        gl.glBindVertexArray(vao[0]);

        //for each vbo/ebo generated...
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, vbo[0]);
        gl.glBufferData(GL4.GL_ARRAY_BUFFER,
                vertices.get(0).limit() * Buffers.SIZEOF_FLOAT,
                vertices.get(0), GL4.GL_STATIC_DRAW);

        gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false,
                6 * Buffers.SIZEOF_FLOAT, 0);
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(1, 3, GL4.GL_FLOAT, false,
                6 * Buffers.SIZEOF_FLOAT, 3 * Buffers.SIZEOF_FLOAT);
        gl.glEnableVertexAttribArray(1);

        gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, ebo[0]);
        gl.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER,
                indices.get(0).limit() * Buffers.SIZEOF_INT,
                indices.get(0), GL4.GL_STATIC_DRAW);

    }

    private ByteBuffer getByteBuffer(int index, JsonObject root, byte[] data){
        JsonObject accessor = root.getAsJsonArray("accessors").get(index).getAsJsonObject();
        JsonObject bufferView = root.getAsJsonArray("bufferViews").get(accessor.get("bufferView").getAsInt()).getAsJsonObject();
        int byteLength = bufferView.get("byteLength").getAsInt();
        int byteOffset = bufferView.get("byteOffset").getAsInt();
        return Buffers.newDirectByteBuffer(data, byteOffset, byteLength);
    }

    /*private class GLTexture{
        int id;
        String type;
        private GLTexture(int id, String type){
            this.id = id;
            this.type = type;
        }
    }*/
}
