package com.example.spendwithbrain.screens.main.fragments.expenses.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spendwithbrain.R
import com.example.spendwithbrain.screens.main.fragments.expenses.adapter.models.Transaction
import com.example.spendwithbrain.utils.TimeUtils

class TransactionAdapter(
    private val context: Context,
    private val arrayList: List<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false)

        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentItem = arrayList[position]
        holder.run {
            transactionDate.text = TimeUtils.transactionTimeFormat(context, currentItem.date)
            transactionAmountValue.text = "-" + currentItem.amount.toString()
            transactionCategoryName.text = currentItem.category
            transactionName.text = currentItem.name
            balanceValue.text = currentItem.balance.toString()
            if (currentItem.balance < 0)
                balanceValue.setTextColor(context.getColor(R.color.colorRed))
        }

        when (currentItem.category) {
            context.getString(R.string.income) -> {
                holder.transactionCategoryIcon.setImageResource(R.drawable.ic_category_income)
                holder.transactionAmountValue.text = "+" + currentItem.amount.toString()
                holder.transactionAmountValue.setTextColor(context.getColor(R.color.colorGreen))
            }

            context.getString(R.string.food) -> {
                holder.transactionCategoryIcon.setImageResource(R.drawable.ic_category_food)
            }
            context.getString(R.string.car) -> {
                holder.transactionCategoryIcon.setImageResource(R.drawable.ic_category_car)
            }
            context.getString(R.string.clothes) -> {
                holder.transactionCategoryIcon.setImageResource(R.drawable.ic_category_clothes)
            }
            context.getString(R.string.savings) -> {
                holder.transactionCategoryIcon.setImageResource(R.drawable.ic_category_savings)
            }
            context.getString(R.string.health) -> {
                holder.transactionCategoryIcon.setImageResource(R.drawable.ic_category_health)
            }
            context.getString(R.string.beauty) -> {
                holder.transactionCategoryIcon.setImageResource(R.drawable.ic_category_beauty)
            }
            context.getString(R.string.travel) -> {
                holder.transactionCategoryIcon.setImageResource(R.drawable.ic_category_travel)
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionDate: TextView = itemView.findViewById(R.id.transaction_date_textView)
        val transactionCategoryIcon: ImageView =
            itemView.findViewById(R.id.transaction_category_icon)
        val transactionCategoryName: TextView =
            itemView.findViewById(R.id.transaction_category_name_textView)
        val transactionAmountValue: TextView =
            itemView.findViewById(R.id.transaction_amount_value_textView)
        val transactionName: TextView = itemView.findViewById(R.id.transaction_name_textView)
        val balanceValue: TextView = itemView.findViewById(R.id.balance_value_textView)
    }
}