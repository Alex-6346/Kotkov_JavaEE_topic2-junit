import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class Test1 {

    @Test
     void mockExample() {
        MyObject myObject = Mockito.mock(MyObject.class);
        Mockito.when(myObject.myReturnMethod()).thenReturn(1);
        Mockito.doThrow(new RuntimeException()).when(myObject).voidMethod();
    }

    class MyObject {
        public int myReturnMethod() { return 1; }
        public void voidMethod() { System.out.println("test"); }
    }



}
