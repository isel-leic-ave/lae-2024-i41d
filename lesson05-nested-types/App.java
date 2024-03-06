import static java.lang.System.out;

public class App {
    public static void main(String[] args) {
        new A(7).makeB().foo();
        new A(11).makeB().foo();
    }
}

class Outer {
    /**
     * Additional FIELD this$0 because it is non-static.
     */
    class Inner {
    }
    static class Nested {
    }
}

/**
 * Members:
 * - FIELD nr
 * - constructor A(), i.e. <init>
 * - METHOD makeB()
 */
class A {
    private final int nr;

    public A(int nr) {
        this.nr = nr;
    }
    public B makeB() {
        // <=> new B(this)
        return new B();       
    }
    /**
     * MEMBERS:
     * - METHOD foo
     * - FIELD this$0
     * - constructor A$B(A), i.e. <init>(A):
     *   > initializes field this$0 with the value of the parameter.
     *  
     */
    class B {
        public void foo() {
            // <=> out.println(A.this.nr);
            // A.this <=> FIELD this$0
            // 
            out.println(nr); // Inner class accessing field from outer class
        }
    }

}