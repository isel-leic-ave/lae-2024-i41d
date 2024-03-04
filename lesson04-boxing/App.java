public class App {
    public static void main(String[] args) {
        int a = 7;
        long b = 11;
        float c = 9.3f;
        double d = 3.4;
        foo(a); // invokestatic Integer.valueOf(int): Integer
        foo(b); // invokestatic Long.valueOf(long): Long
        foo(c); // invokestatic Float.valueOf(float): Float
        foo(d); // invokestatic Double.valueOf(double): Double
    }

    public static void foo(Object o) {}

    public static Integer incSeven(Integer nr) {
        /**
         * Unboxing nr to add 7 => e.g. nr.intValue();
         * Boxing the final result to Integer => Integer.valueOf(...)
         */
        return Integer.valueOf(nr.intValue() + 7);
    }
}