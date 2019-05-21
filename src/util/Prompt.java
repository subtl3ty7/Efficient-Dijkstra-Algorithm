package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * This is the main entrance point. Loads all the exercises specified in exercisesStrings with load() and starts
 * the interface in run(). 
 */
public class Prompt {
	private static Scanner scanner = new Scanner(System.in);

	public static int promptPaths(String header, ArrayList<Path> paths) {
		ArrayList<String> entries = new ArrayList<String>();
		for(Path p : paths)
			entries.add(p.getFileName().toString());
		
		return prompt(header, entries);
	}

	public static int prompt(String header, ArrayList<String> entries) {
		int choice = -1;
		System.out.println(header);

		int i = 1;
		for(String entry : entries) {
			System.out.println(formatIndex(i) + entry);
			i++;
		}
		System.out.println(formatIndex(0) + "End");
		System.out.print("> ");
	
		String in = scanner.next();
		try {
			choice = Integer.parseInt(in);
		} catch (NumberFormatException e) {
			System.out.println(notRecognized(in));
		}
		
		System.out.println("");
		
		return choice;
	}

	public static String getErrorMessage(String message) {
	    return "\u001B[31m" + "Error! " + "\u001B[0m" + message;
    }

    public static String getFileNotFoundMessage(String file) {
        return getErrorMessage("Data \"" + file + "\" not found!");
    }

    public static String getFolderNotFoundMessage(String folder) {
        return getErrorMessage("Folder \"" + folder + "\" not found!");
    }

    public static String getSuccessMessage(String message) {
        return "\u001B[32m" + "Success! " + "\u001B[0m" + message;
    }

	public static boolean terminate(int choice) {
		return (choice == -1 || choice == 0);
	}

	private static String formatIndex(int i) {
		return "(" + i + ") ";
	}
	
	private static String notRecognized(String in) {
		return "(" + in + ") not recognized!";
	}
}
