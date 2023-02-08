package presentation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import data.ClassManager;

public class main {
	public static void main(String[] args) {
		ClassManager c = new ClassManager();
		
		System.out.println("Input the paths to the classes you want to scan, and press q when you are done: ");
		Scanner s = new Scanner(System.in);
		String in = s.nextLine();
		ArrayList<File> files = new ArrayList<File>();
		while(!in.equals("q")) {
			Path path = Paths.get(in);
			File f = path.toFile();
			files.add(f);
		}
		try {
			c.getClasses(files);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(c.assessClasses());
	}
}
