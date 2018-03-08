import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.zip.DataFormatException;

public class GLBModelReader {
    private String type, JSONData;
    private byte binData[];
    private int version, length;
    public GLBModelReader(String filename) throws DataFormatException, IOException{
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
                    JSONData = new String(chunkData, "UTF-8");
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
    }
    //TODO: Parse JSON and access binary information

}
