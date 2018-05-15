import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;

public class HelloJOGL_template {
	private GLWindow window;
	private Animator anim;
	
	public static void main(String[] args){
		@SuppressWarnings("unused")
		HelloJOGL_template test = new HelloJOGL_template();
	}
	
	public HelloJOGL_template(){
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
		anim = new Animator(window);
		anim.start();
	}
}
