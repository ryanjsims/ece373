import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;

public class HelloJOGL implements GLEventListener {
	
	private GLWindow window;
	private Animator anim;
	private ShaderProgram shaderProgram;
	private IntBuffer VAO, VBO;
	
	private Matrix4 projection, view, model;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		HelloJOGL jogl = new HelloJOGL();
	}
	
	public HelloJOGL(){
		GLProfile glp = GLProfile.getMaxProgrammableCore(true);
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);
		
		Display disp = NewtFactory.createDisplay(null);
		Screen screen = NewtFactory.createScreen(disp, 0);
		window = GLWindow.create(screen, caps);
		
		window.setSize(800, 600);
		window.setTitle("Hello Java OpenGL!");
		window.setVisible(true);
		
		window.addGLEventListener(this);
		
		anim = new Animator(window);
		anim.start();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();
		
		gl.glViewport(0, 0, 800, 600);
		
		ShaderCode vertexShader = ShaderCode.create(gl, GL4.GL_VERTEX_SHADER, this.getClass(), "shaders", "shaders/bin", "helloJOGLCubeVertex", true);
		vertexShader.compile(gl, System.err);
		vertexShader.defaultShaderCustomization(gl, true, true);
		
		ShaderCode fragmentShader = ShaderCode.create(gl, GL4.GL_FRAGMENT_SHADER, this.getClass(), "shaders", "shaders/bin", "helloJOGLCubeFragment", true);
		fragmentShader.compile(gl, System.err);
		fragmentShader.defaultShaderCustomization(gl, true, true);
		
		shaderProgram = new ShaderProgram();
		shaderProgram.init(gl);
		shaderProgram.add(vertexShader);
		shaderProgram.add(fragmentShader);
		shaderProgram.link(gl, System.err);
		
		gl.glDeleteShader(vertexShader.id());
		gl.glDeleteShader(fragmentShader.id());
		
		float[] verticesArray = new float[]{
			  //x,     y,    z
			-0.5f, -0.5f, 0.0f, //Front
			 0.5f, -0.5f, 0.0f,
			 0.5f,  0.5f, 0.0f,
			 
			 -0.5f, -0.5f, 0.0f,
			  0.5f,  0.5f, 0.0f,
			 -0.5f,  0.5f, 0.0f,
			 
			 -0.5f, -0.5f, 0.0f, //Left
			 -0.5f, -0.5f, 1.0f,
			 -0.5f,  0.5f, 0.0f,
			 
			 -0.5f,  0.5f, 1.0f,
			 -0.5f, -0.5f, 1.0f,
			 -0.5f,  0.5f, 0.0f,
			 
			  0.5f, -0.5f, 0.0f, //Right
			  0.5f,  0.5f, 0.0f,
			  0.5f,  0.5f, 1.0f,
			 
			  0.5f, -0.5f, 0.0f,
			  0.5f,  0.5f, 1.0f,
			  0.5f, -0.5f, 1.0f,
			 
			 -0.5f,  0.5f, 0.0f, //Top
			  0.5f,  0.5f, 0.0f,
			 -0.5f,  0.5f, 1.0f,
			 
			 -0.5f,  0.5f, 1.0f,
			  0.5f,  0.5f, 0.0f,
			  0.5f,  0.5f, 1.0f,
			 
			 -0.5f, -0.5f, 0.0f, //Bottom
			  0.5f, -0.5f, 0.0f,
			 -0.5f, -0.5f, 1.0f,
			 
			 -0.5f, -0.5f, 1.0f,
			  0.5f, -0.5f, 0.0f,
			  0.5f, -0.5f, 1.0f,
			 
			 -0.5f, -0.5f, 1.0f, //Back
			  0.5f, -0.5f, 1.0f,
			  0.5f,  0.5f, 1.0f,
			 
			 -0.5f, -0.5f, 1.0f,
			  0.5f,  0.5f, 1.0f,
			 -0.5f,  0.5f, 1.0f,
		};
		FloatBuffer vertices = GLBuffers.newDirectFloatBuffer(verticesArray);
		
		VAO = IntBuffer.allocate(1);
		gl.glGenVertexArrays(1, VAO);
		gl.glBindVertexArray(VAO.get(0));
		
		VBO = IntBuffer.allocate(1);
		gl.glGenBuffers(1, VBO);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, VBO.get(0));
		gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices.limit() * GLBuffers.SIZEOF_FLOAT, vertices, GL.GL_STATIC_DRAW);
	
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 3 * GLBuffers.SIZEOF_FLOAT, 0);
		gl.glEnableVertexAttribArray(0);
		
		projection = new Matrix4();
		projection.makePerspective((float)(Math.PI / 4), (float)window.getWidth() / (float)window.getHeight(), 0.1f, 1000f);
		model = new Matrix4();
		model.rotate((float)Math.toRadians(45), 1, 1, 0);
		view = new Matrix4();
		view.translate(0.0f, 0.0f, -3.0f);
		
		gl.glEnable(GL.GL_DEPTH_TEST);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();
		gl.glDeleteVertexArrays(1, VAO);
		gl.glDeleteBuffers(1, VBO);
		anim.stop();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();
		gl.glUseProgram(shaderProgram.program());
		
		model.loadIdentity();
		float angrad = (float)(2 * Math.PI * ((System.currentTimeMillis() % 2000) / 2000.0f));
		model.rotate(angrad, 1, 1, 0);
		
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(shaderProgram.program(), "projection"), 1, false, projection.getMatrix(), 0);
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(shaderProgram.program(), "view"), 1, false, view.getMatrix(), 0);
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(shaderProgram.program(), "model"), 1, false, model.getMatrix(), 0);
		
		gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
		gl.glBindVertexArray(VAO.get(0));
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, 36);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL4 gl = drawable.getGL().getGL4();		
	}

}
