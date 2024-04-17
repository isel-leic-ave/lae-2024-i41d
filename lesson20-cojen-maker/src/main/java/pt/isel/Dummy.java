package pt.isel;

public class Dummy {
    private final int nr;

    public Dummy(int nr) {
        this.nr = nr;
    }

    public int mul(int other) {
        return this.nr * other;
    }
}

class Bar {
    static void test() {
        AppKt.foo("label", 341341);
        AppKt.foo("ola");
    }
}
