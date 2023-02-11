package tests;

public class CouplingTest1 {
    HashCodeTest1 x = null;


    public BadNameTest test(HashCodeTest1 test){
        return null;
    }

    public static AbstractTypeTest1 test1(AbstractTypeTest2 test2){
        return null;
    }

    public static void test2(){
        AbstractTypeTest2 test = new AbstractTypeTest2();
        HashCodeTest3 z = new HashCodeTest3();
        HashCodeTest2 y = new HashCodeTest2();
        CouplingTest2 x = new CouplingTest2();
        HashCodeTest1  p = new HashCodeTest1();

    }
}
