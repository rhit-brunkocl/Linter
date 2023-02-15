package tests;

import static org.junit.Assert.assertEquals;

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
	Linter l = new Linter();
	ArrayList<ClassNode> classes;
	public void setupTests(String s, boolean good) throws IOException {
		classes = new ArrayList<ClassNode>();
		if(s.equals("singleton")) {
			if(good) {
				addClass("tests.Test1");
				addClass("tests.Test2");
			}else {
				addClass("tests.SingletonTest");
			}
		}else if(s.equals("dip")) {
			if(good) {
				addClass("tests.Test1");
				addClass("tests.Test2");
			}else {
			    
			}
		}else {
			if(good) {
				addClass("tests.Test1");
				addClass("tests.Test2");
				addClass("tests.HashCodeTest1");
			}else {
				addClass("tests.HashCodeTest2");
				addClass("tests.HashCodeTest3");
			}
		}
	}
	
	public void addClass(String s) throws IOException {
		ClassReader reader = new ClassReader(s);
		ClassNode classNode = new ClassNode();
		reader.accept(classNode, ClassReader.EXPAND_FRAMES);
		classes.add(classNode);
	}

	//these should fail!
	@Test
	public void DIPTests() throws IOException {
		setupTests("dip", true);
		assertEquals("0 issues found.\n", l.doAllTests(classes));
		setupTests("dip", false);
		assertEquals("insert error message here", l.doAllTests(classes));
	}
	
	//these should pass
	@Test
	public void SingletonTest() throws IOException {
		setupTests("singleton", true);
		assertEquals("0 issues found.\n", l.doAllTests(classes));
		setupTests("singleton", false);
		assertEquals("Issue found in tests.SingletonTest: Bad pattern, Singleton\n", l.doAllTests(classes));
	}
	
	//these should pass
	@Test
	public void equalsHashcodeTest() throws IOException {
		setupTests("equals", true);
		assertEquals("0 issues found.\n", l.doAllTests(classes));
		setupTests("equals", false);
		assertEquals("Issue in tests.HashCodeTest2: equals() method with no hashCode() method.\n"
				+ "Issue in tests.HashCodeTest3: hashCode() method with no equals() method.\n", l.doAllTests(classes));
	}
}
