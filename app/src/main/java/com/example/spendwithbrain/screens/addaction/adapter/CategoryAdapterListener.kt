package com.example.spendwithbrain.screens.addaction.adapter

import com.example.spendwithbrain.models.CategoryItem

interface CategoryAdapterListener {
    fun onCategoryClick(category: CategoryItem)
}