package com.example.spendwithbrain.screens.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
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
import com.example.spendwithbrain.screens.main.fragments.BudgetFragment
import com.example.spendwithbrain.screens.main.fragments.ExpensesFragment
import com.example.spendwithbrain.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var toolbar: Toolbar
    private lateinit var navigationDrawer: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var btnAddAction: ImageView
    private lateinit var sideMenuNavigationView: NavigationView
    private lateinit var btnLogout: TextView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var userName: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        sharedPreferences = getSharedPreferences(Constants.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        btnAddAction = findViewById(R.id.button_add_action)
        btnAddAction.setOnClickListener(addActionOnClickListener)

        sideMenuNavigationView = findViewById(R.id.home_nav_view)
        sideMenuNavigationView.setNavigationItemSelectedListener(this)

        btnLogout = findViewById(R.id.logout_btn)
        btnLogout.setOnClickListener(logoutOnClickListener)

        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            BudgetFragment()
        ).commit()
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.home_tool_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = getString(R.string.my_budget)
        toolbar.setTitleTextAppearance(this, R.style.HomeToolbarFont)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initNavigationDrawer() {
        val header = (findViewById<NavigationView>(R.id.home_nav_view)).getHeaderView(0)
        userName = header.findViewById(R.id.side_menu_user_name)
        if(sharedPreferences.contains(Constants.USER_NAME)){
            userName.text = sharedPreferences.getString(Constants.USER_NAME, "")
        }

        navigationDrawer = findViewById(R.id.home_drawer_layout)
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
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
    }

    private val addActionOnClickListener = View.OnClickListener {
        val intent = Intent(this, AddActionActivity::class.java)
        startActivity(intent)
    }

    private val logoutOnClickListener = View.OnClickListener {
        editor.clear()
        editor.commit()
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
                    selectedFragment = BudgetFragment()
                    toolbar.title = resources.getString(R.string.my_budget)
                }
                R.id._bottom_nav_expenses -> {
                    selectedFragment = ExpensesFragment()
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