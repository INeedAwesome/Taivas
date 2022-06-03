package site.gr8.mattis.taivas;

import org.joml.Math;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImageWrite;
import site.gr8.mattis.taivas.input.KeyboardInput;
import site.gr8.mattis.taivas.settings.Settings;
import site.gr8.mattis.taivas.utils.Constants;
import site.gr8.mattis.taivas.utils.MyFileSystem;
import site.gr8.mattis.taivas.window.Window;

import java.io.File;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

	private static Window window = null;
	private static Shader shader = null;
	private static Matrix4f projectionMatrix = null;

	static float[] positions = new float[] {
			-0.5f, 0.5f, -1f, 		// 0 ------ 3
			-0.5f, -0.5f, -1f,		// |        |
			0.5f, -0.5f, -1,		// |        |
			0.5f, 0.5f, -1f			// 1 ------ 2
	};
	static float[] generatePoss() {
		int multiplier = 1;
		float[] xyz = new float[(int) 1000]; // testing something with the vertices
		for (int i =0; i < 2; i++) {
			xyz[0 + i + i * 11]-=0.5f * multiplier; 		xyz[1 + i + i * 11]+=0.5f * multiplier; 		xyz[2 + i + i * 11]-=1f * multiplier;
			xyz[3 + i + i * 11]-=0.5f * multiplier; 		xyz[4 + i + i * 11]-=0.5f * multiplier; 		xyz[5 + i + i * 11]-=1f * multiplier;
			xyz[6 + i + i * 11]+=0.5f * multiplier; 		xyz[7 + i + i * 11]-=0.5f * multiplier; 		xyz[8 + i + i * 11]-=1f * multiplier;
			xyz[9 + i + i * 11]+=0.5f * multiplier; 		xyz[10 + i + i * 11]+=0.5f * multiplier; 		xyz[11 + i + i * 11]-=1f * multiplier;
			multiplier++;
		}
		return xyz;
	}
	static float[] colors = new float[] {
			0.5f, 0.5f, 1,
			0.5f, 0.5f, 0,
			0.5f, 0.5f, 0.7f,
			0.5f, -0.5f, 0,

			0.9f, 0.5f, 1,
			0.3f, 1.5f, 0,
			0.5f, 0.5f, 0.7f,
			0.2f, -0.5f, 0
	};
	static int[] indices = new int[] {
			0, 1, 3,
			3, 1, 2,
	};
	static int[] genIndices() {
		int[] ind = new int[100];
		for (int i =0; i < 4; i++) {
			ind[0 + i * 6] = 0 + i*4; 		ind[1 + i * 6] = 1 + i*4; 		ind[2 + i * 6] = 3 + i*4;
			ind[3 + i * 6] = 3 + i*4;		ind[4 + i * 6] = 1 + i*4; 		ind[5 + i * 6] = 2 + i*4;
		}
		return ind;
	}

	public static void main(String[] args) {
		MyFileSystem.setRoot(Constants.DEFAULT_RESOURCE_LOCATION);
		projectionMatrix = new Matrix4f();
		Settings.init();

		window = new Window(Constants.DEFAULT_TITLE);
		window.init();
		window.createWindow();

		positions = generatePoss();
		indices = genIndices();
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);

		int positionsArrayBuffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, positionsArrayBuffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positions, GL15.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		int colorsArrayBuffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorsArrayBuffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colors, GL15.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		int elementArrayBuffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, elementArrayBuffer);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

		shader = new Shader();
		shader.init();

		while (!window.windowShouldClose()) {
			input();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.1f, .2f, .3f, 1);
			shader.reload();
			shader.bind();
			projectionMatrix = new Matrix4f().identity().perspective(Math.toRadians(90), (float)(window.getWidth() / window.getHeight()), 0.01f, 1000f);
			shader.setUniform("u_projMat", projectionMatrix);

			GL30.glBindVertexArray(vao);
			GL30.glEnableVertexAttribArray(0);
			GL30.glEnableVertexAttribArray(1);
			GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length * 500, GL11.GL_UNSIGNED_INT, 0);
			GL30.glDisableVertexAttribArray(0);
			GL30.glDisableVertexAttribArray(1);
			shader.unbind();
			window.update();
		}

		shader.cleanup();
		window.dispose();
	}

	public static Window getWindow() {
		return window;
	}

	public static void input() {
		GLFW.glfwPollEvents();
		if (KeyboardInput.wasKeyPressed(GLFW.GLFW_KEY_F2))
			takeScreenshot();
		if (KeyboardInput.wasKeyPressed(GLFW.GLFW_KEY_F4))
			shader.reload();
		if (KeyboardInput.wasKeyPressed(GLFW.GLFW_KEY_F5))
			GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 1);
		if (KeyboardInput.wasKeyPressed(GLFW.GLFW_KEY_F6))
			GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 8);
		if (KeyboardInput.wasKeyPressed(GLFW.GLFW_KEY_F11))
			window.switchFullscreen();
		if (KeyboardInput.wasKeyPressed(GLFW.GLFW_KEY_ESCAPE))
			window.requestWindowShouldClose();
	}

	public static void takeScreenshot() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH-mm-ss.S");
		LocalDateTime time = LocalDateTime.now(); String name = dtf.format(time);
		File save = MyFileSystem.addFile("screenshots/", name + ".jpg"); // creates the file

		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(window.getWidth() * window.getHeight() * 3); // allocates a buffer to store pixels in
		GL11.glReadPixels(0,0, window.getWidth(), window.getHeight(), GL11.GL_RGB,GL11.GL_UNSIGNED_BYTE,byteBuffer); // fills the buffer
		STBImageWrite.stbi_flip_vertically_on_write(true); // flip the image
		STBImageWrite.stbi_write_jpg(save.getPath(), window.getWidth(), window.getHeight(), 3, byteBuffer, window.getWidth()); // writes the png
		System.out.println("Took screenshot and saved it to '" + save.getPath() + "'.");

	}
}
