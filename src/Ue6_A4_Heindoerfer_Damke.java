import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.VectorUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;


public class Ue6_A4_Heindoerfer_Damke extends Jogl2Template {	
	public class Letter {
		public float[][] verts;
		public int[][] quads;
		public float[][] normals;
		public float[][] colors;
		public float[][][] texture;
		
		public Letter(float[][] verts, int[][] quads, BiFunction<float[], Integer, float[]> textureMapper) {
			this.verts = deepen(verts);
			this.quads = cleanUp(quads);
			this.normals = calculateNormals();
			this.colors = calculateColors();
			this.texture = calculateTexture(textureMapper);
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
		
		private float[][] calculateColors() {
			float[][] colors = new float[quads.length][3];
			
			for(int i = 0; i< quads.length; i++) {
				float[] normal = normals[i];
				
				colors[i] = new float[] {
					(1 + normal[0]) / 2,
					(1 + normal[1]) / 2,
					(1 + normal[2]) / 2
				};
			}
			
			return colors;
		}
		
		private float[][][] calculateTexture(BiFunction<float[], Integer, float[]> textureMapper) {
			float[][][] texture = new float[quads.length][4][2];
			
			for(int i = 0; i < quads.length; i++) {
				int[] quad = quads[i];
				
				for(int j = 0; j < 4; j++)
					texture[i][j] = textureMapper.apply(verts[quad[j]], j);
			}
			
			return texture;
		}
		
		public void draw(GL2 gl) {
			drawVertexIndexList(gl, verts, quads, normals, colors, texture);
		}
	}
	
	private int shownLetter = 0;
	private char[] letterOrder = new char[] {'j', 'h', 'c', 'd'};
	private HashMap<Character, Letter> letters = new HashMap<>();
	private Texture texture;
	
	BiFunction<float[], Integer, float[]> mapperA = (vert, i) -> {
		switch(i) {
		case 0: return new float[] { 0, 0 };
		case 1: return new float[] { 1, 0 };
		case 2: return new float[] { 1, 1 };
		case 3: return new float[] { 0, 1 };
		}
		
		return null;
	};

	BiFunction<float[], Integer, float[]> mapperB = (vert, i) -> {
		return new float[] { (vert[0] + 5) / 10, (vert[1] + 5) / 10 };
	};
	
	public static void main(String[] args) {
		Ue6_A4_Heindoerfer_Damke template = new Ue6_A4_Heindoerfer_Damke();
		template.setVisible(true);
	}
	
	public static void drawVertexIndexList(GL2 gl, float[][] verts, int[][] quads, float[][] normals, float[][] colors, float[][][] texture) {
		gl.glBegin(GL2.GL_QUADS);
		
		for(int i = 0; i < quads.length; i++) {
			int[] quad = quads[i];
			float[] normal = normals[i];
			float[] color = colors[i];
			float[][] quadTexture = texture[i];
			
			for(int j = 0; j < 4; j++) {
				float[] v = verts[quad[j]];
				float[] vTex = quadTexture[j];
				
				gl.glNormal3f(normal[0], normal[1], normal[2]);
				gl.glTexCoord2f(vTex[0], vTex[1]);
				gl.glColor3f(color[0], color[1], color[2]);
				gl.glVertex3f(v[0], v[1], v[2]);
			}
		}
		
		gl.glEnd();
	}

	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		setTitle("Initialen");
		
		try {
			GL2 gl = drawable.getGL().getGL2();
			
			texture = TextureIO.newTexture(new File("texture.png"), true);
			
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
			texture.bind(gl);
		} catch (Exception e) {
			System.err.println("Loading the texture failed: " + e.getMessage());
		}
		
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
		}, mapperA));
		
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
		}, mapperB));
		
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
		}, mapperA));
		
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
		}, mapperB));
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
		
		super.display(drawable);
		
		gl.glDisable(GL2.GL_TEXTURE_2D);
		
		drawCoordinateSystem(gl);
		
		gl.glEnable(GL2.GL_TEXTURE_2D);
		
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		transform(gl,
				translate(-6.5f, 0, 0),
				letters.get(letterOrder[shownLetter])::draw);
		
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
		transform(gl,
				translate(6.5f, 0, 0),
				letters.get(letterOrder[shownLetter + 1])::draw);
	}

	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		
		switch(e.getKeyCode()) {
		case KeyEvent.VK_I:
			shownLetter = (shownLetter + 2) % letterOrder.length;
			break;
		}
	}
}