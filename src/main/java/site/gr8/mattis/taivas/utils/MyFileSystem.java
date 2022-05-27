package site.gr8.mattis.taivas.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MyFileSystem {

	public static File root = null;

	public static void setRoot(String resources) {
		root = new File(resources);
		if (!root.exists()) {
			root.mkdirs();
		}
	}

	public static File addFile(String filename) {
		File file = new File(root, filename);
		if (!file.exists()) {
			try {
				file.mkdirs();
				file.delete();
				file.createNewFile();
				return file;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static File addFile(String root, String filename) {
		File localRoot = new File(MyFileSystem.root, root);
		if (!localRoot.exists()) {
			localRoot.mkdirs();
		}
		File file = new File(localRoot, filename);
		try {
			if (!file.exists()) {
				file.createNewFile();
				return file;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static void writeFile(String root, String file, String value) {
		try (final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(addFile(root + "/" + file)), StandardCharsets.UTF_8))) {
			printWriter.println(value);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
