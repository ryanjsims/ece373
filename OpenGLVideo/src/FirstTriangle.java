import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class FirstTriangle implements GLEventListener{
	private GLWindow window;
	private Animator anim;
	private IntBuffer VBO, VAO;
	private ShaderProgram shaderProgram;
	
	public static void main(String[] args){
		@SuppressWarnings("unused")
		FirstTriangle test = new FirstTriangle();
	}
	
	public FirstTriangle(){		
		GLProfile glp = GLProfile.getMaxProgrammableCore(true);
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);
		Display disp = NewtFactory.createDisplay(null);
		Screen screen = NewtFactory.createScreen(disp, 0);
		
		window = GLWindow.create(screen, caps);
		window.setSize(800, 600);
		window.setTitle("Java Triangle!");
		
		window.addGLEventListener(this);
		
		window.setVisible(true);
		anim = new Animator(window);
		anim.start();
	}

	@Override //New
	public void init(GLAutoDrawable drawable) {
		VAO = IntBuffer.allocate(1);
		VBO = IntBuffer.allocate(1);
		GL4 gl = drawable.getGL().getGL4();
		gl.glViewport(0, 0, 800, 600);
		float[] verticesArray = new float[]{
			  //x,     y,    z
			-0.5f, -0.5f, 0.0f,
			 0.5f, -0.5f, 0.0f,
			 0.0f,  0.5f, 0.0f
		};
		FloatBuffer vertices = GLBuffers.newDirectFloatBuffer(verticesArray);
		
		gl.glGenVertexArrays(1, VAO);
		gl.glBindVertexArray(VAO.get(0));
		
		gl.glGenBuffers(1, VBO);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, VBO.get(0));
		gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices.limit() * GLBuffers.SIZEOF_FLOAT, vertices, GL.GL_STATIC_DRAW);
		
		ShaderCode vertexShader = ShaderCode.create(gl, GL4.GL_VERTEX_SHADER, this.getClass(), "shaders", "shaders/bin", "firstTriangleVertex", true);
		vertexShader.compile(gl, System.err);
		vertexShader.defaultShaderCustomization(gl, true, true);
		
		ShaderCode fragmentShader = ShaderCode.create(gl, GL4.GL_FRAGMENT_SHADER, this.getClass(), "shaders", "shaders/bin", "firstTriangleFragment", true);
		fragmentShader.compile(gl, System.err);
		fragmentShader.defaultShaderCustomization(gl, true, true);
		
		shaderProgram = new ShaderProgram();
		shaderProgram.init(gl);
		shaderProgram.add(vertexShader);
		shaderProgram.add(fragmentShader);
		shaderProgram.link(gl, System.err);
		gl.glUseProgram(shaderProgram.program());
		
		gl.glDeleteShader(vertexShader.id());
		gl.glDeleteShader(fragmentShader.id());
		
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 3 * GLBuffers.SIZEOF_FLOAT, 0);
		gl.glEnableVertexAttribArray(0);
	}

	@Override //New
	public void dispose(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();
		gl.glDeleteVertexArrays(1, VAO);
		gl.glDeleteBuffers(1, VBO);
		anim.stop();
	}

	@Override //New
	public void display(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();
		gl.glClear(GL4.GL_COLOR_BUFFER_BIT);
		gl.glBindVertexArray(VAO.get(0));
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, 3);
	}

	@Override //New
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// Called on window resize
		GL4 gl = drawable.getGL().getGL4();
		gl.glViewport(0, 0, width, height);
	}
}
