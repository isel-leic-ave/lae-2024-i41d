package pt.isel.annotations

@Color("Green")
class Account(
    @Color("blue") @property:Tag var balance: Long,
    @Color("red")val owner: String
) {
    fun deposit(@Tag value: Long) {
        balance += value
    }
}