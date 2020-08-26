package com.example.spendwithbrain.screens.addaction.adapter

import com.example.spendwithbrain.screens.addaction.adapter.models.CategoryItem

interface CategoryAdapterListener {
    fun onCategoryClick(category: CategoryItem)
}