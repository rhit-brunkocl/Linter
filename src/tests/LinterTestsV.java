package tests;

import static org.junit.Assert.assertEquals;

import java.awt.Desktop;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import data.ClassManager;
import domain.Linter;

public class LinterTestsV {
	ClassManager c = new ClassManager();
	ArrayList<String> classes;
	ArrayList<Boolean> bools = new ArrayList<Boolean>();
	public void setupTests(String s, boolean good) throws IOException {
		for(int i = 0; i < 3; i++) {
			bools.add(true);
		}
		for(int i = 0; i < 3; i++) {
			bools.add(false);
		}
		classes = new ArrayList<String>();
		if(s.equals("singleton")) {
			if(good) {
				classes.add("tests.Test1");
				classes.add("tests.Test2");
			}else {
				classes.add("tests.SingletonTest");
			}
		}else if(s.equals("dip")) {
			if(good) {
				classes.add("tests.Test1");
				classes.add("tests.Test2");
			}else {
				classes.add("tests.DIPTest1");
			    classes.add("tests.DIPTest2");
			}
		}else {
			if(good) {
				classes.add("tests.Test1");
				classes.add("tests.Test2");
				classes.add("tests.HashCodeTest1");
			}else {
				classes.add("tests.HashCodeTest2");
				classes.add("tests.HashCodeTest3");
			}
		}
		c.getClasses(classes);
	}

	@Test
	public void DIPTests() throws IOException {
		setupTests("dip", true);
		assertEquals("0 issues found.\n", c.assessClasses(bools));
		setupTests("dip", false);
		assertEquals("Issue in tests.DIPTest2: method method2() calls method method() in implementation tests.DIPTest1 instead of in interface tests.DIPTest3 which is a DIP violation\n",c.assessClasses(bools));
	}
	
	@Test
	public void SingletonTest() throws IOException {
		setupTests("singleton", true);
		assertEquals("0 issues found.\n", c.assessClasses(bools));
		setupTests("singleton", false);
		assertEquals("Issue in tests.SingletonTest: Bad pattern, Singleton\n", c.assessClasses(bools));
	}
	
	@Test
	public void equalsHashcodeTest() throws IOException {
		setupTests("equals", true);
		assertEquals("0 issues found.\n", c.assessClasses(bools));
		setupTests("equals", false);
		assertEquals("Issue in tests.HashCodeTest2: equals() method with no hashCode() method.\n"
				+ "Issue in tests.HashCodeTest3: hashCode() method with no equals() method.\n", c.assessClasses(bools));
	}
	
	@Test
	public void OBSCURETests() {
		classes = new ArrayList<String>();
		classes.add("java.lang.Runtime");
		classes.add("java.awt.Desktop");
		classes.add("java.util.Observable");
		classes.add("java.util.Observer");
		
		System.out.println("OBSCURE Tests: \n");
		
		for(int i = 0; i < 3; i++) {
			bools.add(true);
		}
		for(int i = 0; i < 3; i++) {
			bools.add(false);
		}
		try {
			c.getClasses(classes);
			System.out.println(c.assessClasses(bools));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void MoreUMLPuzzlesTests() {
		classes = new ArrayList<String>();
		classes.add("java.util.Calendar");
		Desktop d = Desktop.getDesktop();
		System.out.println("\n\nMore UML Tests: \n");
		
		for(int i = 0; i < 3; i++) {
			bools.add(true);
		}
		for(int i = 0; i < 3; i++) {
			bools.add(false);
		}
		try {
			c.getClasses(classes);
			System.out.println(c.assessClasses(bools));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
