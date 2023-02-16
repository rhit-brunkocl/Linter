package tests;

public class SingletonTest {
	private static SingletonTest single;
	
	private SingletonTest() {
		single = new SingletonTest();
	}
	
	public static SingletonTest getSingleton() {
		return single;
	}
}
