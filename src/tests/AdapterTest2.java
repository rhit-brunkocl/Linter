package tests;

public class AdapterTest2 implements AdapterTest1 {

	private AdapterTest3 adaptee;

	public AdapterTest2(AdapterTest3 adaptee) {
		this.adaptee = adaptee;
	}

	@Override
	public void test1() {
		this.adaptee.test2();
	}

}
