package com.example.spendwithbrain.screens.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.spendwithbrain.R
import com.example.spendwithbrain.screens.addaction.AddActionActivity
import com.example.spendwithbrain.screens.converter.ConverterActivity
import com.example.spendwithbrain.screens.login.LoginActivity
import com.example.spendwithbrain.screens.main.fragments.budget.BudgetFragment
import com.example.spendwithbrain.screens.main.fragments.expenses.ExpensesFragment
import com.example.spendwithbrain.utils.Constants
import com.example.spendwithbrain.utils.SharedPrefUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.home_nav_header.view.*
import kotlinx.android.synthetic.main.home_toolbar.view.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var layout: View
    private lateinit var toolbar: Toolbar
    private lateinit var navigationDrawer: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var userName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layout = View.inflate(this, R.layout.activity_main, null)
        setContentView(layout)
        initComponents()
        initToolbar()
        initNavigationDrawer()
        initBottomNavigationView()
    }

    @Override
    override fun onBackPressed() {
        if (navigationDrawer.isDrawerOpen(GravityCompat.START)) {
            navigationDrawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    @Override
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> navigationDrawer.closeDrawers()
            R.id.nav_converter -> navigateToConverter()
        }
        navigationDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initComponents() {
        layout.button_add_action.setOnClickListener(addActionOnClickListener)
        layout.home_nav_view.setNavigationItemSelectedListener(this)
        layout.logout_btn.setOnClickListener(logoutOnClickListener)

        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            BudgetFragment()
        ).commit()
    }

    private fun initToolbar() {
        toolbar = layout.home_tool_bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = getString(R.string.my_budget)
        toolbar.setTitleTextAppearance(this, R.style.HomeToolbarFont)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initNavigationDrawer() {
        val header = layout.home_nav_view.getHeaderView(0)
        userName = header.side_menu_user_name
        if (SharedPrefUtils.hasKey(Constants.USER_NAME)) {
            userName.text = SharedPrefUtils.read(Constants.USER_NAME, "")
        }

        navigationDrawer = layout.home_drawer_layout
        actionBarToggle = ActionBarDrawerToggle(
            this,
            navigationDrawer,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        navigationDrawer.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()
    }

    private fun initBottomNavigationView() {
        layout.bottom_navigation.setOnNavigationItemSelectedListener(navListener)
    }

    private val addActionOnClickListener = View.OnClickListener {
        val intent = Intent(this, AddActionActivity::class.java)
        startActivity(intent)
    }

    private val logoutOnClickListener = View.OnClickListener {
        SharedPrefUtils.clear()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToConverter() {
        val intent = Intent(this, ConverterActivity::class.java)
        startActivity(intent)
    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.bottom_nav_budget -> {
                    selectedFragment =
                        BudgetFragment()
                    toolbar.title = resources.getString(R.string.my_budget)
                }
                R.id._bottom_nav_expenses -> {
                    selectedFragment =
                        ExpensesFragment()
                    toolbar.title = resources.getString(R.string.my_expenses)
                }
            }
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                selectedFragment!!
            ).commit()
            true
        }
}