package site.gr8.mattis.taivas.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import site.gr8.mattis.taivas.input.MouseInput;
import site.gr8.mattis.taivas.utils.Constants;
import site.gr8.mattis.taivas.input.KeyboardInput;
import site.gr8.mattis.taivas.settings.Settings;

public class Window {

	private long handle;
	private boolean resized;
	private boolean fullscreen;
	private boolean maximized;

	private int posX;
	private int posY;
	private int width;
	private int height;
	private boolean vsync;
	private String title;

	public long getHandle() {
		return this.handle;
	}

	public Window(String title) {
		this.width = Constants.DEFAULT_WIDTH;
		this.height = Constants.DEFAULT_HEIGHT;
		this.title = title;
		this.vsync = Constants.DEFAULT_VSYNC;
		this.resized = false;
	}

	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!GLFW.glfwInit()) {
			System.err.println("Could not instantiate GLFW! ");
			System.exit(Constants.EXC_GLFW_INIT);
		}
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, Settings.getInt("samples"));
	}

	public void createWindow() {
		handle = GLFW.glfwCreateWindow(this.width, this.height, this.title, 0, 0);
		if (handle == 0) {
			System.err.println("Could not instantiate a GLFW window! ");
			GLFW.glfwTerminate();
			System.exit(Constants.EXC_GLFW_WINDOW_INIT);
		}
		GLFW.glfwMakeContextCurrent(getHandle());
		GL.createCapabilities();
		setVsync(isVsync());
		centerWindow();
		GLFW.glfwSetFramebufferSizeCallback(getHandle(), this::FrameBufferCallback);
		GLFW.glfwSetKeyCallback(getHandle(), KeyboardInput::keyCallback);
		GLFW.glfwSetMouseButtonCallback(getHandle(), MouseInput::buttonCallback);
		GLFW.glfwSetScrollCallback(getHandle(), MouseInput::scrollCallback);
		GLFW.glfwSetCursorPosCallback(getHandle(), MouseInput::positionCallback);
		MouseInput.enableRawMouseMotion();

		if (Constants.START_IN_MAXIMISED)
			GLFW.glfwMaximizeWindow(getHandle());

		GLFW.glfwShowWindow(getHandle());
	}

	private void centerWindow() {
		GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		assert vidMode != null;
		GLFW.glfwSetWindowPos(getHandle(), (vidMode.width() - this.width) / 2, (vidMode.height() - this.height) / 2);
	}

	public void update() {
		GLFW.glfwSwapBuffers(getHandle());
		if (isResized()) {
			GL11.glViewport(0, 0, getWidth(), getHeight());
			setResized(false);
		}
	}

	public void dispose() {
		GLFW.glfwDestroyWindow(getHandle());
		GLFWErrorCallback.createPrint(System.err).free();
		GLFW.glfwTerminate();
	}

	public boolean windowShouldClose() {
		return GLFW.glfwWindowShouldClose(getHandle());
	}

	public void requestWindowShouldClose() {
		GLFW.glfwSetWindowShouldClose(getHandle(), true);
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
		GLFW.glfwSetWindowPos(getHandle(), posX, this.posY);
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
		GLFW.glfwSetWindowPos(getHandle(), this.posX, posY);
	}

	public boolean isResized() {
		return resized;
	}

	public void setResized(boolean resized) {
		this.resized = resized;
	}

	private void FrameBufferCallback(long windowHandle, int width, int height) {
		this.width = width;
		this.height = height;
		setResized(true);
	}

	public int getWidth() {
		return this.width == 0 ? 1 : this.width;
	}

	public void setWidth(int width) {
		this.width = width;
		GLFW.glfwSetWindowSize(getHandle(),width, this.height);
		setResized(true);
	}

	public int getHeight() {
		return this.height == 0 ? 1 : this.height;
	}

	public void setHeight(int height) {
		this.height = height;
		GLFW.glfwSetWindowSize(getHandle(),this.width, height);
		setResized(true);
	}

	public boolean isVsync() {
		return vsync;
	}

	public void setVsync(boolean vsync) {
		this.vsync = vsync;
		GLFW.glfwSwapInterval(vsync ? 1 : 0);
	}

	public void switchFullscreen() {
		if (this.isFullscreen()) {
			setFullscreen(false);
			return;
		}
		setFullscreen(true);
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
		if (fullscreen) {
			// switch to fullscreen
			GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			if (vidMode != null) {
				int[] x = {0};
				int[] y = {0};
				GLFW.glfwGetWindowPos(getHandle(), x, y);
				setPosX(x[0]);
				setPosY(y[0]);
				maximized = GLFW.glfwGetWindowAttrib(getHandle(), GLFW.GLFW_MAXIMIZED) == 1;
				GLFW.glfwSetWindowMonitor(getHandle(), GLFW.glfwGetPrimaryMonitor(), 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());
			}
			return;
		}
		// switch to windowed mode
		GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		if (vidMode != null) {
			GLFW.glfwRestoreWindow(getHandle()); // fix for the corners
			GLFW.glfwSetWindowMonitor(getHandle(),0,
					getPosX(), getPosY(),
					Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT,
					vidMode.refreshRate());
			if (getPosX() < 1 || getPosY() < 31)
				centerWindow();
			if (maximized)
				GLFW.glfwMaximizeWindow(getHandle());
			setResized(true);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		GLFW.glfwSetWindowTitle(getHandle(), title);
	}

}
