package tests;

public class DIPTest2 {
	public void method2() {
		new DIPTest1().method();
		new DIPTest1().method1();
		DIPTest3 test = new DIPTest1();
		test.method();
	}
}
