package com.example.roomies.model

data class Expense(
    var id: String = "",
    var title: String = "",
    var amount: Double = 0.0, // non-null total amount
    var persons: Int = 1,
    var dueDate: String = "",
    var isPaid: Boolean = false,
    var paidDate: String = ""
)