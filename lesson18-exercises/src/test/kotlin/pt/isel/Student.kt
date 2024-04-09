package pt.isel

class Student (
    @Comparison(2) val nr:Int,
    val name: String,
    @Comparison(1) val nationality: String,
)