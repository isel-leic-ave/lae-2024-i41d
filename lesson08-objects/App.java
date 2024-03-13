public class App {
    public static void main(String[] args) {
        new Account();
        new Account();
        new Account();
        new Account();
        System.out.println(Account.Companion.getCountInstances());
    }
}
