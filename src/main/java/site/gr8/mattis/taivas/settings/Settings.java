package site.gr8.mattis.taivas.settings;

import site.gr8.mattis.taivas.utils.MyFileSystem;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Settings {

	private static Map<String, String> settingsMap = new HashMap<>();

	private static String settingsFileName = "settings.txt";

	private static File settingsFile;

	private static int times = 0;

	private Settings() {
	}

	public static void init() {
		System.out.println(("Initializing settings file."));
		settingsFile = MyFileSystem.addFile("",settingsFileName);
		// My system ensures the file exists.

		if (settingsFile.length() == 0) {
			writeDefaultSettingsFile();
		}
		load();
	}

	/**
	 * Gives you the value of a key in settings.txt
	 * @param key (String)
	 * @return {@link String}
	 */
	public static String getString(String key) {
		return settingsMap.get(key);
	}

	/**
	 * Gives you the value of a key in settings.txt
	 * @param key (String)
	 * @return {@link Integer}
	 */
	public static int getInt(String key) {
		return Integer.parseInt(settingsMap.get(key));
	}

	/**
	 * Gives you the value of a key in settings.txt
	 * @param key (String)
	 * @return {@link Float}
	 */
	public static float getFloat(String key) {
		return Float.parseFloat(settingsMap.get(key));
	}

	/**
	 * Gives you the value of a key in settings.txt
	 * @param key (String)
	 * @return {@link Double}
	 */
	public static double getDouble(String key) {
		return Double.parseDouble(settingsMap.get(key));
	}

	/**
	 * Gives you the value of a key in settings.txt
	 * @param key (String)
	 * @return {@link Boolean}
	 */
	public static boolean getBool(String key) {
		return Boolean.parseBoolean(settingsMap.get(key));
	}

	private static void load() {
		settingsMap.clear();
		try {
			Scanner fileScanner = new Scanner(settingsFile);
			while (fileScanner.hasNext()) {
				String line = fileScanner.nextLine();
				String[] array = line.split(":");
				if (array.length == 2) {
					settingsMap.put(array[0], array[1]);
				}
			}
		} catch (FileNotFoundException exception) {
			System.err.println("File could not be found. Generating a new one!");
			writeDefaultSettingsFile();
		}
	}

	private static void writeDefaultSettingsFile() {
		MyFileSystem.writeFile("", settingsFileName, """
			app.version:0.0.9
			fullscreen:false
			maxFPS:165
			vsync:true
			fov:90
			sensitivity:0.1
			lang:en-us
			""");
	}

}
