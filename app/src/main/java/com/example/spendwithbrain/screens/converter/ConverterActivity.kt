package com.example.spendwithbrain.screens.converter

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spendwithbrain.R
import com.example.spendwithbrain.rest.APIClient
import com.example.spendwithbrain.rest.APIInterface
import com.example.spendwithbrain.rest.apiModels.response.CurrencyCoin
import com.example.spendwithbrain.rest.apiModels.response.CurrencyResponse
import com.example.spendwithbrain.screens.converter.adapter.CurrencyAdapter
import com.example.spendwithbrain.screens.converter.adapter.models.CurrencyItem
import kotlinx.android.synthetic.main.activity_add_action.view.*
import kotlinx.android.synthetic.main.activity_converter.view.*
import kotlinx.android.synthetic.main.home_toolbar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ConverterActivity : AppCompatActivity() {
    private lateinit var layout: View
    private val apiInterface: APIInterface = APIClient.getClient().create(APIInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = View.inflate(this, R.layout.activity_converter, null)
        setContentView(layout)

        iniToolbar()
        getCurrency()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun iniToolbar() {
        var toolbar = layout.home_tool_bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = getString(R.string.converter)
        toolbar.setTitleTextAppearance(this, R.style.HomeToolbarFont)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        layout.home_tool_bar.button_add_action.visibility = View.GONE
    }

    private fun initMessage(date: String) {
        layout.converted_using_api_textView.text =
            resources.getString(R.string.dodgy_api_date, date)
    }

    private fun getCurrency() {
        val call: Call<CurrencyResponse> = apiInterface.allCurrency
        call.enqueue(object : Callback<CurrencyResponse?> {
            override fun onResponse(
                call: Call<CurrencyResponse?>,
                response: Response<CurrencyResponse?>
            ) {
                if (response.isSuccessful) {
                    Log.d("Currency", "The response was received")
                    val resp: CurrencyResponse? = response.body()
                    val currencyObj: CurrencyCoin = resp?.rates!!
                    initSpinners(currencyObj)
                    initMessage(resp.date)
                } else {
                    Log.d("Currency", "Error")
                    Toast.makeText(applicationContext, "Fail retrieve info", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<CurrencyResponse?>, t: Throwable) {
                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        })
    }

    private fun initSpinners(currencyObj: CurrencyCoin) {
        val modelList: MutableList<CurrencyItem> = mutableListOf()
        modelList.add(CurrencyItem("EUR", R.drawable.ic_euro ,currencyObj.EUR))
        modelList.add(CurrencyItem("USD", R.drawable.ic_dolar ,currencyObj.USD))
        modelList.add(CurrencyItem("GBP", R.drawable.ic_pound,currencyObj.GBP))
        modelList.add(CurrencyItem("CHF", R.drawable.ic_chf,currencyObj.CHF))
        modelList.add(CurrencyItem("AUD", R.drawable.ic_aud,currencyObj.AUD))

        val foreignSpinner = CurrencyAdapter(applicationContext, modelList)
        layout.foreign_currency_spinner.adapter = foreignSpinner

        val nativeList: MutableList<CurrencyItem> = mutableListOf()
        nativeList.add(CurrencyItem("RON", R.drawable.ic_flag_ro,1.0))
        val nativeSpinner = CurrencyAdapter(applicationContext, nativeList)
        layout.native_currency_spinner.adapter = nativeSpinner

        layout.foreign_currency_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    var item: CurrencyItem = parent?.getItemAtPosition(position) as CurrencyItem
                    var valueToConvert = layout.native_currency.text.toString().toDouble()
                    layout.foreign_currency.setText(
                        (layout.native_currency.text.toString()
                            .toDouble() * item.currencyValue).toString()
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }
}