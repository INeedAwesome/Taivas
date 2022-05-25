package site.gr8.mattis.taivas.input;

import org.lwjgl.glfw.GLFW;

public class KeyboardInput {
	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
	private static int[] keyStates = new int[GLFW.GLFW_KEY_LAST];


	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		try {
			keyStates[key] = action;
			keys[key] = action != GLFW.GLFW_RELEASE;
		} catch (ArrayIndexOutOfBoundsException ignored){}
	}

	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}

	public static boolean wasKeyPressed(int keycode) {
		if (keyStates[keycode] == GLFW.GLFW_PRESS) {
			boolean isDown = keys[keycode]; // <--
			keys[keycode] = false;			// <--
			return isDown;					// <--
			// Thanks to illuminator3#0001 on discord for helping me with this,
			// in discord server Together Java in #frameworks_help_0, 2022.01.01
		}
		return false;
	}
}
