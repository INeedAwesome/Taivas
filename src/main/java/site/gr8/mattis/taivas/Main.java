package site.gr8.mattis.taivas;

import org.lwjgl.glfw.GLFW;
import site.gr8.mattis.taivas.window.Window;

public class Main {

	private static Window window = null;

	public static void main(String[] args) throws InterruptedException {
		window = new Window(Constants.DEFAULT_TITLE);
		window.init();
		window.createWindow();
		while (!window.windowShouldClose()) {
			GLFW.glfwPollEvents();
			window.update();
			if (GLFW.glfwGetKey(window.getHandle(), GLFW.GLFW_KEY_0) == GLFW.GLFW_PRESS) {
				window.switchFullscreen();
				Thread.sleep(40);
			}
		}
		window.dispose();
	}

	public static Window getWindow() {
		return window;
	}
}
