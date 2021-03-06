import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.VectorUtil;


public class Ue5_A5_Heindoerfer_Damke extends Jogl2Template {	
	public class Letter {
		public float[][] verts;
		public int[][] quads;
		public float[][] normals;
		public float[] material;
		
		public Letter(float[][] verts, int[][] quads, float[] material) {
			this.verts = deepen(verts);
			this.quads = cleanUp(quads);
			this.normals = calculateNormals();
			this.material = material;
		}
		
		private float[][] deepen(float[][] f) {
			float[][] result = new float[f.length * 2][3];
			
			for(int i = 0 ; i < f.length; i++) {
				float[] p2d = f[i];
				result[i * 2] = new float[] {p2d[0], p2d[1], 1f};
				result[i * 2 + 1] = new float[] {p2d[0], p2d[1], -1f};
			}
			
			return result;
		}
		
		private int[][] cleanUp(int[][] a) {
			for(int i = 0; i < a.length; i++)
				for(int j = 0; j < a[i].length; j++)
					a[i][j]--;
			
			return a;
		}
		
		private float[][] calculateNormals() {
			float[][] normals = new float[quads.length][3];
			
			float[] tmp1 = new float[3];
			float[] tmp2 = new float[3];
			
			for(int i = 0; i < quads.length; i++) {
				int[] quad = quads[i];

				VectorUtil.getNormalVec3(normals[i], verts[quad[0]], verts[quad[1]], verts[quad[2]], tmp1, tmp2);
			}
			
			return normals;
		}
		
		public void draw(GL2 gl) {
			drawVertexIndexList(gl, verts, quads, normals, material);
		}
	}
	
	private int shownLetter = 0;
	private int shownLight = 0;
	private boolean smooth = true;
	private char[] letterOrder = new char[] {'j', 'h', 'c', 'd'};
	private HashMap<Character, Letter> letters = new HashMap<>();
	
	float[] BRASS_MATERIAL = {
		0.33f, 0.22f, 0.03f, 1.0f,
		0.78f, 0.57f, 0.11f, 1.0f,
		0.99f, 0.91f, 0.81f, 1.0f,
		5.0f
	};
	
	float[] REDPLASTIC_MATERIAL = {
		0.3f, 0.0f, 0.0f, 1.0f,
		0.6f, 0.0f, 0.0f, 1.0f,
		0.8f, 0.4f, 0.4f, 1.0f,
		10.0f
	};
	
	public static void main(String[] args) {
		Ue5_A5_Heindoerfer_Damke template = new Ue5_A5_Heindoerfer_Damke();
		template.setVisible(true);
	}
	
	public static void drawVertexIndexList(GL2 gl, float[][] verts, int[][] quads, float[][] normals, float[] material) {
		gl.glBegin(GL2.GL_QUADS);
		
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, Arrays.copyOfRange(material, 0, 3), 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, Arrays.copyOfRange(material, 4, 7), 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, Arrays.copyOfRange(material, 8, 11), 0);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, material[12]);
		
		for(int i = 0; i < quads.length; i++) {
			int[] quad = quads[i];
			float[] normal = normals[i];
			
			for(int j = 0; j < 4; j++) {
				float[] v = verts[quad[j]];
				
				gl.glNormal3f(normal[0], normal[1], normal[2]);
				gl.glVertex3f(v[0], v[1], v[2]);
			}
		}
		
		gl.glEnd();
	}

	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		setTitle("Initialen");
		
		letters.put('j', new Letter(new float[][] {
			{-1f, 5f},
			{-1f, 2f},
			{5f, 5f},
			{2f, 2f},
			{5f, 0f},
			{2f, 0f},
			{3.54f, -3.54f},
			{1.4f, -1.4f},
			{0f, -5f},
			{0f, -2f},
			{-1f, -5f},
			{-1f, -2f},
		}, new int[][] {
			{1,5,6,2},
			{5,9,10,6},
			{9,13,14,10},
			{13,17,18,14},
			{21,22,18,17},
			{23,24,22,21},
			{23,19,20,24},
			{19,15,16,20},
			{11,12,16,15},
			{7,8,12,11},
			{3,4,8,7},
			{1,2,4,3},
			{1,3,7,5},
			{5,7,11,9},
			{9,11,15,13},
			{13,15,19,17},
			{17,19,23,21},
			{2,6,8,4},
			{8,6,10,12},
			{12,10,14,16},
			{16,14,18,20},
			{22,24,20,18}
		}, BRASS_MATERIAL));
		
		letters.put('h', new Letter(new float[][] {
			{-5f,5f},
			{-2f,5f},
			{-2f,1.5f},
			{2f,1.5f},
			{2f,5f},
			{5f,5f},
			{5f,-5f},
			{2f,-5f},
			{2f,-1.5f},
			{-2f,-1.5f},
			{-2f,-5f},
			{-5f,-5f}
		}, new int[][] {
			{2,1,3,4},
			{4,3,5,6},
			{6,5,7,8},
			{8,7,9,10},
			{10,9,11,12},
			{12,11, 13,14},
			{14,13,15,16},
			{16,15,17,18},
			{18,17,19,20},
			{20,19,21,22},
			{22,21,23,24},
			{24,23,1,2},
			{1,23,21,3},
			{5,19,17,7},
			{9,15,13,11},
			{2,4,22,24},
			{10,12,14,16},
			{6,8,18,20}
		}, REDPLASTIC_MATERIAL));
		
		letters.put('c', new Letter(new float[][] {
			{3.54f,3.54f},
			{1.4f,1.4f},
			{0f,2f},
			{-1.4f,1.4f},
			{-2f,0f},
			{-1.4f,-1.4f},
			{0f,-2f},
			{1.4f,-1.4f},
			{3.54f,-3.54f},
			{0f,-5f},
			{-3.54f,-3.54f},
			{-5f,0f},
			{-3.54f,3.54f},
			{0f,5f}
		}, new int[][] {
			{2,1,3,4},
			{4,3,5,6},
			{6,5,7,8},
			{8,7,9,10},
			{10,9,11,12},
			{12,11,13,14},
			{14,13,15,16},
			{16,15,17,18},
			{18,17,19,20},
			{20,19,21,22},
			{22,21,23,24},
			{24,23,25,26},
			{26,25,27,28},
			{28,27,1,2},
			{1,27,5,3},
			{27,25,7,5},
			{25,23,9,7},
			{23,21,11,9},
			{21,19,13,11},
			{19,17,15,13},
			{4,6,28,2},
			{6,8,26,28},
			{8,10,24,26},
			{10,12,22,24},
			{12,14,20,22},
			{14,16,18,20}
		}, BRASS_MATERIAL));
		
		letters.put('d', new Letter(new float[][] {
			{-4f,5f},
			{0f,5f},
			{3.54f,3.54f},
			{5f,0f},
			{3.54f,-3.54f},
			{0f,-5f},
			{-4f,-5f},
			{-1f,2f},
			{0f,2f},
			{1.4f,1.4f},
			{2f,0f},
			{1.4f,-1.4f},
			{0f,-2f},
			{-1f,-2f}
		}, new int[][] {
			{2,1,3,4},
			{4,3,5,6},
			{6,5,7,8},
			{8,7,9,10},
			{10,9,11,12},
			{12,11,13,14},
			{14,13,1,2},
			{18,17,15,16},
			{20,19,17,18},
			{22,21,19,20},
			{24,23,21,22},
			{26,25,23,24},
			{28,27,25,26},
			{16,15,27,28},
			{1,15,17,3},
			{3,17,19,5},
			{5,19,21,7},
			{7,21,23,9},
			{9,23,25,11},
			{11,25,27,13},
			{15,1,13,27},
			{2,4,18,16},
			{4,6,20,18},
			{6,8,22,20},
			{8,10,24,22},
			{10,12,26,24},
			{12,14,28,26},
			{14,2,16,28}
		}, REDPLASTIC_MATERIAL));
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
		
		gl.glDisable(GL2.GL_LIGHT0);
		gl.glDisable(GL2.GL_LIGHT1);
		gl.glDisable(GL2.GL_LIGHT2);
		
		gl.glEnable(
				shownLight == 0
				? GL2.GL_LIGHT0
				: shownLight == 1
				? GL2.GL_LIGHT1
				: GL2.GL_LIGHT2);
		
		gl.glShadeModel(smooth ? GL2.GL_SMOOTH : GL2.GL_FLAT);
		
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[] { 0, 1, 1, 0 }, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[] { 0.3f, 0.3f, 0.3f }, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[] { 1, 1, 1 }, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[] { 1, 1, 1 }, 0);

		
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, new float[] { 0, 0, 3, 1 }, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, new float[] { 0, 0, -1, 1 }, 0);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, 4);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, 2);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, new float[] { 0.4f, 0.4f, 0.2f }, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, new float[] { 1, 1, 0.4f }, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, new float[] { 1, 1, 0.6f }, 0);
		
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, new float[] { 5, 5, 5, 1 }, 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_AMBIENT, new float[] { 0.2f, 0, 0 }, 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, new float[] { 0, 1, 0 }, 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, new float[] { 0, 0, 1 }, 0);
	}
	
	@SuppressWarnings("unchecked")
	public void transform(GL2 gl, Consumer<GL2> ...f) {
		gl.glPushMatrix();
		try {
			Consumer<GL2> res = x -> {};
			for(Consumer<GL2> c: f)
				res = res.andThen(c);
			
			res.accept(gl);
		} catch(Exception e) {}
		gl.glPopMatrix();
	}
	
	public Consumer<GL2> translate(float x, float y, float z) {
		return gl -> gl.glTranslatef(x, y, z);
	}

	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glPushMatrix();
		
		super.display(drawable);
		
		drawCoordinateSystem(gl);

		transform(gl,
				translate(-6.5f, 0, 0),
				letters.get(letterOrder[shownLetter])::draw);
		transform(gl,
				translate(6.5f, 0, 0),
				letters.get(letterOrder[shownLetter + 1])::draw);
		
		gl.glPopMatrix();
		
		addLights(gl);
	}

	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		
		switch(e.getKeyCode()) {
		case KeyEvent.VK_I:
			shownLetter = (shownLetter + 2) % letterOrder.length;
			break;
		case KeyEvent.VK_1:
			shownLight = 0;
			break;
		case KeyEvent.VK_2:
			shownLight = 1;
			break;
		case KeyEvent.VK_3:
			shownLight = 2;
			break;
		case KeyEvent.VK_S:
			smooth = !smooth;
			break;
		}
	}
}