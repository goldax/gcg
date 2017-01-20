import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.function.Consumer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.VectorUtil;
import com.jogamp.opengl.util.gl2.GLUT;


public class Ue5_A4_Heindoerfer_Damke_spot extends Jogl2Template {	
	
	private boolean smooth = true;
	
	private GLUT glut = new GLUT();
	
	public static void main(String[] args) {
		Ue5_A4_Heindoerfer_Damke_spot template = new Ue5_A4_Heindoerfer_Damke_spot();
		template.setVisible(true);
	}

	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		setTitle("Initialen");
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
	
	public void addLights(GL2 gl) {
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		
		gl.glEnable(GL2.GL_LIGHT1);
		
		gl.glShadeModel(smooth ? GL2.GL_SMOOTH : GL2.GL_FLAT);
		
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, new float[] { 0, 0, 3, 1 }, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, new float[] { 0, 0, -1, 1 }, 0);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, 4);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, 2);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, new float[] { 0.4f, 0.4f, 0.2f }, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, new float[] { 1, 1, 0.4f }, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, new float[] { 1, 1, 0.6f }, 0);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_CONSTANT_ATTENUATION, 1);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_LINEAR_ATTENUATION, 0.05f);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_QUADRATIC_ATTENUATION, 0.025f);
	}

	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glPushMatrix();
		
		super.display(drawable);
		
		drawCoordinateSystem(gl);
		
		gl.glColor3f(1, 1, 1);
		
		glut.glutSolidSphere(1, 200, 100);
		
		gl.glPopMatrix();
		
		addLights(gl);
	}

	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		
		switch(e.getKeyCode()) {
		case KeyEvent.VK_S:
			smooth = !smooth;
			break;
		}
	}
}