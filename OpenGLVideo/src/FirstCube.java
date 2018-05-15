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
import com.jogamp.opengl.math.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;

public class FirstCube implements GLEventListener {
	private GLWindow window;
	private Animator anim;
	private IntBuffer VBO, VAO, EBO;
	private ShaderProgram shaderProgram;
	private Matrix4 projection, model, view;
	
	public static void main(String[] args){
		@SuppressWarnings("unused")
		FirstCube test = new FirstCube();
	}
	
	public FirstCube(){		
		GLProfile glp = GLProfile.getMaxProgrammableCore(true);
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);
		Display disp = NewtFactory.createDisplay(null);
		Screen screen = NewtFactory.createScreen(disp, 0);
		
		window = GLWindow.create(screen, caps);
		window.setSize(800, 600);
		window.setTitle("Drawing cubes!");
		
		window.addGLEventListener(this);
		
		window.setVisible(true);
		anim = new Animator(window);
		anim.start();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		VAO = IntBuffer.allocate(1);
		VBO = IntBuffer.allocate(1);
		
		EBO = IntBuffer.allocate(1); //new
		
		GL4 gl = drawable.getGL().getGL4();
		gl.glViewport(0, 0, 800, 600);
		float[] verticesArray = new float[]{
				// Positions        // Colors
                -0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f,   //bottom right Front face
                 0.5f, -0.5f, 0.5f, 0.0f, 1.0f, 0.0f,   //bottom left
                -0.5f,  0.5f, 0.5f, 0.0f, 0.0f, 1.0f,   //top right
                 0.5f,  0.5f, 0.5f, 1.0f, 1.0f, 0.0f,   //top left

                -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f,  //bottom right Back face
                 0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 1.0f,  //bottom left
                -0.5f,  0.5f, -0.5f, 0.0f, 1.0f, 0.0f,  //top right
                 0.5f,  0.5f, -0.5f, 1.0f, 0.0f, 0.0f,  //top left
		};
		
		int[] indexData = new int[]{
                0, 1, 2,
                1, 2, 3,

                2, 3, 6,
                3, 6, 7,

                0, 2, 4,
                2, 4, 6,

                1, 3, 5,
                3, 5, 7,

                0, 1, 4,
                1, 4, 5,

                4, 5, 6,
                5, 6, 7
        };
		
		FloatBuffer vertices = GLBuffers.newDirectFloatBuffer(verticesArray);
		
		IntBuffer indices = GLBuffers.newDirectIntBuffer(indexData); //New
		
		gl.glGenVertexArrays(1, VAO);
		gl.glBindVertexArray(VAO.get(0));
		
		gl.glGenBuffers(1, VBO);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, VBO.get(0));
		gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices.limit() * GLBuffers.SIZEOF_FLOAT, vertices, GL.GL_STATIC_DRAW);
		
		//Element buffer object allows us to use indices to denote vertices rather than using more data
		
		gl.glGenBuffers(1, EBO);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, EBO.get(0));
		gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indices.limit() * GLBuffers.SIZEOF_INT, indices, GL.GL_STATIC_DRAW);
		
		ShaderCode vertexShader = ShaderCode.create(gl, GL4.GL_VERTEX_SHADER, this.getClass(), "shaders", "shaders/bin", "firstCubeVertex", true);
		vertexShader.compile(gl, System.err);
		vertexShader.defaultShaderCustomization(gl, true, true);
		
		ShaderCode fragmentShader = ShaderCode.create(gl, GL4.GL_FRAGMENT_SHADER, this.getClass(), "shaders", "shaders/bin", "firstCubeFragment", true);
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
		
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6 * GLBuffers.SIZEOF_FLOAT, 0); //Size from 3 to 6 since we added colors
		gl.glEnableVertexAttribArray(0);
		
		gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6 * GLBuffers.SIZEOF_FLOAT, 3 * GLBuffers.SIZEOF_FLOAT); //Size from 3 to 6 since we added colors
		gl.glEnableVertexAttribArray(1); //new
		
		projection = new Matrix4();
		projection.makePerspective((float)Math.toRadians(45.0f), window.getWidth() / window.getHeight(), 0.1f, 100f);
		view = new Matrix4();
		view.translate(0.0f, 0.0f, -3.0f);
		model = new Matrix4();
		gl.glEnable(GL.GL_DEPTH_TEST);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();
		gl.glDeleteVertexArrays(1, VAO);
		gl.glDeleteBuffers(1, VBO);
		gl.glDeleteBuffers(1, EBO);
		anim.stop();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();
		gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
		gl.glBindVertexArray(VAO.get(0));
				
		model.loadIdentity();
		model.rotate((float)((6.28 * (System.currentTimeMillis() % 1000) / 1000)), 1, 1, 0);
		
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(shaderProgram.program(), "projection"), 1, false, projection.getMatrix(), 0);
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(shaderProgram.program(), "view"), 1, false, view.getMatrix(), 0);
		gl.glUniformMatrix4fv(gl.glGetUniformLocation(shaderProgram.program(), "model"), 1, false, model.getMatrix(), 0);
		
		gl.glDrawElements(GL.GL_TRIANGLES, 36, GL.GL_UNSIGNED_INT, 0);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// Called on window resize
		GL4 gl = drawable.getGL().getGL4();
		gl.glViewport(0, 0, width, height);
		projection.loadIdentity();
		projection.makePerspective((float)Math.toRadians(45.0f), width / height, 0.1f, 100f);
	}
}
