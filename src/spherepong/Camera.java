package spherepong;

import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.*;

public class Camera {

    private Vector3f position;
    private Vector3f rotation;
    private float fov;
    private float aspect;
    private float near;
    private float far;

    public Camera(float fov, float aspect, float near, float far) {
	this(fov, aspect, near, far, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
    }

    public Camera(float fov, float aspect, float near, float far, Vector3f position, Vector3f rotation) {
	this.fov = fov;
	this.aspect = aspect;
	this.near = near;
	this.far = far;
	this.position = position;
	this.rotation = rotation;
    }

    public void useView() {
	// Rotate
	glRotatef(rotation.x, 1, 0, 0);
	glRotatef(rotation.y, 0, 1, 0);
	glRotatef(rotation.z, 0, 0, 1);
	
	// Set position
	glTranslatef(position.x, position.y, position.z);
    }

    public void initialize() {
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(fov, aspect, near, far);
	glMatrixMode(GL_MODELVIEW);
    }

    public void setPosition(Vector3f position) {
	this.position = position;
    }

    public void setRotation(Vector3f rotation) {
	this.rotation = rotation;
    }

    /**
     * Was replaced in LWJGL3.
     */
    private void gluPerspective(float fov, float aspect, float zNear, float zFar) {
	float fH = (float) Math.tan(fov / 360 * Math.PI) * zNear;
	float fW = fH * aspect;
	glFrustum(-fW, fW, -fH, fH, zNear, zFar);
    }
}