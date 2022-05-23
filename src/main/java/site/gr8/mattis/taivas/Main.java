package site.gr8.mattis.taivas;

import org.lwjgl.glfw.GLFW;
import site.gr8.mattis.taivas.window.Window;

public class Main {

	public static void main(String[] args) {
		Window window = new Window(Constants.TITLE);
		window.init();
		window.createWindow();
		int i = 1000;
		while (!window.windowShouldClose()) {
			GLFW.glfwPollEvents();
			window.setWidth(i + 1);
			i++;
			window.update();
		}

	}
}
