package com.example.tip_calc

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.tip_calc.databinding.ActivityMainBinding
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //This line declares a top-level variable in the class for the binding object.
    // It's defined at this level because it will be used across multiple methods in MainActivity class
    //lateinit = It's a promise that your code will initialize the variable before using it

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Instead of calling findViewById() for each View in your app, you'll create and initialize a binding object once.
        //one binding object has reference for all views with an ID- ActivityMainBinding
        binding = ActivityMainBinding.inflate(layoutInflater)//initializes the binding object
        setContentView(binding.root)    //Instead of passing the resource ID of the layout, R.layout.activity_main,
        // this specifies the root of the hierarchy of views in your app, binding.root
        //old way: val myButton: Button = findViewById(R.id.my_button)
        //best way without using variable
        binding.calculateButton.setOnClickListener { calculateTip() }

        // Set up a key listener on the EditText field to listen for "enter" button presses
        binding.textInput1.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(view, keyCode)
        }

    }
    
    // hides the onscreen keyboard if the keyCode input parameter is equal to KeyEvent.KEYCODE_ENTER
    //The InputMethodManager controls if a soft keyboard is shown, hidden,
    // and allows the user to choose which soft keyboard is displayed.
    // The method returns true if the key event was handled, and returns false otherwise.
    private fun handleKeyEvent(view: View?, keyCode: Int):Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken,0)
            }
            return true
        }
        return false
    }

    private fun calculateTip() {
        val stringInTextField = binding.textInput1.text.toString()
        val cost = stringInTextField.toDoubleOrNull()
        val tipPercentage = when (binding.tipOption.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }
        if (cost == null || cost == 0.0) {
            //binding.tipResult.text = ""
            displayTip(0.0)
            return
        }
        var tip = tipPercentage * cost
        if (binding.roundUpSwitch.isChecked) {
            tip = kotlin.math.ceil(tip)
        }
        displayTip(tip)
    }

    private fun displayTip(tip: Double) {
        val formattedTip = NumberFormat.getCurrencyInstance(Locale.US).format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)

    }
}