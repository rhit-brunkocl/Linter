package domain;

import org.junit.Test;

public class LinterTestsV {
	//don't want to test the UI here, I will test that by hand, so I am just 
	//testing the two classes I'm planning for the logic, and getting the classes
	ClassManager c;
	
	public void setupTests(String s, boolean good) {
		//add all the classes here through the class manager, using the testname and 
		//the result we want, aka whether the result should be fine or get something bad
	}

	//will have to create test classes for the linter to use
	@Test
	public void hollywoodPrincipleTests() {
		setupTests("hollywood", true);
		assertEquals("0 issues.\n", c.assessClasses());
		setupTests("hollywood", false);
		assertEquals("1 issue in INSERTCLASSNAMEHERE: INSERTRESULTHERE", c.assessClasses());
	}
	
	@Test
	public void badDesignPatternsTest() {
		setupTests("design", true);
		assertEquals("0 issues.\n", c.assessClasses());
		setupTests("design", false);
		assertEquals("1 issue in LISTCLASSES: INSERTRESULTHERE", c.assessClasses());
	}
	
	@Test
	public void equalsHashcodeTest() {
		setupTests("equals", true);
		assertEquals("0 issues.\n", c.assessClasses());
		setupTests("equals", false);
		assertEquals("1 issue in INSERTCLASSNAMEHERE: INSERTRESULTHERE", c.assessClasses());
	}
}
