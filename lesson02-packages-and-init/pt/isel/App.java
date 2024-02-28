package pt.isel;

public class App {
    public static void main(String[] args) {
        perTypeMethod(); // <=> App.perTypeMethod();

        new App().otherInstanceMethod();
    }

    public void perInstanceMethod() {}

    public void otherInstanceMethod() {
        perInstanceMethod(); // <=> this.perInstanceMethod()
    }

    public static void perTypeMethod() {}
}
