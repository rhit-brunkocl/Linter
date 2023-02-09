package tests;

public class SingletonTest {
	private SingletonTest single;
	
	private SingletonTest() {
		single = new SingletonTest();
	}
	
	public SingletonTest getSingleton() {
		return single;
	}
}
