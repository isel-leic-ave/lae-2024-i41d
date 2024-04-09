package pt.isel

class Person(
    val id: Int,
    val name: String,
    @Comparison(cmp = AddressByRoad::class) val address: Address,
    @Comparison(cmp = AccountByBalance::class) val account: Account) {
}

class AccountByBalance : Comparator<Account>{
    override fun compare(o1: Account, o2: Account): Int {
        return o1.balance.compareTo(o2.balance);
    }
}

class AddressByRoad : Comparator<Address> {
    override fun compare(o1: Address, o2: Address): Int {
        return o1.road.compareTo(o2.road)
    }
}