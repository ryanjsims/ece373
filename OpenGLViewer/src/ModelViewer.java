//import com.jogamp.newt.event.MouseEvent;
//import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import static com.jogamp.opengl.GLProfile.GL4;

public class ModelViewer extends Frame implements GLEventListener{
    /*
     * TODO: Make this the GUI class after designing structure for other classes as needed.
     *       Look at documentation for OpenGL hooks for java.
     * */

    private GLU glu = new GLU();
    private Point center;
    private float fov = 45.0f,
            asp = 1.0f,
            hAngle = 3.14f,
            vAngle = 0f,
            mouseSpeed = 0.05f,
            speed = 0.1f;

    private int x = 60, y = 60, width = 1024, height = 768;
    private long currTime, prevTime;
    private Vector3D loc;

    private boolean movingDir[] = {
            false,
            false,
            false,
            false,
            false,
            false
    };

    private int dirKeys[] = {
            KeyEvent.VK_W,
            KeyEvent.VK_A,
            KeyEvent.VK_S,
            KeyEvent.VK_D,
            KeyEvent.VK_CONTROL,
            KeyEvent.VK_SPACE
    };

    private mouseInput m;
    private GLCanvas canvas;

    public static void main(String[] args){
        ModelViewer viewer = new ModelViewer();
        viewer.setVisible(true);

    }

    public ModelViewer(){
        super("3D Model Viewer");
        setUndecorated(true);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dispose();
                System.exit(0);
            }
        });
        loc = new Vector3D(0, 0, 0);
        setLocation(x, y);
        setVisible(true);

        setupJOGL();
    }

    private void setupJOGL(){
        GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        caps.setDoubleBuffered(true);
        caps.setHardwareAccelerated(true);
        canvas = new GLCanvas(caps);
        canvas.addGLEventListener(this);

        m = new mouseInput();
        canvas.addMouseListener(m);
        canvas.addMouseMotionListener(m);
        canvas.addMouseWheelListener(m);

        canvas.addKeyListener(new KeyInput());

        canvas.setPreferredSize(new Dimension(width, height));
        center = new Point(width / 2, height / 2);
        this.

        add(canvas, BorderLayout.CENTER);
        canvas.requestFocus();
        pack();
        SwingUtilities.convertPointToScreen(center, this);

        System.out.printf("Top left corner: %d %d\n", x, y);
        Animator anim = new Animator(canvas);
        anim.start();
    }

    @Override
    public void init(GLAutoDrawable drawable){
        GL2 gl = drawable.getGL().getGL2();

        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glEnable(gl.GL_CULL_FACE);

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        //dispose();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        prevTime = System.currentTimeMillis();
        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(fov, asp, 1.0, 1000.0);
        setView();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glTranslatef( 0, 0, -5 );

        //gl.glRotatef(hAngle, 1, 0, 0);
        //gl.glRotatef(vAngle, 0, 1, 0);
        gl.glBegin(GL2.GL_QUADS);

        gl.glColor3f(1f,0f,0f); //red color
        gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Top)
        gl.glVertex3f( -1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Top)
        gl.glVertex3f( -1.0f, 1.0f, 1.0f ); // Bottom Left Of The Quad (Top)
        gl.glVertex3f( 1.0f, 1.0f, 1.0f ); // Bottom Right Of The Quad (Top)

        gl.glColor3f( 0f,1f,0f ); //green color
        gl.glVertex3f( 1.0f, -1.0f, 1.0f ); // Top Right Of The Quad
        gl.glVertex3f( -1.0f, -1.0f, 1.0f ); // Top Left Of The Quad
        gl.glVertex3f( -1.0f, -1.0f, -1.0f ); // Bottom Left Of The Quad
        gl.glVertex3f( 1.0f, -1.0f, -1.0f ); // Bottom Right Of The Quad

        gl.glColor3f( 0f,0f,1f ); //blue color
        gl.glVertex3f( 1.0f, 1.0f, 1.0f ); // Top Right Of The Quad (Front)
        gl.glVertex3f( -1.0f, 1.0f, 1.0f ); // Top Left Of The Quad (Front)
        gl.glVertex3f( -1.0f, -1.0f, 1.0f ); // Bottom Left Of The Quad
        gl.glVertex3f( 1.0f, -1.0f, 1.0f ); // Bottom Right Of The Quad

        gl.glColor3f( 1f,1f,0f ); //yellow (red + green)
        gl.glVertex3f( 1.0f, -1.0f, -1.0f ); // Bottom Left Of The Quad
        gl.glVertex3f( -1.0f, -1.0f, -1.0f ); // Bottom Right Of The Quad
        gl.glVertex3f( -1.0f, 1.0f, -1.0f ); // Top Right Of The Quad (Back)
        gl.glVertex3f( 1.0f, 1.0f, -1.0f ); // Top Left Of The Quad (Back)

        gl.glColor3f( 1f,0f,1f ); //purple (red + green)
        gl.glVertex3f( -1.0f, 1.0f, 1.0f ); // Top Right Of The Quad (Left)
        gl.glVertex3f( -1.0f, 1.0f, -1.0f ); // Top Left Of The Quad (Left)
        gl.glVertex3f( -1.0f, -1.0f, -1.0f ); // Bottom Left Of The Quad
        gl.glVertex3f( -1.0f, -1.0f, 1.0f ); // Bottom Right Of The Quad

        gl.glColor3f( 0f,1f, 1f ); //sky blue (blue +green)
        gl.glVertex3f( 1.0f, 1.0f, -1.0f ); // Top Right Of The Quad (Right)
        gl.glVertex3f( 1.0f, 1.0f, 1.0f ); // Top Left Of The Quad
        gl.glVertex3f( 1.0f, -1.0f, 1.0f ); // Bottom Left Of The Quad
        gl.glVertex3f( 1.0f, -1.0f, -1.0f ); // Bottom Right Of The Quad

        gl.glEnd();
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        if(this.height <= 0)
            this.height = 1;

        asp = (float) this.width / (float) this.height;
        gl.glViewport(0, 0, this.width, this.height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(fov, asp, 1.0, 1000.0);
        setView();

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void setView(){
        Vector3D vel = new Vector3D(0, 0, 0);
        Vector3D right = new Vector3D(
                (float)Math.sin((double)hAngle - 3.14/2.0),
                0,
                (float)Math.cos((double)hAngle - 3.14/2.0)
        );
        Vector3D dir = new Vector3D(hAngle, vAngle);
        Vector3D up = dir.cross(right);
        Vector3D moves[] = {
                dir.sMult(speed),
                right.sMult(speed),
                dir.sMult(-speed),
                right.sMult(-speed),
                up.sMult(-speed),
                up.sMult(speed)
        };
        for(int i = 0; i < movingDir.length; i++){
            if(movingDir[i]){
                vel.translate(moves[i]);
            }
        }
        loc.translate(vel);
        Vector3D point = loc.add(dir);
        glu.gluLookAt(loc.x, loc.y, loc.z, point.x, point.y, point.z, up.x, up.y, up.z);
    }

    private class Vector3D {
        public float x, y, z;
        public Vector3D(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Constructs a Vector3D from spherical coordinates
         * @param hAngle is the horizontal angle of the vector
         * @param vAngle is the vertical angle of the vector
         */
        public Vector3D(float hAngle, float vAngle){
            x = (float)Math.cos(vAngle) * (float)Math.sin(hAngle);
            y = (float)Math.sin(vAngle);
            z = (float)Math.cos(vAngle) * (float)Math.cos(hAngle);
        }

        public void translate(float dx, float dy, float dz){
            x += dx;
            y += dy;
            z += dz;
        }

        public void translate(Vector3D other){
            x += other.x;
            y += other.y;
            z += other.z;
        }

        public Vector3D add(Vector3D other){
            float x = this.x + other.x;
            float y = this.y + other.y;
            float z = this.z + other.z;
            return new Vector3D(x, y, z);
        }

        public Vector3D sMult(float scalar){
            float x = this.x * scalar;
            float y = this.y * scalar;
            float z = this.z * scalar;
            return new Vector3D(x, y, z);
        }

        public Vector3D cross(Vector3D other){
            float x = this.y * other.z - this.z * other.y;
            float y = this.z * other.x - this.x * other.z;
            float z = this.x * other.y - this.y * other.x;
            return new Vector3D(x, y, z);
        }
    }

    private class KeyInput implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            //System.out.printf("Key: %d\n", e.getKeyCode());
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                dispose();
                System.exit(0);
            }
            for(int i = 0; i < dirKeys.length; i++){
                if(e.getKeyCode() == dirKeys[i]){
                    movingDir[i] = true;
                }
            }
            if(e.getKeyCode() == KeyEvent.VK_ALT ){
                if(canvas.getMouseMotionListeners().length > 0) {
                    canvas.removeMouseMotionListener(m);
                    setCursor(Cursor.getDefaultCursor());
                }
                else {
                    setCursor(getToolkit().createCustomCursor(
                            new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB ),
                            new Point(),
                            null));
                    canvas.addMouseMotionListener(m);
                    canvas.requestFocus();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            for(int i = 0; i < dirKeys.length; i++){
                if(e.getKeyCode() == dirKeys[i]){
                    movingDir[i] = false;
                }
            }
        }
    }

    private class mouseInput extends MouseAdapter{
        Robot rbt;

        public mouseInput(){
            super();
            prevTime = System.currentTimeMillis();
            try {
                rbt = new Robot();
            } catch(AWTException e){
                e.printStackTrace();
                System.exit(1);
            }
        }
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            System.out.printf("click %d %d\n", mouseEvent.getX(), mouseEvent.getY());
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
            setCursor(getToolkit().createCustomCursor(
                    new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB ),
                    new Point(),
                    null));
            prevTime = System.currentTimeMillis();
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            setCursor(Cursor.getDefaultCursor());
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            int mouseX = mouseEvent.getX(), mouseY = mouseEvent.getY();
            currTime = System.currentTimeMillis();
            float dt = (float)(currTime - prevTime) / 1000f;
            rbt.mouseMove(center.x, center.y);
            float dH = mouseSpeed * dt * (width / 2 - mouseX);
            float dV = mouseSpeed * dt * (height / 2 - mouseY );
            hAngle -= dH;
            vAngle -= dV;
            //System.out.printf("%f %f\n%d %d\n", dH, dV, mouseX, mouseY);
            prevTime = currTime;

        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent mouseEvent) {

        }
    }



}
