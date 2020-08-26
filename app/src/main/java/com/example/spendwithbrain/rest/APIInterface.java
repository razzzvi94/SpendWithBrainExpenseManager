package com.example.spendwithbrain.rest;

import com.example.spendwithbrain.rest.apiModels.response.CurrencyResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {
    @GET("latest?base=RON")
    Call<CurrencyResponse> getAllCurrency();
}
