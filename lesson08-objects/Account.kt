public class Account {

    companion object {
        var countInstances = 0
    }

    init {
        Account.countInstances++
    }
}

fun main() {
    Account()
    Account()
    Account()
    Account()
    println(Account.countInstances)
}