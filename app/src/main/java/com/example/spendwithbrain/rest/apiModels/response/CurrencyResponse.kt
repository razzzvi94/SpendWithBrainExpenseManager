package com.example.spendwithbrain.rest.apiModels.response

data class CurrencyResponse(val isSuccess: Boolean, val base: String, val date: String, val rates: CurrencyCoin)