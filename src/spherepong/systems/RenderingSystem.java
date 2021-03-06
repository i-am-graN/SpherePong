package spherepong.systems;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;
import java.util.logging.Logger;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;

import spherepong.Camera;
import spherepong.SpherePong;
import spherepong.components.Position;
import spherepong.components.Renderable;
import spherepong.components.Spring;
import spherepong.exceptions.SystemExitException;

public class RenderingSystem extends EntitySystem {

    private static final Logger LOGGER = Logger.getLogger(RenderingSystem.class.getName());
    private static final boolean DEBUG = true;
    private long window;
    private Camera camera;
    private int width, height;
    private String windowTitle;

    public RenderingSystem(final int width, final int height, final String windowTitle) {
	super(Aspect.all(Renderable.class, Position.class));
	this.width = width;
	this.height = height;
	this.windowTitle = windowTitle;
    }

    @Override
    protected void initialize() {
	super.initialize();
	LOGGER.info("Initializing rendering system");

	// Setup an error callback. The default implementation will print the error message in System.err.
	GLFWErrorCallback.createPrint(System.err).set();

	// Initialize GLFW. Most GLFW functions will not work before doing this.
	if (!glfwInit())
	    throw new IllegalStateException("Unable to initialize GLFW");

	// Configure GLFW
	glfwDefaultWindowHints(); // optional, the current window hints are already the default
	glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
	glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

	// Create the window
	this.window = glfwCreateWindow(this.width, this.height, this.windowTitle, NULL, NULL);
	if (this.window == NULL)
	    throw new RuntimeException("Failed to create the GLFW window");

	// Setup a key callback. It will be called every time a key is pressed, repeated or released.
	glfwSetKeyCallback(this.window, (window, key, scancode, action, mods) -> {
	    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
		glfwSetWindowShouldClose(this.window, true); // We will detect this in the rendering loop
	});

	// Get the thread stack and push a new frame
	try (MemoryStack stack = stackPush()) {
	    IntBuffer pWidth = stack.mallocInt(1); // int*
	    IntBuffer pHeight = stack.mallocInt(1); // int*

	    // Get the window size passed to glfwCreateWindow
	    glfwGetWindowSize(this.window, pWidth, pHeight);

	    // Get the resolution of the primary monitor
	    GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

	    // Center the window
	    glfwSetWindowPos(this.window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
	} // the stack frame is popped automatically

	// Make the OpenGL context current
	glfwMakeContextCurrent(this.window);
	// Enable v-sync
	glfwSwapInterval(1);

	// Make the window visible
	glfwShowWindow(this.window);

	// This line is critical for LWJGL's interoperation with GLFW's OpenGL context, or any context that is managed
	// externally. LWJGL detects the context that is current in the current thread, creates the GLCapabilities
	// instance and makes the OpenGL bindings available for use.
	GL.createCapabilities();

	// Set the clear color
	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

	glEnable(GL_DEPTH_TEST);
	initCamera();
    }

    private void initCamera() {
	camera = new Camera(70, SpherePong.WINDOW_WIDTH / SpherePong.WINDOW_HEIGHT, 0.3f, 1000);
	camera.initialize();
	camera.setPosition(new Vector3f(SpherePong.WINDOW_WIDTH / 2, SpherePong.WINDOW_HEIGHT / 2, 800));
	camera.setRotation(new Vector3f(0, 0, 0));
	camera.useView();
    }

    @Override
    protected void processSystem() {
	// Poll for window events. The key callback above will only be invoked during this call.
	glfwPollEvents();

	if (glfwWindowShouldClose(this.window)) {
	    throw new SystemExitException();
	}

	// Clear the frame buffer
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	drawDebugLines(SpherePong.WINDOW_WIDTH, SpherePong.WINDOW_HEIGHT, SpherePong.WINDOW_WIDTH, 100);

	for (Entity e : this.getEntities()) {
//	    Spring spring = e.getComponent(Spring.class);
//	    if (spring != null) {
//		Position posA = spring.a.getComponent(Position.class);
//		Position posB = spring.b.getComponent(Position.class);
//		drawLine(posA, posB);
//	    } else {
		Position position = e.getComponent(Position.class);
		Renderable renderable = e.getComponent(Renderable.class);
		drawRectangle(position, renderable.width, renderable.height);
//	    }
	}

	// Swap the color buffers
	glfwSwapBuffers(this.window);
    }

    @Override
    protected void dispose() {
	super.dispose();
	LOGGER.info("Shutting down rendering system");
	// Free the window callbacks and destroy the window
	glfwFreeCallbacks(this.window);
	glfwDestroyWindow(this.window);

	// Terminate GLFW and free the error callback
	glfwTerminate();
	glfwSetErrorCallback(null).free();
    }

    private void drawRectangle(Position position, float width, float height) {
	glBegin(GL_QUADS);
	{
	    // Just testing adding a layer to see depth
	    float depth = -100;
	    glColor3f(0.0f, 0.0f, 0.8f);
	    glVertex3f(position.position.x, position.position.y, depth);
	    glVertex3f(position.position.x, position.position.y + height, depth);
	    glVertex3f(position.position.x + width, position.position.y + height, depth);
	    glVertex3f(position.position.x + width, position.position.y, depth);

	    glColor3f(0.0f, 0.0f, 1.0f);
	    glVertex3f(position.position.x, position.position.y, 0);
	    glVertex3f(position.position.x, position.position.y + height, 0);
	    glVertex3f(position.position.x + width, position.position.y + height, 0);
	    glVertex3f(position.position.x + width, position.position.y, 0);
	}
	glEnd();

	if (DEBUG) {
	    drawBoundingBox(position, width, height, 5);
	}
    }

    private void drawLine(Position positionA, Position positionB) {
	glLineWidth(1);
	glColor3f(0.0f, 1.0f, 0.0f);
	glBegin(GL_LINE);
	{
	    glVertex3f(positionA.position.x, positionA.position.y, positionA.position.z);
	    glVertex3f(positionB.position.x, positionB.position.y, positionB.position.z);
	}
	glEnd();
    }

    private void drawBoundingBox(Position position, float width, float height, float depth) {
	glLineWidth(1);
	glColor3f(0.0f, 1.0f, 0.0f);
	glBegin(GL_LINES);
	{
	    glVertex3f(position.position.x, position.position.y, depth);
	    glVertex3f(position.position.x + width, position.position.y, depth);

	    glVertex3f(position.position.x, position.position.y, depth);
	    glVertex3f(position.position.x, position.position.y + height, depth);

	    glVertex3f(position.position.x, position.position.y + height, depth);
	    glVertex3f(position.position.x + width, position.position.y + height, depth);

	    glVertex3f(position.position.x + width, position.position.y, depth);
	    glVertex3f(position.position.x + width, position.position.y + height, depth);
	}
	glEnd();
    }

    private void drawDebugLines(int width, int height, int depth, int spacing) {
	glLineWidth(1.0f);
	glColor3f(0.8f, 0.8f, 0.8f);
	glBegin(GL_LINES);
	{
	    for (int i = 0; i <= width; i += spacing) {
		for (int j = 0; j <= height; j += spacing) {
		    glVertex3f(0, i, j);
		    glVertex3f(width, i, j);

		    glVertex3f(i, 0, j);
		    glVertex3f(i, height, j);

		    glVertex3f(i, j, 0);
		    glVertex3f(i, j, depth);
		}
	    }
	}
	glEnd();
    }
}
