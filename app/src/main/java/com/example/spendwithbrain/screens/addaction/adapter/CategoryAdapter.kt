package com.example.spendwithbrain.screens.addaction.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.spendwithbrain.R
import com.example.spendwithbrain.models.CategoryItem

class CategoryAdapter(
    private val context: Context,
    private val arrayList: List<CategoryItem>,
    private val categoryAdapterListener: CategoryAdapterListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var previousSelectedItem = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = arrayList[position]
        holder.imageView.setImageResource(currentItem.categoryIcon)
        holder.textView.text = currentItem.categoryName

        selectDeselectCategory(currentItem, holder, position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.category_image)
        val textView: TextView = itemView.findViewById(R.id.category_name)
        val categoryItem: ConstraintLayout = itemView.findViewById(R.id.category_item_box)
    }

    private fun selectDeselectCategory(
        currentItem: CategoryItem,
        holder: CategoryViewHolder,
        position: Int
    ) {
        if (currentItem.isSelected) {
            holder.categoryItem.setBackgroundResource(R.drawable.category_box_shape_selected)
        } else {
            holder.categoryItem.setBackgroundResource(R.drawable.category_box_shape)
        }

        holder.categoryItem.setOnClickListener {
            if (currentItem.isSelected) {
                currentItem.isSelected = false
                holder.categoryItem.setBackgroundResource(R.drawable.category_box_shape)
            } else {
                currentItem.isSelected = true
                holder.categoryItem.setBackgroundResource(R.drawable.category_box_shape_selected)
            }

            if (previousSelectedItem != -1) {
                arrayList[previousSelectedItem].isSelected = false
                notifyItemChanged(previousSelectedItem, true)
            }
            previousSelectedItem = position

            categoryAdapterListener.onCategoryClick(currentItem)
        }
    }
}