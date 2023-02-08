package tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import data.ClassManager;

public class LinterTestsV {
	//don't want to test the UI here, I will test that by hand, so I am just 
	//testing the two classes I'm planning for the logic, and getting the classes
	ClassManager c;
	
	public void setupTests(String s, boolean good) throws IOException {
		//add all the classes here through the class manager, using the testname and 
		//the result we want, aka whether the result should be fine or get something bad
		c = new ClassManager();
		c.getClasses(null);
	}

	//will have to create test classes for the linter to use
	@Test
	public void DIPTests() throws IOException {
		setupTests("dip", true);
		assertEquals("0 issues.\n", c.assessClasses());
		setupTests("dip", false);
		assertEquals("1 issue in INSERTCLASSNAMEHERE: INSERTRESULTHERE", c.assessClasses());
	}
	
	
	//these should currently fail!
	@Test
	public void badDesignPatternsTest() throws IOException {
		setupTests("design", true);
		assertEquals("0 issues.\n", c.assessClasses());
		setupTests("design", false);
		assertEquals("1 issue in LISTCLASSES: INSERTRESULTHERE", c.assessClasses());
	}
	
	@Test
	public void equalsHashcodeTest() throws IOException {
		setupTests("equals", true);
		assertEquals("0 issues.\n", c.assessClasses());
		setupTests("equals", false);
		assertEquals("1 issue in INSERTCLASSNAMEHERE: INSERTRESULTHERE", c.assessClasses());
	}
}
