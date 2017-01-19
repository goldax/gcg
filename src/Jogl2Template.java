
/**
 * @author Sabrina Heppner / Stephan Arens / Andreas Elsner / Gitta Domik 
 * Jogl2Template (Version 09/2015)
 * Grundlagen der Computergrafik (WS 2014/2015) 
 * Department of Computer Science at the University of Paderborn, Germany
 * Research Group of Prof. Gitta Domik - Computer Graphics, Visualization and Digital Image Processing
 **/

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.IntBuffer;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.common.nio.Buffers;

public class Jogl2Template extends Frame implements GLEventListener,
		MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
{
	// rotation variables
	private float view_rotx = 0.0f, view_roty = 0.0f, view_rotz = 0.0f;

	// translation variables
	private float view_transx = 0.0f, view_transy = 0.0f, view_transz = -10.0f;

	// previous mouse x and y coordinates
	private int prevMouseX, prevMouseY;

	// create glu and glut objects
	private GLU glu = new GLU();

	private GLUT glut = new GLUT();

	private FPSAnimator animator;

	private boolean fullscreen = false;

	// true if event listeners are initialized (added to GLAutoDrawable, see
	// public void init(GLAutoDrawable drawable))
	private boolean listenersInitialized = false;

	private GLCanvas canvas;

	public static void main(String[] args)
	{
		Jogl2Template template = new Jogl2Template();
		template.setVisible(true);
	}

	public Jogl2Template()
	{
		setTitle("Jogl2 Template");
		
		canvas = new GLCanvas();
		
		// the DisplayListener is responsible for showing us the graphics
		canvas.addGLEventListener(this);
		animator = new FPSAnimator(canvas, 60);
		
		// set the Window size
		setSize(600, 400);
		
		// add the canvas to it
		add(canvas);
		// center on screen
		setLocationRelativeTo(null);

		// WindowListener that stops rendering and shuts down the application if
		// the window is closed
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				setVisible(false);
				animator.stop();
				dispose();
				System.exit(0);
			}
		});
	}

	public void setVisible(boolean b)
	{
		boolean visible = isVisible();
		super.setVisible(b);
		// if visible, start the display loop, calling the method "display" of the
		// DisplayListener again and again...
		if (visible != b)
			if (b)
			{
				getAnimator().start();
				// request focus for event handling
				canvas.requestFocus();
			}
			else
				getAnimator().stop();
	}

	/*
	 * The init() method is called immediately after the OpenGL context is
	 * initialized for the first time. This method is where we do our basic
	 * environment set up.
	 * 
	 * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
	 */
	public void init(GLAutoDrawable drawable)
	{
		// get a "gl" object. this object provides you with all OpenGL methods
		GL2 gl = drawable.getGL().getGL2();
		
		// who is the vendor of the graphics card?
		System.out.println("GL_VENDOR: " + gl.glGetString(GL2.GL_VENDOR));
		// which OpenGL renderer is used?
		System.out.println("GL_RENDERER: " + gl.glGetString(GL2.GL_RENDERER));
		// what kind of OpenGL version is currently supported?
		System.out.println("GL_VERSION: " + gl.glGetString(GL2.GL_VERSION));

		// fullscreen will call init on the same GLAutoDrawable again, so we must
		// avoid adding the listeners once again
		if (!listenersInitialized)
		{
			listenersInitialized = true;
			
			canvas.addMouseListener(this);
			canvas.addMouseMotionListener(this);
			canvas.addMouseWheelListener(this);
			canvas.addKeyListener(this);
		}

		gl.glEnable(GL2.GL_DEPTH_TEST);
	}

	/*
	 * Called after the frame is resized. This includes the first time the
	 * component appears here you should set up the viewport and perspective,
	 * fitting to the enclosing window.
	 * 
	 * @see javax.media.opengl.GLEventListener#reshape(javax.media.opengl.GLAutoDrawable,
	 *      int, int, int, int)
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height)
	{
		// get a gl object
		GL2 gl = drawable.getGL().getGL2();
		// set the OpenGL Viewport to the actual width and height of the window
		gl.glViewport(0, 0, width, height);
		// choose your type of projection(ortho or perspective)
		// choose the projection matrix-mode
		gl.glMatrixMode(GL2.GL_PROJECTION);
		// and load it with the identity matrix
		gl.glLoadIdentity();
		// perspective projection
		glu.gluPerspective(30, (float) width / height, 1, 100);
		// make sure to use the modelview matrix-mode
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		// load the identity matrix as the modelview (resets all previous
		// transformations)
		gl.glLoadIdentity();
	}

	/*
	 * Called if the display properties (resolution, etc.) are changed.
	 * 
	 * @see javax.media.opengl.GLEventListener#displayChanged(javax.media.opengl.GLAutoDrawable,
	 *      boolean, boolean)
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged)
	{
	}

	/*
	 * Called to do the rendering. The Animator calls this method repetitive to
	 * allow animation
	 * 
	 * @see javax.media.opengl.GLEventListener#display(javax.media.opengl.GLAutoDrawable)
	 */
	public void display(GLAutoDrawable drawable)
	{
		// get the gl object
		GL2 gl = drawable.getGL().getGL2();
		
		// clear screen with the defined erasing color and depth buffer
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		// load identity for modelview matrix
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		//apply mouse actions
		applyMouseTranslation(gl);
		applyMouseRotation(gl);
	}

	
	protected void applyMouseTranslation(GL2 gl)
	{
		IntBuffer buffer = Buffers.newDirectIntBuffer(1);
		gl.glGetIntegerv(GL2.GL_MATRIX_MODE, buffer);

		// make sure to use the modelview matrix-mode
		gl.glMatrixMode(GL2.GL_MODELVIEW);

		// apply the translation
		gl.glTranslatef(view_transx, view_transy, view_transz);
		gl.glMatrixMode(buffer.get(0));
	}

	protected void applyMouseRotation(GL2 gl)
	{
		IntBuffer buffer = Buffers.newDirectIntBuffer(1);
		gl.glGetIntegerv(GL2.GL_MATRIX_MODE, buffer);
		
		// make sure to use the modelview matrix-mode
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		
		// apply the rotation
		gl.glRotatef(view_rotx, 1f, 0f, 0f);
		gl.glRotatef(view_roty, 0f, 1f, 0f);
		gl.glRotatef(view_rotz, 0f, 0f, 1f);
		gl.glMatrixMode(buffer.get(0));
	}

	// Methods required for the implementation of MouseListener
	// --------------------------------------------------------
	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	/*
	 * This method is used to store the current mouse x and y coordinates.
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e)
	{
		prevMouseX = e.getX();
		prevMouseY = e.getY();
	}

	public void mouseReleased(MouseEvent e)
	{
	}

	// Methods required for the implementation of MouseMotionListener
	// --------------------------------------------------------------
	public void mouseMoved(MouseEvent e)
	{
	}

	/*
	 * This method handles mouse interactions (rotation, movement)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e)
	{
		// get current mouse x and y coordinate
		int x = e.getX();
		int y = e.getY();
		
		// get size of component
		Dimension size = e.getComponent().getSize();

		// left button is dragged (rotation)
		if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
		{
			// store thetaY movement for rotation around x-axis
			float thetaY = 360f * ((float) (x - prevMouseX) / (float) size.width);
			// store thetaX movement for rotation around y-axis
			float thetaX = 360f * ((float) (prevMouseY - y) / (float) size.height);
			// store current x and y coordinate for future calculations
			prevMouseX = x;
			prevMouseY = y;
			// change x and y rotation value
			view_rotx -= thetaX;
			view_roty += thetaY;
		}
		// right button is dragged (translation)
		if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
		{
			// thetaX movement for x translation
			float thetaX = 2f * ((float) (x - prevMouseX) / (float) size.width); // store
			// thetaY movement for y translation
			float thetaY = 2f * ((float) (prevMouseY - y) / (float) size.height); // store
			// store current x and y coordinate for future calculations
			prevMouseX = x;
			prevMouseY = y;
			// change x and y rotation value
			view_transx += thetaX;
			view_transy += thetaY;
		}
	}

	/*
	 * Handles mouse wheel interactions (camera distance 0, 0)
	 * 
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		view_transz += e.getWheelRotation() * 0.5f;
	}

	// Methods required for the implementation of KeyListener
	// ------------------------------------------------------
	public void keyReleased(KeyEvent e)
	{
	}

	public void keyTyped(KeyEvent e)
	{
	}

	/*
	 * Handles all key events
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e)
	{
		// reset all translations and rotations
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			view_rotx = 0f;
			view_roty = 0f;
			view_rotz = 0f;
			view_transx = 0f;
			view_transy = 0f;
			view_transz = -10f;
		}
		// "up arrow" for zooming out
		else if (e.getKeyCode() == KeyEvent.VK_UP)
		{
			view_transz -= 0.2f;
		}
		// "down arrow" for zooming in
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			view_transz += 0.2f;
		}
		else if (e.getKeyCode() == KeyEvent.VK_F11)
		{
			setFullscreen(!isFullscreen());
		}
	}

	public float getView_rotx()
	{
		return view_rotx;
	}

	public void setView_rotx(float view_rotx)
	{
		this.view_rotx = view_rotx;
	}

	public float getView_roty()
	{
		return view_roty;
	}

	public void setView_roty(float view_roty)
	{
		this.view_roty = view_roty;
	}

	public float getView_rotz()
	{
		return view_rotz;
	}

	public void setView_rotz(float view_rotz)
	{
		this.view_rotz = view_rotz;
	}

	public float getView_transx()
	{
		return view_transx;
	}

	public void setView_transx(float view_transx)
	{
		this.view_transx = view_transx;
	}

	public float getView_transy()
	{
		return view_transy;
	}

	public void setView_transy(float view_transy)
	{
		this.view_transy = view_transy;
	}

	public float getView_transz()
	{
		return view_transz;
	}

	public void setView_transz(float view_transz)
	{
		this.view_transz = view_transz;
	}

	public GLU getGlu()
	{
		return glu;
	}

	public void setGlu(GLU glu)
	{
		this.glu = glu;
	}

	public GLUT getGlut()
	{
		return glut;
	}

	public void setGlut(GLUT glut)
	{
		this.glut = glut;
	}

	public FPSAnimator getAnimator()
	{
		return animator;
	}

	public boolean isFullscreen()
	{
		return fullscreen;
	}

	public void setFullscreen(boolean fullscreen)
	{
		if (fullscreen == this.fullscreen)
			return;

		boolean visible = isVisible();
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();

		if (fullscreen)
		{
			this.fullscreen = true;
			setVisible(false);
			dispose();
			setUndecorated(true);
			setResizable(false);
			gd.setFullScreenWindow(this);
		}
		else
		{
			this.fullscreen = false;
			gd.setFullScreenWindow(null);
			setVisible(false);
			dispose();
			setUndecorated(false);
			setResizable(true);
			setSize(600, 400);
		}

		setVisible(visible);
	}

	static public void printMatrix(float[] matrix)
	{
		for (int i = 0; i < 4; i++)
		{
			System.out.format("%1$ 5.3f %2$ 5.3f %3$ 5.3f %4$ 5.3f", new Object[] {
					new Float(matrix[i]), new Float(matrix[i + 4]),
					new Float(matrix[i + 8]), new Float(matrix[i + 12]) });
			System.out.println();
		}

		System.out.println();
	}

	public void dispose(GLAutoDrawable arg0) {
		
	}

}