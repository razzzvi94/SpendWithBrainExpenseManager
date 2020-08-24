package com.example.spendwithbrain.screens.addaction

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spendwithbrain.R
import com.example.spendwithbrain.db.RoomDb
import com.example.spendwithbrain.db.entities.ExpensesDetails
import com.example.spendwithbrain.db.entities.IncomeDetails
import com.example.spendwithbrain.models.CategoryItem
import com.example.spendwithbrain.screens.addaction.adapter.CategoryAdapter
import com.example.spendwithbrain.screens.addaction.adapter.CategoryAdapterListener
import com.example.spendwithbrain.utils.Constants
import com.example.spendwithbrain.utils.SharedPrefUtils
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


class AddActionActivity : AppCompatActivity(), CategoryAdapterListener {
    private lateinit var toolbar: Toolbar
    private lateinit var categoryGrid: RecyclerView
    private lateinit var categoryArrayList: List<CategoryItem>
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var dateEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var detailsText: EditText
    private lateinit var detailsImage: ImageView
    private lateinit var loadImageBtn: ImageView
    private lateinit var deleteImage: ImageView
    private lateinit var saveButton: TextView
    private lateinit var categorySelected: String
    private lateinit var fragmentTitle: String
    private var userId: Int = -1
    private var gmtTimestamp: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_action)

        initComponents()
        initToolbar()
        initDatePicker()
        initCategoryGrid()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE) {
            detailsImage.setBackgroundResource(android.R.color.transparent)
            detailsImage.setImageURI(data?.data)
        }
    }

    override fun onCategoryClick(category: CategoryItem) {
        categorySelected = category.categoryName
    }

    private fun initComponents() {
        SharedPrefUtils.init(applicationContext)
        if (SharedPrefUtils.hasKey(Constants.USER_ID)) {
            userId = SharedPrefUtils.read(Constants.USER_ID, -1)!!
        }

        dateEditText = findViewById(R.id.date_EditText)
        amountEditText = findViewById(R.id.amount_EditText)
        detailsText = findViewById(R.id.details_EditText)
        detailsImage = findViewById(R.id.details_ImageView)
        loadImageBtn = findViewById(R.id.add_image)
        deleteImage = findViewById(R.id.delete)
        saveButton = findViewById(R.id.button_save)

        loadImageBtn.setOnClickListener(loadImageOnClickListener)
        detailsImage.setOnClickListener(loadImageOnClickListener)
        deleteImage.setOnClickListener(deleteImageOnClickListener)
        saveButton.setOnClickListener(saveOnClickListener)
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = getString(R.string.add_action)
        toolbar.setTitleTextAppearance(this, R.style.HomeToolbarFont)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val min = c.get(Calendar.MINUTE)

        dateEditText.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    dateEditText.setText(mYear.toString() + "-" + mMonth.toString() + "-" + mDay.toString() + " at " + hour + ":" + min)
                    gmtTimestamp = c.run {
                        c.set(Calendar.YEAR, mYear)
                        c.set(Calendar.MONTH, mMonth)
                        c.set(Calendar.DAY_OF_MONTH, mDay)
                        timeInMillis
                    }
                },
                year,
                month,
                day
            )
            dpd.show()
            dpd.datePicker.maxDate = System.currentTimeMillis();
        }
    }

    private fun initCategoryGrid() {
        categoryGrid = findViewById(R.id.grid_recycler)
        val numberOfColumns = 4
        categoryArrayList = setCategoryData()
        categoryGrid.layoutManager = GridLayoutManager(this, numberOfColumns)
        categoryGrid.adapter = CategoryAdapter(this, categoryArrayList, this)
        categoryGrid.setHasFixedSize(true)
    }

    private fun setCategoryData(): List<CategoryItem> {
        val arrayList: ArrayList<CategoryItem> = ArrayList()
//        val categoryString = arrayOf(resources.getStringArray(R.array.category_strings))
//        val categoryIcons = resources.obtainTypedArray(R.array.category_icons)
//        for(i in categoryString.indices){
//            arrayList.add(CategoryItem(categoryIcons.getResourceId(i, -1), categoryString[i].toString()))
//        }
        arrayList.add(CategoryItem(R.drawable.ic_category_income, "Income", false))
        arrayList.add(CategoryItem(R.drawable.ic_category_food, "Food", false))
        arrayList.add(CategoryItem(R.drawable.ic_category_car, "Car", false))
        arrayList.add(CategoryItem(R.drawable.ic_category_clothes, "Clothes", false))
        arrayList.add(CategoryItem(R.drawable.ic_category_savings, "Savings", false))
        arrayList.add(CategoryItem(R.drawable.ic_category_health, "Health", false))
        arrayList.add(CategoryItem(R.drawable.ic_category_beauty, "Beauty", false))
        arrayList.add(CategoryItem(R.drawable.ic_category_travel, "Travel", false))
        return arrayList
    }

    private val loadImageOnClickListener = View.OnClickListener {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Constants.REQUEST_CODE)
    }

    private val deleteImageOnClickListener = View.OnClickListener {
        detailsImage.setImageResource(android.R.color.transparent)
        detailsImage.setBackgroundResource(R.drawable.image_placeholder)
    }

    private val saveOnClickListener = View.OnClickListener {
        var defaultDetailsText: String = "";
        var defaultDetailsImage: Drawable = resources.getDrawable(android.R.color.transparent)
        Thread {
            if (detailsText.text.toString() != null) {
                defaultDetailsText = detailsText.text.toString()
            }
            if (detailsImage.drawable != null) {
                defaultDetailsImage = detailsImage.drawable
            }
            if (categorySelected == resources.getString(R.string.income) && SharedPrefUtils.hasKey(
                    Constants.USER_ID
                )
            ) {
                val objDetails = IncomeDetails(
                    userId = userId,
                    incomeDate = gmtTimestamp,
                    incomeAmount = amountEditText.text.toString().toLong(),
                    incomeCategory = categorySelected,
                    incomeDetails = defaultDetailsText,
                    incomeImage = imageToBitmap(detailsImage)
                )

                RoomDb.db.userDetailsDAO().insertOrUpdateIncome(objDetails)
                RoomDb.db.incomeDetailsDAO()
                    .updateUserBudget(userId, amountEditText.text.toString().toLong())
            } else {
                if (SharedPrefUtils.hasKey(Constants.USER_ID)) {
                    val objDetails = ExpensesDetails(
                        userId = userId,
                        expensesDate = gmtTimestamp,
                        expensesAmount = amountEditText.text.toString().toLong(),
                        expensesCategory = categorySelected,
                        expensesDetails = defaultDetailsText,
                        expensesImage = imageToBitmap(detailsImage)
                    )

                    RoomDb.db.userDetailsDAO().insertOrUpdateExpense(objDetails)
                    RoomDb.db.expenseDetailsDAO()
                        .updateUserExpense(userId, amountEditText.text.toString().toLong())
                }
            }
        }.start()

        if (categorySelected == resources.getString(R.string.income)) {
            Toast.makeText(this, getString(R.string.budget_saved), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.expense_saved), Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun imageToBitmap(image: ImageView): ByteArray {
        var returnArray = ByteArray(0)
        if (image.drawable != null) {
            val bitmap = (image.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            returnArray = stream.toByteArray()
        }
        return returnArray
    }
}