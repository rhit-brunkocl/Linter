package tests;

public class CouplingTest1 {
    HashCodeTest1 x = null;


    public BadNameTest test(HashCodeTest1 test){
        return null;
    }

    public static Test1 test1(Test2 test2){
        return null;
    }

    public static void test2(){
        Test2 test = new Test2();
        HashCodeTest3 z = new HashCodeTest3();
        HashCodeTest2 y = new HashCodeTest2();
        Test1 test1 = new Test1();
        HashCodeTest1  p = new HashCodeTest1();

    }
}
