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
		
		System.out.println("Input a folder path: ");
		Scanner s = new Scanner(System.in);
		String in = s.nextLine();
		finder(in);
		try {
			c.getClasses(fileNames);
			System.out.println(c.assessClasses());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
