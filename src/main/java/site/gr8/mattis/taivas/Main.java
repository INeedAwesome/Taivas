package site.gr8.mattis.taivas;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import site.gr8.mattis.taivas.input.KeyboardInput;
import site.gr8.mattis.taivas.window.Window;

public class Main {

	private static Window window = null;

	public static void main(String[] args) {
		window = new Window(Constants.DEFAULT_TITLE);
		window.init();
		window.createWindow();
		while (!window.windowShouldClose()) {
			GLFW.glfwPollEvents();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			GL11.glClearColor(0.1f, .2f, .3f, 1);
			if (KeyboardInput.wasKeyPressed(GLFW.GLFW_KEY_F11)) {
				window.switchFullscreen();
			}
			if (KeyboardInput.wasKeyPressed(GLFW.GLFW_KEY_ESCAPE))
				window.requestWindowShouldClose();
			window.update();
		}
		window.dispose();
	}

	public static Window getWindow() {
		return window;
	}
}
