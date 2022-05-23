package site.gr8.mattis.taivas;

import org.lwjgl.glfw.GLFW;
import site.gr8.mattis.taivas.window.Window;

public class Main {

	private static Window window = null;

	public static void main(String[] args) {
		window = new Window(Constants.TITLE);
		window.init();
		window.createWindow();
		while (!window.windowShouldClose()) {
			GLFW.glfwPollEvents();
			window.update();
		}
		window.dispose();
	}

	public static Window getWindow() {
		return window;
	}
}
