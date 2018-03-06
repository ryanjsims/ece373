import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.jogamp.opengl.GLProfile.GL4;

public class ModelViewer extends Frame implements GLEventListener{
    /*
     * TODO: Make this the GUI class after designing structure for other classes as needed.
     *       Look at documentation for OpenGL hooks for java.
     * */
    public static void main(String[] args){
        ModelViewer viewer = new ModelViewer();
    }

    public ModelViewer(){
        super("3D Model Viewer");

        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dispose();
            }
        });
        setSize(1024, 768);
        setLocation(60, 60);

        setVisible(true);


        setupJOGL();
    }

    private void setupJOGL(){
        GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        caps.setDoubleBuffered(true);
        caps.setHardwareAccelerated(true);
        GLCanvas canvas = new GLCanvas(caps);
        canvas.addGLEventListener(this);

        add(canvas, BorderLayout.CENTER);

        Animator anim = new Animator(canvas);
        anim.start();
    }

    @Override
    public void init(GLAutoDrawable drawable){
        GL2 gl = (GL2)drawable.getGL();

        gl.glClearColor(0, 0, 0, 0);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 1, 0, 1, -1, 1);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        dispose();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = (GL2)drawable.getGL();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glBegin(GL.GL_TRIANGLES);

        gl.glColor3f(1, 0, 0);
        gl.glVertex3f(0.25f, 0.25f, 0);

        gl.glColor3f(0, 1, 0);
        gl.glVertex3f(0.5f, 0.25f, 0);

        gl.glColor3f(0, 0, 1);
        gl.glVertex3f(0.25f, 0.5f, 0);

        gl.glEnd();
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {

    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged){

    }



}
