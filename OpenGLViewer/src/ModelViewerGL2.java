import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;

import com.jogamp.newt.event.*;

import java.io.IOException;
import java.util.zip.DataFormatException;


public class ModelViewerGL2 implements GLEventListener, WindowListener, KeyListener, MouseListener {

    private Animator anim;
    private GLU glu = new GLU();
    private float fov = 45.0f,
            asp = 1.0f,
            hAngle = 3.14f,
            vAngle = 0f;
    private static final float mouseSpeed = 0.05f, speed = 0.1f;

    private int width = 800, height = 600;
    private long prevTime;
    private Vector3D loc;

    private Vector3D vel = new Vector3D(0, 0, 0);
    private Vector3D right = new Vector3D(0, 0, 0);
    private Vector3D dir = new Vector3D(hAngle, vAngle);
    private Vector3D up = new Vector3D(0, 0, 0);
    private Vector3D point = new Vector3D(0, 0, 0);
    private Vector3D moves[] = {
            new Vector3D(0, 0, 0),
            new Vector3D(0, 0, 0),
            new Vector3D(0, 0, 0),
            new Vector3D(0, 0, 0),
            new Vector3D(0, 0, 0),
            new Vector3D(0, 0, 0)
    };

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

    private GLWindow window;

    public static void main(String[] args){
        //ModelViewerGL2 viewer = new ModelViewerGL2();
        //viewer.window.setVisible(true);
        try {
            GLBModelReader mdl = new GLBModelReader("res/hemisphere.glb");
        } catch (IOException e){
            e.printStackTrace();
        } catch (DataFormatException e){
            e.printStackTrace();
        }
    }

    private ModelViewerGL2(){
        setupJOGL();
        window.setUndecorated(true);
        window.addWindowListener(this);
        loc = new Vector3D(0, 0, 0);
        window.setFullscreen(true);
    }

    private void setupJOGL(){
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        caps.setDoubleBuffered(true);
        caps.setHardwareAccelerated(true);
        Display display = NewtFactory.createDisplay(null);
        Screen screen = NewtFactory.createScreen(display, 0);
        window = GLWindow.create(screen, caps);

        window.addGLEventListener(this);
        window.addMouseListener(this);
        window.addKeyListener(this);
        window.addWindowListener(this);

        window.setSize(width, height);

        window.requestFocus();
        anim = new Animator(window);
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

    private void setView(){
        vel.setCoords(0, 0, 0);
        right.setCoords(
                (float)Math.sin((double)hAngle - 3.14/2.0),
                0,
                (float)Math.cos((double)hAngle - 3.14/2.0)
        );
        dir.setCoords(hAngle, vAngle);
        up.cross(dir, right);

        moves[0].sMult(dir, speed);
        moves[1].sMult(right, speed);
        moves[2].sMult(dir, -speed);
        moves[3].sMult(right, -speed);
        moves[4].sMult(up, -speed);
        moves[5].sMult(up, speed);

        for(int i = 0; i < movingDir.length; i++){
            if(movingDir[i]){
                vel.translate(moves[i]);
            }
        }
        loc.translate(vel);
        point.add(loc, dir);
        glu.gluLookAt(loc.x, loc.y, loc.z, point.x, point.y, point.z, up.x, up.y, up.z);
    }

    /*
    * Key Listener Methods
    * */
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.isAutoRepeat()){
            return;
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            window.destroy();
            System.exit(0);
        }
        for(int i = 0; i < dirKeys.length; i++){
            if(e.getKeyCode() == dirKeys[i]){
                movingDir[i] = true;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ALT ){
            if(window.getMouseListeners().length > 0) {
                window.removeMouseListener(this);
                window.setPointerVisible(true);
                window.confinePointer(false);
            }
            else {
                window.addMouseListener(this);
                window.setPointerVisible(false);
                window.confinePointer(true);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.isAutoRepeat()){
            return;
        }
        for(int i = 0; i < dirKeys.length; i++){
            if(e.getKeyCode() == dirKeys[i]){
                movingDir[i] = false;
            }
        }
    }

    /*
    * Mouse Listener Methods
    * */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        window.setPointerVisible(false);
        window.confinePointer(true);
        prevTime = System.currentTimeMillis();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        window.setPointerVisible(true);
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
        long currTime = System.currentTimeMillis();
        float dt = (float)(currTime - prevTime) / 1000f;
        window.warpPointer(width / 2, height / 2);
        float dH = mouseSpeed * dt * (width / 2 - mouseX);
        float dV = mouseSpeed * dt * (height / 2 - mouseY );
        hAngle -= dH;
        vAngle -= dV;
        prevTime = currTime;
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseWheelMoved(MouseEvent mouseEvent) {

    }

    /*
    * Window Listener Methods
    * */
    @Override
    public void windowResized(WindowEvent windowEvent) {
        width = window.getWidth();
        height = window.getHeight();
    }

    @Override
    public void windowMoved(WindowEvent windowEvent) {

    }

    @Override
    public void windowDestroyNotify(WindowEvent windowEvent) {
        anim.stop();
    }

    @Override
    public void windowDestroyed(WindowEvent windowEvent) {
        System.exit(0);
    }

    @Override
    public void windowGainedFocus(WindowEvent windowEvent) {

    }

    @Override
    public void windowLostFocus(WindowEvent windowEvent) {

    }

    @Override
    public void windowRepaint(WindowUpdateEvent windowUpdateEvent) {

    }
}
