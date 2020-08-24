package com.example.spendwithbrain.db.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.spendwithbrain.db.entities.ExpensesDetails
import com.example.spendwithbrain.db.entities.IncomeDetails
import com.example.spendwithbrain.db.entities.UserDetails

data class UserWithExpenses(
    @Embedded
    var userDetails: UserDetails,
    @Relation(parentColumn = "userId", entityColumn = "userId", entity = IncomeDetails::class)
    var incomes: MutableList<IncomeDetails>,
    @Relation(parentColumn = "userId", entityColumn = "userId", entity = ExpensesDetails::class)
    var expenses: MutableList<ExpensesDetails>
)