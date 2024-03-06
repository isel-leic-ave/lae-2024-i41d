import static java.lang.System.out;

public class App {
    public static void main(String[] args) {
        new A().makeB().foo();
    }
}

class A {

    final B dummy = new B() {
        public void foo() {
            out.println("I am a different B");
        }

    };
    public B makeB() {
        return new B() {
            public void foo() {
                out.println("Foo from B on Java A class");
            }
        };
    }
}