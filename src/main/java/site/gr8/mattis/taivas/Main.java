package site.gr8.mattis.taivas;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImageWrite;
import site.gr8.mattis.taivas.input.KeyboardInput;
import site.gr8.mattis.taivas.input.MouseInput;
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
	private static Camera camera = null;

	static float[] positions = new float[] {
			-0.5f, 0.5f, -1f, 		// 0 ------ 3
			-0.5f, -0.5f, -1f,		// |        |
			0.5f, -0.5f, -1,		// |        |
			0.5f, 0.5f, -1f			// 1 ------ 2
	};
	static float[] generatePoss() {
		float[] xyz = new float[(int) 16*16*16*24]; // testing something with the vertices
		int num = 0;
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				for (int z = 0; z < 5; z++) {
					int i = x+y+z;
					System.out.println("i:"+i + " x:"+x + " y:"+y+" z:"+z + "		num:"+ num);
					xyz[0 + num + num * 23] = x - 0.5f; 		xyz[1 + num + num * 23] = y + 0.5f; 		xyz[2 + num + num * 23] = z + 0f;
					xyz[3 + num + num * 23] = x - 0.5f; 		xyz[4 + num + num * 23] = y - 0.5f; 		xyz[5 + num + num * 23] = z + 0f;
					xyz[6 + num + num * 23] = x + 0.5f;			xyz[7 + num + num * 23] = y - 0.5f; 		xyz[8 + num + num * 23] = z + 0f;
					xyz[9 + num + num * 23] = x + 0.5f;			xyz[10 + num + num * 23] = y + 0.5f; 		xyz[11 + num + num * 23] = z + 0f;

					xyz[12 + num + num * 23] = x - 0.5f;		xyz[13 + num + num * 23] = y + 0.5f; 		xyz[14 + num + num * 23] = z - 1f;
					xyz[15 + num + num * 23] = x - 0.5f; 		xyz[16 + num + num * 23] = y - 0.5f; 		xyz[17 + num + num * 23] = z - 1f;
					xyz[18 + num + num * 23] = x + 0.5f;		xyz[19 + num + num * 23] = y - 0.5f; 		xyz[20 + num + num * 23] = z - 1f;
					xyz[21 + num + num * 23] = x + 0.5f;		xyz[22 + num + num * 23] = y + 0.5f;		xyz[23 + num + num * 23] = z - 1f;
					num++;
				}
			}
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
		int[] ind = new int[100000];
		for (int i =0; i < 300; i++) {
			ind[0 + i * 36] = 0 + i*24; 	ind[1 + i * 36] = 1 + i*24; 		ind[2 + i * 36] = 2 + i*24; // front
			ind[3 + i * 36] = 2 + i*24;		ind[4 + i * 36] = 3 + i*24; 		ind[5 + i * 36] = 0 + i*24;

			ind[6 + i * 36] = 4 + i*24; 	ind[7 + i * 36] = 5 + i*24; 		ind[8 + i * 36] = 6 + i*24; // back
			ind[9 + i * 36] = 6 + i*24;		ind[10 + i * 36] = 7 + i*24; 		ind[11 + i * 36] = 4 + i*24;

			ind[12 + i * 36] = 4 + i*24; 	ind[13 + i * 36] = 5 + i*24; 		ind[14 + i * 36] = 1 + i*24; // right
			ind[15 + i * 36] = 1 + i*24;	ind[16 + i * 36] = 0 + i*24; 		ind[17 + i * 36] = 4 + i*24;

			ind[18 + i * 36] = 3 + i*24; 	ind[19 + i * 36] = 2 + i*24; 		ind[20 + i * 36] = 6 + i*24; // left
			ind[21 + i * 36] = 6 + i*24;	ind[22 + i * 36] = 7 + i*24; 		ind[23 + i * 36] = 3 + i*24;

			ind[24 + i * 36] = 1 + i*24;	ind[25 + i * 36] = 5 + i*24; 		ind[26 + i * 36] = 6 + i*24; // bottom
			ind[27 + i * 36] = 6 + i*24;	ind[28 + i * 36] = 2 + i*24; 		ind[29 + i * 36] = 1 + i*24;

			ind[30 + i * 36] = 4 + i*24;	ind[31 + i * 36] = 0 + i*24; 		ind[32 + i * 36] = 3 + i*24; // top
			ind[33 + i * 36] = 3 + i*24;	ind[34 + i * 36] = 7 + i*24; 		ind[35 + i * 36] = 4 + i*24;
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
		camera = new Camera();
		camera.setPosition(new Vector3f(0, 0, 1));
		camera.setOrientation(new Vector3f(0, 0, 0));

		double lastTime = System.nanoTime();
		float deltaTime;
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		while (!window.windowShouldClose()) {
			double nowTime = System.nanoTime();
			deltaTime = (float)(nowTime - lastTime) / 1_000_000_000;
			lastTime = nowTime;
			input();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.1f, .2f, .3f, 1);
			shader.reload();
			shader.bind();
			projectionMatrix = new Matrix4f().identity().perspective(Math.toRadians(90), (float)window.getWidth() / (float)window.getHeight(), 0.1f, 100f);
			shader.setUniform("u_projMat", projectionMatrix);
			shader.setUniform("u_viewMat", camera.getViewMatrix());
			shader.setUniform("u_time", deltaTime);

			GL30.glBindVertexArray(vao);
			GL30.glEnableVertexAttribArray(0);
			GL30.glEnableVertexAttribArray(1);
			GL11.glDrawElements(GL11.GL_TRIANGLES, 5000, GL11.GL_UNSIGNED_INT, 0);
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
		camera.addRotation(MouseInput.getMouseDX(), MouseInput.getMouseDY(), 0);
		if (KeyboardInput.isKeyDown(GLFW.GLFW_KEY_W))
			camera.moveRelativeForward(true);
		if (KeyboardInput.isKeyDown(GLFW.GLFW_KEY_S))
			camera.moveRelativeForward(false);
		if (KeyboardInput.isKeyDown(GLFW.GLFW_KEY_A))
			camera.moveRelativeSide(false);
		if (KeyboardInput.isKeyDown(GLFW.GLFW_KEY_D))
			camera.moveRelativeSide(true);
		if (KeyboardInput.isKeyDown(GLFW.GLFW_KEY_Q))
			camera.addPosition(0, -0.1f, 0);
		if (KeyboardInput.isKeyDown(GLFW.GLFW_KEY_E))
			camera.addPosition(0, 0.1f, 0);
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
