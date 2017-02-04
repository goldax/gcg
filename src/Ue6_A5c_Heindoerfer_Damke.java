import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.function.Consumer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.VectorUtil;

import SheetShader.GLSLShaderObject;
import SheetShader.OBJLoader;

public class Ue6_A5c_Heindoerfer_Damke extends Jogl2Template {	
	private GLSLShaderObject shader;
	private float uniform_size = 1;
	private float[] uniform_color = new float[] { 0.769f, 0.173f, 0.176f, 1 };
	private int displayList;
	
	public static void main(String[] args) {
		Ue6_A5c_Heindoerfer_Damke template = new Ue6_A5c_Heindoerfer_Damke();
		template.setVisible(true);
	}

	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		
		GL2 gl = drawable.getGL().getGL2();
		
		setTitle("Bunny");

		shader = new GLSLShaderObject();
		shader.loadFragmentShader("cartoon.frag");
		shader.loadVertexShader("cartoon.vert");
		shader.init(gl);
		shader.addUniform(gl, "size", uniform_size);
		shader.addUniform(gl, "color", uniform_color);
		
		OBJLoader bunny = new OBJLoader("bunny", 5.0f, gl);
		displayList = bunny.getDisplayList();
	}
	
	public void drawCoordinateSystem(GL2 gl) {
		gl.glBegin(gl.GL_LINES);
		// X:
		gl.glColor3f(1, 0, 0);
		gl.glVertex3f(-10, 0, 0);
		gl.glVertex3f(10, 0, 0);
		// Y:
		gl.glColor3f(0, 1, 0);
		gl.glVertex3f(0, -10, 0);
		gl.glVertex3f(0, 10, 0);
		// Z:
		gl.glColor3f(0, 0, 1);
		gl.glVertex3f(0, 0, -10);
		gl.glVertex3f(0, 0, 10);
		gl.glEnd();
	}

	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		super.display(drawable);
		
		drawCoordinateSystem(gl);
		
		shader.activate(gl);
		gl.glCallList(displayList);
		shader.deactivate(gl);
	}
}