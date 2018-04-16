import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.zip.DataFormatException;

public class ModelViewerGL4
        implements GLEventListener, KeyListener{

    public static void main(String[] args){
        ModelViewerGL4 viewer = new ModelViewerGL4();
        //viewer.view();
        /*try {
            GLBModelReader reader = new GLBModelReader("res/hemisphere.glb");
        } catch(IOException e){
            e.printStackTrace();
        } catch(DataFormatException e){
            e.printStackTrace();
        }*/
    }

    private GLWindow window;
    private GLCamera camera;
    private Animator anim;
    private GLShader spLighting, spLamp;
    private int[] VAO, tex;
    private Matrix4 model;
    private TextureData texture;
    private float[][] cubePositions;
    private float[] lightPos = new float[]{1.2f, 1.0f, -2.0f};


    public ModelViewerGL4(){
        float[] loc = new float[]{0.0f, 0.0f, 3.0f};
        float[] up = new float[]{0.0f, 1.0f, 0.0f};
        float[] front = new float[]{0.0f, 0.0f, -1.0f};
        camera = new GLCamera(loc, front, up);
        camera.canFly(true);

        GLProfile glp = GLProfile.getMaxProgrammableCore(true);
        GLCapabilities caps = new GLCapabilities(glp);
        caps.setDoubleBuffered(true);
        caps.setHardwareAccelerated(true);
        Display disp = NewtFactory.createDisplay(null);
        Screen screen = NewtFactory.createScreen(disp, 0);
        window = GLWindow.create(
                screen,
                caps
        );
        window.setSize(1000, 1000);
        window.setTitle("3D Model Viewer - OpenGL4");
        window.addGLEventListener(this);
        window.addKeyListener(this);
        window.addMouseListener(camera);
        window.setVisible(true);
        anim = new Animator(window);
        anim.start();
    }

    private void view(){
        printMatrix(model);
    }

    private void printMatrix(Matrix4 mat){
        for(int i = 1; i <= mat.getMatrix().length; i++){
            System.out.printf("%.2f", mat.getMatrix()[4 * (i - 1) % 16 + (i - 1) / 4]);
            if(i % 4 == 0)
                System.out.print("\n");
            else
                System.out.print(" ");
        }
        System.out.print("\n");
    }

    //GLEventListener
    @Override
    public void init(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();
        gl.glViewport(0, 0, 1000, 1000);
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        gl.glEnable(GL4.GL_DEPTH_TEST);

        window.confinePointer(true);
        window.setPointerVisible(false);

        spLighting = new GLShader(gl, "vertex", "toy");
        spLamp = new GLShader(gl, "vertex", "light");

        cubePositions = new float[][]{
                new float[]{ 0.0f,  0.0f,  0.0f},
                new float[]{ 2.0f,  5.0f, -15.0f},
                new float[]{-1.5f, -2.2f, -2.5f},
                new float[]{-3.8f, -2.0f, -12.3f},
                new float[]{ 2.4f, -0.4f, -3.5f},
                new float[]{-1.7f,  3.0f, -7.5f},
                new float[]{ 1.3f, -2.0f, -2.5f},
                new float[]{ 1.5f,  2.0f, -2.5f},
                new float[]{ 1.5f,  0.2f, -1.5f},
                new float[]{-1.3f,  1.0f, -1.5f}
        };

        float[] verticesForIndices = new float[]{
                // Positions        // Colors        //Texture coords
                -0.5f, -0.5f, 0.5f, //1.0f, 0.0f, 0.0f, 1.0f, 0.0f,  //bottom right Front face
                 0.5f, -0.5f, 0.5f, //0.0f, 1.0f, 0.0f, 0.0f, 0.0f,  //bottom left
                -0.5f,  0.5f, 0.5f, //0.0f, 0.0f, 1.0f, 1.0f, 1.0f,  //top right
                 0.5f,  0.5f, 0.5f, //1.0f, 1.0f, 0.0f, 0.0f, 1.0f,  //top left

                -0.5f, -0.5f, -0.5f, //1.0f, 1.0f, 0.0f, 0.0f, 1.0f,  //bottom right Back face
                 0.5f, -0.5f, -0.5f, //0.0f, 0.0f, 1.0f, 1.0f, 1.0f,  //bottom left
                -0.5f,  0.5f, -0.5f, //0.0f, 1.0f, 0.0f, 0.0f, 0.0f,  //top right
                 0.5f,  0.5f, -0.5f, //1.0f, 0.0f, 0.0f, 1.0f, 0.0f,  //top left
        };

        float[] vertices = new float[]{
                // positions          // normals           // texture coords
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f, 0.0f,
                 0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f, 0.0f,
                 0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f, 1.0f,
                 0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f, 0.0f,

                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   0.0f, 0.0f,
                 0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   1.0f, 0.0f,
                 0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   1.0f, 1.0f,
                 0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   0.0f, 0.0f,

                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f, 0.0f,
                -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  0.0f, 0.0f,
                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f, 0.0f,

                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  1.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  0.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  1.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f, 1.0f,

                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f, 1.0f
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

        FloatBuffer verts = GLBuffers.newDirectFloatBuffer(vertices);
        IntBuffer indices = GLBuffers.newDirectIntBuffer(indexData);

        VAO = new int[2];
        gl.glGenVertexArrays(2, VAO, 0);

        int[] VBO = new int[1];
        gl.glGenBuffers(1, VBO, 0);

        //int[] EBO = new int[1];
        //gl.glGenBuffers(1, EBO, 0);

        gl.glBindVertexArray(VAO[0]);

        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, VBO[0]);
        gl.glBufferData(GL4.GL_ARRAY_BUFFER,
                verts.limit() * Buffers.SIZEOF_FLOAT,
                verts, GL4.GL_STATIC_DRAW);

        //gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, EBO[0]);
        /*gl.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER,
                indices.limit() * Buffers.SIZEOF_INT,
                indices, GL4.GL_STATIC_DRAW);*/

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

        /*
        gl.glVertexAttribPointer(1, 3, GL4.GL_FLOAT, false,
                8 * Buffers.SIZEOF_FLOAT, 3 * Buffers.SIZEOF_FLOAT);
        gl.glEnableVertexAttribArray(1);

        gl.glVertexAttribPointer(2, 2, GL4.GL_FLOAT, false,
                8 * Buffers.SIZEOF_FLOAT, 6 * Buffers.SIZEOF_FLOAT);
        gl.glEnableVertexAttribArray(2);
        */
        TextureData specular = null;
        try {
            texture = TextureIO.newTextureData(gl.getGLProfile(), new File("res/container2.png"), GL4.GL_TEXTURE_2D, GL4.GL_RGBA, false, "png");
            specular = TextureIO.newTextureData(gl.getGLProfile(), new File("res/container2_specular.png"), GL4.GL_TEXTURE_2D, GL4.GL_RGBA, false, "png");
        } catch(IOException e){
            e.printStackTrace();
        }

        tex = new int[2];
        gl.glGenTextures(2, tex, 0);

        gl.glBindTexture(GL4.GL_TEXTURE_2D, tex[0]);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, GL4.GL_REPEAT);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, GL4.GL_REPEAT);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
        gl.glTexImage2D(GL4.GL_TEXTURE_2D, 0, GL4.GL_RGBA,
                texture.getWidth(), texture.getHeight(), 0,
                GL4.GL_RGBA, GL4.GL_UNSIGNED_BYTE, texture.getBuffer());
        gl.glGenerateMipmap(GL4.GL_TEXTURE_2D);

        gl.glBindTexture(GL4.GL_TEXTURE_2D, tex[1]);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, GL4.GL_REPEAT);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, GL4.GL_REPEAT);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
        gl.glTexImage2D(GL4.GL_TEXTURE_2D, 0, GL4.GL_RGBA,
                specular.getWidth(), specular.getHeight(), 0,
                GL4.GL_RGBA, GL4.GL_UNSIGNED_BYTE, specular.getBuffer());
        gl.glGenerateMipmap(GL4.GL_TEXTURE_2D);

        //gl.glPolygonMode(GL4.GL_FRONT_AND_BACK, GL4.GL_LINE);

        model = new Matrix4();
        model.rotate((float)Math.toRadians(-55.0), 1.0f, 0.0f, 0.0f);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        anim.stop();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();
        gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);

        lightPos[0] = (float)(5 / Math.sqrt(2) * Math.sin(System.currentTimeMillis() * Math.PI / 86400000.0));
        lightPos[1] = (float)(5 * Math.cos(System.currentTimeMillis() * Math.PI / 86400000.0));
        //lightPos[2] = (float)(2 / Math.sqrt(2) * Math.sin(System.currentTimeMillis() * Math.PI / 2000.0));

        camera.update();
        spLighting.use(gl);
        spLighting.setMat4(gl, "view", camera.getView().getMatrix());
        spLighting.setMat4(gl, "projection", camera.getProjection().getMatrix());

        gl.glActiveTexture(gl.GL_TEXTURE0);
        gl.glBindTexture(GL4.GL_TEXTURE_2D, tex[0]);
        gl.glActiveTexture(gl.GL_TEXTURE1);
        gl.glBindTexture(GL4.GL_TEXTURE_2D, tex[1]);
        gl.glBindVertexArray(VAO[0]);
        for(int i = 0; i < cubePositions.length; i++){
            model.loadIdentity();
            model.translate(cubePositions[i][0], cubePositions[i][1], cubePositions[i][2]);
            model.rotate((float)Math.toRadians(20.0f * i), 1.0f, 0.3f, 0.5f);

            Matrix4 normalModel = new Matrix4();
            for(int j = 0; j < normalModel.getMatrix().length; j++){
                normalModel.getMatrix()[j] = model.getMatrix()[j];
            }
            normalModel.invert();
            normalModel.transpose();

            spLighting.setMat4(gl, "model", model.getMatrix());
            spLighting.setMat4(gl, "normalModel", normalModel.getMatrix());
            spLighting.setInt(gl, "material.diffuse", 0);
            spLighting.setInt(gl, "material.specular", 1);
            spLighting.setFloat(gl, "material.shininess", 32.0f);
            spLighting.setVec3(gl, "light.ambient", new float[]{0.2f, 0.2f, 0.2f});
            spLighting.setVec3(gl, "light.diffuse", new float[]{0.7f, 0.7f, 0.7f});
            spLighting.setVec3(gl, "light.specular", new float[]{1.0f, 1.0f, 1.0f});
            spLighting.setVec3(gl, "light.position", lightPos);
            spLighting.setVec3(gl, "viewPos", camera.getLoc());

            gl.glDrawArrays(GL4.GL_TRIANGLES, 0, 36);
        }
        model.loadIdentity();
        model.translate(lightPos[0], lightPos[1], lightPos[2]);
        model.scale(0.2f, 0.2f, 0.2f);

        spLamp.use(gl);
        spLamp.setMat4(gl, "view", camera.getView().getMatrix());
        spLamp.setMat4(gl, "projection", camera.getProjection().getMatrix());
        spLamp.setMat4(gl, "model", model.getMatrix());

        gl.glBindVertexArray(VAO[1]);

        gl.glDrawArrays(GL4.GL_TRIANGLES, 0, 36);
        gl.glBindVertexArray(0);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL4 gl = drawable.getGL().getGL4();
        gl.glViewport(0, 0, width, height);
        camera.reshape(x, y, width, height);
    }

    //KeyListener
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            window.destroy();
        }
        if(e.isAutoRepeat())
            return;
        camera.keyHeld(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.isAutoRepeat())
            return;
        camera.keyReleased(e.getKeyCode());
    }
}
