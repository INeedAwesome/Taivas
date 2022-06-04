package site.gr8.mattis.taivas.input;

import org.lwjgl.glfw.GLFW;
import site.gr8.mattis.taivas.Main;

public class MouseInput {

	public static final int BUTTON_LEFT = 0;
	public static final int BUTTON_RIGHT = 1;
	public static final int BUTTON_SCROLL_WHEEL = 2;
	public static final int BUTTON_SIDE_BACK = 3;
	public static final int BUTTON_SIDE_FRONT = 4;
	public static final int BUTTON_LAST = BUTTON_SIDE_FRONT;

	private static boolean[] buttons = new boolean[16];
	private static int[] keyStates = new int[16];

	private static int mouseX;
	private static int mouseY;
	private static int mouseDX;
	private static int mouseDY;


	public static void buttonCallback(long window, int key, int action, int mods) {
		try {
			keyStates[key] = action;
			buttons[key] = action != GLFW.GLFW_RELEASE;
		} catch (ArrayIndexOutOfBoundsException ignored){}
	}

	public static boolean isButtonDown(int button) {
		return buttons[button];
	}

	public static boolean wasButtonPressed(int button) {
		if (keyStates[button] == GLFW.GLFW_PRESS) {
			boolean isDown = buttons[button];
			buttons[button] = false;
			return isDown;
		}
		return false;
	}

	public static void scrollCallback(long window, double offsetX, double offsetY) {

	}
	public static void enableRawMouseMotion() {
		boolean canUseRaw = GLFW.glfwRawMouseMotionSupported();
		if (canUseRaw) {
			GLFW.glfwSetInputMode(Main.getWindow().getHandle(), GLFW.GLFW_RAW_MOUSE_MOTION, GLFW.GLFW_TRUE);
		}
	}


	public static void positionCallback(long window, double posX, double posY) {
		mouseDX += (int) posX - mouseX;
		mouseDY += (int) posY - mouseY;
		mouseX  =  (int) posX;
		mouseY  =  (int) posY;
	}

	public static int getMouseDX() {
		return mouseDX | (mouseDX = 0);
	}

	public static int getMouseDY() {
		return mouseDY | (mouseDY = 0);
	}
}
