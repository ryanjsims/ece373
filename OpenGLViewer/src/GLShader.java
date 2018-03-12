import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;

public class GLShader {
    ShaderProgram prog;

    public GLShader(GL4 gl, String vertName, String fragName){
        ShaderCode vertShader = ShaderCode.create(gl,
                GL4.GL_VERTEX_SHADER,
                this.getClass(),
                "shaders",
                "shaders/bin",
                vertName,
                null,
                null,
                true);
        ShaderCode fragShader = ShaderCode.create(gl,
                GL4.GL_FRAGMENT_SHADER,
                this.getClass(),
                "shaders",
                "shaders/bin",
                fragName,
                null,
                null,
                true);
        vertShader.compile(gl, System.err);
        fragShader.compile(gl, System.err);

        vertShader.defaultShaderCustomization(gl, true, true);
        fragShader.defaultShaderCustomization(gl, true, true);

        prog = new ShaderProgram();
        prog.init(gl);
        prog.add(vertShader);
        prog.add(fragShader);
        prog.link(gl, System.err);

        gl.glDeleteShader(vertShader.id());
        gl.glDeleteShader(fragShader.id());
    }

    public int program(){
        return prog.program();
    }

    public void use(GL4 gl){
        gl.glUseProgram(program());
    }

    public void setBool(GL4 gl, String name, boolean value){
        int val = value ? 1 : 0;
        gl.glUniform1i(gl.glGetUniformLocation(program(), name), val);
    }

    public void setInt(GL4 gl, String name, int value){
        gl.glUniform1i(gl.glGetUniformLocation(program(), name), value);
    }

    public void setFloat(GL4 gl, String name, float value) {
        gl.glUniform1f(gl.glGetUniformLocation(program(), name), value);
    }

    public void setVec3(GL4 gl, String name, float[] value){
        gl.glUniform3fv(gl.glGetUniformLocation(program(), name), 1, value, 0);
    }

    public void setVec3(GL4 gl, String name, float[] value, int off, int count){
        gl.glUniform3fv(gl.glGetUniformLocation(program(), name), count, value, off);
    }

    public void setMat4(GL4 gl, String name, float[] value){
        gl.glUniformMatrix4fv(gl.glGetUniformLocation(program(), name), 1, false, value, 0);
    }

    public void setMat4(GL4 gl, String name, float[] value, int off, int count, boolean transpose){
        gl.glUniformMatrix4fv(gl.glGetUniformLocation(program(), name), count, transpose, value, off);
    }
}
