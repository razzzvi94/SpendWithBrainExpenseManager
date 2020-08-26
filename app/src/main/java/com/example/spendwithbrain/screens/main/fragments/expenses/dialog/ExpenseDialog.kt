package com.example.spendwithbrain.screens.main.fragments.expenses.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.spendwithbrain.R
import kotlinx.android.synthetic.main.dialog_transaction_info.view.*

class ExpenseDialog: DialogFragment() {
    private lateinit var layout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.dialog_transaction_info, container, false)

        initComponents()
        return layout
    }

    private fun initComponents() {
        layout.close_dialog_btn.setOnClickListener { dialog?.dismiss() }
    }

}