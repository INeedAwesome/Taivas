package site.gr8.mattis.taivas.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import site.gr8.mattis.taivas.Constants;

public class Window {

	private long handle;
	private boolean resized;
	private boolean fullscreen;

	private int width;
	private int height;
	private boolean vsync;
	private String title;

	public long getHandle() {
		return handle;
	}

	public Window(int width, int height, String title, boolean vsync) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.vsync = vsync;
		this.resized = false;
	}
	public Window(String title) {
		this.width = DefaultWindowsAttribs.width;
		this.height = DefaultWindowsAttribs.height;
		this.title = title;
		this.vsync = DefaultWindowsAttribs.vsync;
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
	}

	public void dispose() {
		GLFW.glfwDestroyWindow(getHandle());
		GLFWErrorCallback.createPrint(System.err).free();
		GLFW.glfwTerminate();
	}

	public void createWindow() {
		handle = GLFW.glfwCreateWindow(this.width, this.height, this.title, 0, 0);
		if (handle == 0) {
			System.err.println("Could not instantiate a GLFW window! ");
			System.exit(Constants.EXC_GLFW_WINDOW_INIT);
		}
		GLFW.glfwMakeContextCurrent(getHandle());
		GL.createCapabilities();
		GLFW.glfwSetFramebufferSizeCallback(getHandle(), this::FrameBufferCallback);

		GLFW.glfwShowWindow(getHandle());

	}
	public void update() {
		GLFW.glfwSwapBuffers(getHandle());
		if (isResized()) {
			GL11.glViewport(0, 0, getWidth(), getHeight());
			setResized(false);
		}
	}
	public boolean windowShouldClose() {
		return GLFW.glfwWindowShouldClose(getHandle());
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
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		GLFW.glfwSetWindowSize(getHandle(),width, this.height);
		setResized(true);
	}

	public int getHeight() {
		return height;
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
				GLFW.glfwSetWindowMonitor(getHandle(), GLFW.glfwGetPrimaryMonitor(), 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());
			}
			return;
		}
		// switch to windowed mode
		GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		if (vidMode != null) {
			GLFW.glfwRestoreWindow(getHandle()); // fix for the corners
			GLFW.glfwSetWindowMonitor(getHandle(),0,
					(vidMode.width() / 2) - (DefaultWindowsAttribs.width / 2),
					(vidMode.height() / 2) - (DefaultWindowsAttribs.height / 2),
					DefaultWindowsAttribs.width, DefaultWindowsAttribs.height,
					vidMode.refreshRate());

		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		GLFW.glfwSetWindowTitle(getHandle(), title);
	}

	public static class DefaultWindowsAttribs {
		public static final int width = 896;
		public static final int height = 504;
		public static final String title = "Taivas";
		public static final boolean vsync = true;

	}
}
