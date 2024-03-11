public class App {
    public static void main(String[] args) {
        Person p = new Person("Maria");
        System.out.println(p.getName());
        // p.name = "Manuela"; // error: name has private access in Person
        p.setName("Manuela");
    }
}