import com.jogamp.newt.Window;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.math.VectorUtil;
import jogamp.newt.WindowImpl;
import jogamp.newt.driver.windows.WindowDriver;

import java.util.HashMap;

public class GLCamera implements MouseListener{
    private Matrix4 view, projection;
    private float[] loc, front, up;
    private HashMap<Short, Boolean> keysDown;
    private boolean mouseEntered, mouseTrapped, canFly;
    private float pitch, yaw, sens, fov, spd;
    private long prevFrameTime;

    public GLCamera(float[] loc, float[] front, float[] up)
            throws InstantiationError{
        if(loc.length != 3 || front.length != 3 || up.length != 3){
            throw new InstantiationError("Invalid camera vector(s)!");
        }
        this.loc = loc.clone();
        this.front = front.clone();
        this.up = up.clone();
        keysDown = new HashMap<>();
        mouseEntered = false;
        mouseTrapped = true;
        pitch = 0.0f;
        yaw = 270.0f;
        sens = 0.05f;
        fov = 45.0f;
        spd = 2.5f;
        canFly = false;
        view = new Matrix4();
        projection = new Matrix4();
        prevFrameTime = System.currentTimeMillis();
        calculateView();
    }

    public void keyHeld(KeyEvent key){
        keysDown.put(key.getKeyCode(), true);
        if(key.getKeyCode() == KeyEvent.VK_ALT) {
            mouseTrapped = !mouseTrapped;
            Window window = ((WindowImpl)key.getSource())
                    .getDelegatedWindow();
            window.confinePointer(mouseTrapped);
            window.setPointerVisible(!mouseTrapped);
        }
    }

    public float[] getLoc(){
        return loc;
    }

    public void keyReleased(short key){
        keysDown.put(key, false);
    }

    public void setYaw(float y){
        yaw = y;
    }

    public void setPitch(float p){
        pitch = p;
    }

    public void setSens(float s){
        sens = s;
    }

    public void setSpeed(float s){
        spd = s;
    }

    public void canFly(boolean c){
        canFly = c;
    }

    public void update(){
        move();
        calculateView();
    }

    private void move(){
        long currFrameTime = System.currentTimeMillis();
        float deltaTime = (currFrameTime - prevFrameTime) / 1000.0f;
        prevFrameTime = currFrameTime;
        float cameraSpeed = spd * deltaTime;
        float[] sclFront = new float[3];
        float[] right = new float[3];
        float[] sclUp = new float[3];
        VectorUtil.crossVec3(right, front, up);
        if(canFly)
            VectorUtil.scaleVec3(sclFront, front, cameraSpeed);
        else {
            VectorUtil.crossVec3(sclFront, up, right);
            VectorUtil.normalizeVec3(sclFront, sclFront);
            VectorUtil.scaleVec3(sclFront, sclFront, cameraSpeed);
        }
        VectorUtil.normalizeVec3(right, right);
        VectorUtil.scaleVec3(right, right, cameraSpeed);
        VectorUtil.scaleVec3(sclUp, up, cameraSpeed);

        if(keysDown.containsKey(KeyEvent.VK_W) && keysDown.get(KeyEvent.VK_W)){
            VectorUtil.addVec3(loc, loc, sclFront);
        }
        if(keysDown.containsKey(KeyEvent.VK_S) && keysDown.get(KeyEvent.VK_S)){
            VectorUtil.subVec3(loc, loc, sclFront);
        }
        if(keysDown.containsKey(KeyEvent.VK_A) && keysDown.get(KeyEvent.VK_A)){
            VectorUtil.subVec3(loc, loc, right);
        }
        if(keysDown.containsKey(KeyEvent.VK_D) && keysDown.get(KeyEvent.VK_D)){
            VectorUtil.addVec3(loc, loc, right);
        }
        if(keysDown.containsKey(KeyEvent.VK_CONTROL) && keysDown.get(KeyEvent.VK_CONTROL)){
            VectorUtil.subVec3(loc, loc, sclUp);
        }
        if(keysDown.containsKey(KeyEvent.VK_SPACE) && keysDown.get(KeyEvent.VK_SPACE)){
            VectorUtil.addVec3(loc, loc, sclUp);
        }
    }

    private void calculateView(){
        float[] f, s, u;
        f = VectorUtil.normalizeVec3(front);
        s = new float[3];
        u = new float[3];

        VectorUtil.crossVec3(s, f, up);
        s = VectorUtil.normalizeVec3(s);
        VectorUtil.crossVec3(u, s, f);
        u = VectorUtil.normalizeVec3(u);

        view.loadIdentity();
        view.getMatrix()[0] = s[0];
        view.getMatrix()[4] = s[1];
        view.getMatrix()[8] = s[2];

        view.getMatrix()[1] = u[0];
        view.getMatrix()[5] = u[1];
        view.getMatrix()[9] = u[2];

        view.getMatrix()[2] = -f[0];
        view.getMatrix()[6] = -f[1];
        view.getMatrix()[10] = -f[2];

        view.getMatrix()[3] = 0.0f;
        view.getMatrix()[7] = 0.0f;
        view.getMatrix()[11] = 0.0f;

        view.getMatrix()[12] = -VectorUtil.dotVec3(s, loc);
        view.getMatrix()[13] = -VectorUtil.dotVec3(u, loc);
        view.getMatrix()[14] = VectorUtil.dotVec3(f, loc);
        view.getMatrix()[15] = 1.0f;
    }

    public Matrix4 getView() {
        return view;
    }

    public Matrix4 getProjection() {
        return projection;
    }

    public void reshape(int x, int y, int width, int height){
        projection.loadIdentity();
        projection.makePerspective((float)Math.toRadians(fov),
                (float)width / (float)height,
                0.1f, 100.0f);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        WindowImpl src = (WindowImpl)e.getSource();
        Window window = src.getDelegatedWindow();
        if(mouseTrapped) {
            window.warpPointer(window.getWidth() / 2, window.getHeight() / 2);
        } else{
            return;
        }
        if(!mouseEntered){
            mouseEntered = true;
            return;
        }

        pitch += sens * (window.getHeight() / 2 - e.getY());
        yaw += sens * (e.getX() - window.getWidth() / 2);
        if(pitch > 89.0f)
            pitch = 89.0f;
        if(pitch < -89.0f)
            pitch = -89.0f;
        if(yaw < 0.0f)
            yaw += 360.0f;
        if(yaw > 360.0f)
            yaw -= 360.0f;
        float[] front = new float[3];
        front[0] = (float)(Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw)));
        front[1] = (float)Math.sin(Math.toRadians(pitch));
        front[2] = (float)(Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)));

        this.front = VectorUtil.normalizeVec3(front);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
        if(!mouseTrapped)
            return;
        WindowImpl src = (WindowImpl)e.getSource();
        Window window = src.getDelegatedWindow();
        float[] rot = e.getRotation();
        fov -= rot[1];
        if(fov < 1.0f)
            fov = 1.0f;
        if(fov > 90.0f)
            fov = 90.0f;
        projection.loadIdentity();
        projection.makePerspective((float)Math.toRadians(fov),
                (float) window.getWidth() / (float) window.getHeight(),
                0.1f, 100.0f);
    }
}
