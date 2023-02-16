package presentation;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import data.ClassManager;

public class main {
	static ArrayList<String> fileNames = new ArrayList<>();
	public static File[] finder(String dirName) {

	    File dir = new File(dirName);

	    return dir.listFiles(new FilenameFilter() {

	        public boolean accept(File dir, String filename) {
	            if(filename.endsWith(".java"))
	            {
	            	fileNames.add(dir.getName() + "." + filename.substring(0, filename.length()-5));
	            }
	            return filename.endsWith(".java");

	        }
	    });
	}
	public static void main(String[] args) {
		ClassManager c = new ClassManager();
		ArrayList<Boolean> checks = new ArrayList<Boolean>();
		System.out.println("Input a folder path: ");
		Scanner s = new Scanner(System.in);
		String in = s.nextLine();
		finder(in);
		System.out.println("Check for hashCode and equals method issues? (y/n)");
		in = s.nextLine();
		if(in.charAt(0) == 'y') {
			checks.add(true);
		}else {
			checks.add(false);
		}
		
		System.out.println("Check for singletons? (y/n)");
		in = s.nextLine();
		if(in.charAt(0) == 'y') {
			checks.add(true);
		}else {
			checks.add(false);
		}
		
		System.out.println("Check for DIP violations? (y/n)");
		in = s.nextLine();
		if(in.charAt(0) == 'y') {
			checks.add(true);
		}else {
			checks.add(false);
		}
		
		System.out.println("Check for Abstract Methods Implementation? (y/n)");
		in = s.nextLine();
		if(in.charAt(0) == 'y') {
			checks.add(true);
		}else {
			checks.add(false);
		}
		
		System.out.println("Check for Adapter Patter? (y/n)");
		in = s.nextLine();
		if(in.charAt(0) == 'y') {
			checks.add(true);
		}else {
			checks.add(false);
		}
		
		System.out.println("Check for LSP violations? (y/n)");
		in = s.nextLine();
		if(in.charAt(0) == 'y') {
			checks.add(true);
		}else {
			checks.add(false);
		}
		
		try {
			c.getClasses(fileNames);
			System.out.println(c.assessClasses(checks));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
