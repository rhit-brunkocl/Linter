package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
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
	
	public void getClasses(ArrayList<String> fileNames) throws IOException{
		classes = new ArrayList<ClassNode>();
		for(String fileName: fileNames) {
			ClassReader reader = new ClassReader(fileName);
			ClassNode classNode = new ClassNode();
			reader.accept(classNode, ClassReader.EXPAND_FRAMES);
			classes.add(classNode);
		}
	}
	
	public String assessClasses() {
		return linter.doAllTests(classes);
	}
	
}
