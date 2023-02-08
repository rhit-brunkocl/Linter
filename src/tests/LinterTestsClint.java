package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import data.ClassManager;

public class LinterTestsClint {
	ClassManager manager;
	
	public void setupTest(String s, boolean pass) {
		
	}
	
	@Test
	void TestMissingImplementation() {
		setupTest("MissingImplementation", true);
		assertEquals("0 issues", manager.assessClasses());
		setupTest("MissingImplementation", false);
		assertEquals("1 issure in (ClassName): (ErrorMessage)", manager.assessClasses());
	}
	
	@Test
	void TestSingleResponsibility() {
		setupTest("SingleRes", true);
		assertEquals("0 issues", manager.assessClasses());
		setupTest("SingleRes", false);
		assertEquals("1 issure in (ClassName): (ErrorMessage)", manager.assessClasses());
	}
	
}
