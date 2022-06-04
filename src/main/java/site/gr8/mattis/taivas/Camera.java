package site.gr8.mattis.taivas;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

	private Vector3f position;
	private float pitch;
	private float yaw;
	private float roll;

	private Matrix4f viewMatrix;

	public Camera() {
		this.viewMatrix = new Matrix4f();
	}

	public void moveRelativeForward(boolean forward) {
		float x = Math.sin(Math.toRadians(getYaw())) * 0.25f;
		float z = Math.cos(Math.toRadians(getYaw())) * 0.25f;
		if (forward) {
			this.position.z -= z;
			this.position.x += x;
		} else {
			this.position.z += z;
			this.position.x -= x;
		}
	}
	public void moveRelativeSide(boolean right) {
		float z = Math.sin(Math.toRadians(getYaw())) * 0.25f;
		float x = Math.cos(Math.toRadians(getYaw())) * 0.25f;
		if (right) {
			this.position.z += z;
			this.position.x += x;
		} else {
			this.position.z -= z;
			this.position.x -= x;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void addPosition(float x, float y, float z) {
		this.position.x += x;
		this.position.y += y;
		this.position.z += z;
	}

	public Vector3f getOrientation() {
		return new Vector3f(this.pitch, this.yaw, this.roll);
	}

	public void setOrientation(Vector3f orientation) {
		this.pitch 	= orientation.x;
		this.yaw 	= orientation.y;
		this.roll 	= orientation.z;

	}

	/**
	 * @param x side to side
	 * @param y up - down
	 * @param z roll
	 */
	public void addRotation(float x, float y, float z) {
		this.yaw += x;
		this.pitch += y;
		this.roll += z;
		this.pitch = Math.min(this.pitch, 90);
		this.pitch = Math.max(this.pitch, -90);
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public Matrix4f getViewMatrix() {
		return this.viewMatrix.identity().
				rotateX(Math.toRadians(this.pitch)).
				rotateY(Math.toRadians(this.yaw)).
				rotateZ(Math.toRadians(this.roll)).
				translate(-position.x, -position.y, -position.z);
	}
}
