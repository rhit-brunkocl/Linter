package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import domain.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public class ClassManager {
	Linter linter;
	ArrayList<ClassNode> classes;
	ArrayList<String> routes;
	ArrayList<ClassReader> readers;
	
	public ClassManager() {
		linter = new Linter();
		classes = new ArrayList<ClassNode>();
		routes = new ArrayList<>();
		readers = new ArrayList<>();
	}
	
	public void getClasses(ArrayList<String> fileNames) throws IOException{
		classes = new ArrayList<ClassNode>();
		for(String fileName: fileNames) {
			String filePath = fileName.replace(".", "/") + ".java";
			FileInputStream is = new FileInputStream(filePath);
			ClassReader reader = new ClassReader(is);
			ClassNode classNode = new ClassNode();
			reader.accept(classNode, ClassReader.EXPAND_FRAMES);
			routes.add(fileName);
			readers.add(reader);
			classes.add(classNode);
		}
	}
	
	public String assessClasses(ArrayList<Boolean> tests) {
		ArrayList<CheckBehavior> checks = new ArrayList<CheckBehavior>();
		if(tests.get(0)) {
			checks.add(new HashCheckBehavior());
		}
		if(tests.get(1)) {
			checks.add(new SingletonCheckBehavior());
		}
		if(tests.get(2)) {
			checks.add(new DIPCheckBehavior());
		}
		if(tests.get(3)) {
			checks.add(new AbstractTypeCheck());
		}
		if(tests.get(4)) {
			checks.add(new AdapterCheckBehavior());
		}
		if(tests.get(5)) {
			checks.add(new LSPCheckBehavior());
		}
		if(tests.get(6)) {
			checks.add(new BadNameCheck());
		}
		if(tests.get(7)) {
			checks.add(new TemplateCheck());
		}
		if(tests.get(8)) {
			checks.add(new TightCouplingCheck());
		}
		return linter.doAllTests(routes, readers, classes, checks);
	}
	
}
