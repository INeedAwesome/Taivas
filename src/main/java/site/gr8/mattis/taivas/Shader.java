package site.gr8.mattis.taivas;

import org.joml.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import site.gr8.mattis.taivas.utils.MyFileSystem;
import site.gr8.mattis.taivas.utils.Utils;

import java.nio.FloatBuffer;

public class Shader {

	private int programID;
	private int vertexID;
	private int fragmentID;

	public Shader() {
	}

	public void init() {
		this.vertexID = loadShader(GL20.GL_VERTEX_SHADER, Utils.loadStringFromFile(MyFileSystem.addFile("shaders/vertex.vs")));
		this.fragmentID = loadShader(GL20.GL_FRAGMENT_SHADER, Utils.loadStringFromFile(MyFileSystem.addFile("shaders/fragment.vs")));
		this.programID = GL20.glCreateProgram();
		GL20.glAttachShader(this.programID, this.vertexID);
		GL20.glAttachShader(this.programID, this.fragmentID);
		GL20.glLinkProgram(this.programID);
		if (GL20.glGetProgrami(this.programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			System.err.println(GL20.glGetShaderInfoLog(this.programID, 500));
			System.err.println("Could not link program, " + this.programID);
		}
		GL20.glValidateProgram(this.programID);
		if (GL20.glGetProgrami(this.programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
			System.err.println(GL20.glGetShaderInfoLog(this.programID, 500));
			System.err.println("Could not validate program, " + this.programID);
		}
	}

	public void reload() {
		cleanup();
		init();
	}

	public void bind() {
		GL20.glUseProgram(this.programID);
	}

	public void unbind() {
		GL20.glUseProgram(0);
	}

	public void cleanup() {
		unbind();
		GL20.glDetachShader(programID, this.vertexID);
		GL20.glDetachShader(programID, this.fragmentID);
		GL20.glDeleteShader(this.vertexID);
		GL20.glDeleteShader(this.fragmentID);
		GL20.glDeleteProgram(this.programID);
	}

	private int loadShader(int type, String shaderSource) {
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader, " + shaderID);
		}
		return shaderID;
	}

	public int getLocation(String name) {
		return GL20.glGetUniformLocation(this.programID, name);
	}

	public void setUniform(String name, int value) {
		GL20.glUniform1i(getLocation(name), value);
	}
	public void setUniform(String name, boolean value) {
		GL20.glUniform1i(getLocation(name), value ? 1 : 0 );
	}
	public void setUniform(String name, float value) {
		GL20.glUniform1f(getLocation(name), value);
	}
	public void setUniform(String name, Matrix3f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(9); // 3 * 3
			value.get(fb);
			GL20.glUniformMatrix3fv(getLocation(name), false, fb);
		}
	}
	public void setUniform(String name, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16); // 4 * 4
			value.get(fb);
			GL20.glUniformMatrix4fv(getLocation(name), false, fb);
		}
	}
	public void setUniform(String name, Vector2f value) {
		GL20.glUniform2f(getLocation(name), value.x, value.y);
	}
	public void setUniform(String name, Vector3f value) {
		GL20.glUniform3f(getLocation(name), value.x, value.y, value.z);
	}
	public void setUniform(String name, Vector4f value) {
		GL20.glUniform4f(getLocation(name), value.x, value.y, value.z, value.w);
	}
}
