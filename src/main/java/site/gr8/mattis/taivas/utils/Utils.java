package site.gr8.mattis.taivas.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Utils {

	public static String loadStringFromFile(File file) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
			return shaderSource.toString();
		}
		catch (IOException exception) {
			System.err.printf("Could not read file '%b'!", file.getName());
			exception.printStackTrace();
		}
		return "";
	}
}
