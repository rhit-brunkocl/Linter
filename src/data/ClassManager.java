package data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import domain.Linter;

public class ClassManager {
	Linter linter;
	ArrayList<ClassNode> classes;
	
	public ClassManager() {
		linter = new Linter();
		classes = new ArrayList<ClassNode>();
	}
	
	public void getClasses() throws IOException{
		System.out.println("Input the paths to the classes you want to scan, and press q when you are done: ");
		Scanner s = new Scanner(System.in);
		String in = s.nextLine();
		while(!in.equals("q")) {
			InputStream input = new FileInputStream(in);
			ClassReader reader = new ClassReader(input);
			ClassNode classNode = new ClassNode();
			reader.accept(classNode, ClassReader.EXPAND_FRAMES);
			classes.add(classNode);
		}
	}
	
	public String assessClasses() {
		return linter.doAllTests(classes);
	}
}
