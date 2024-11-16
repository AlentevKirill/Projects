package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.myapplication.databinding.ActivityMainBinding
import kotlin.properties.Delegates.notNull

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState != null) {
            binding.outputField.text = savedInstanceState.getString(KEY_NUMBER, "")
        }
        Log.d(TAG, "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        val mapButton = mapOf(binding.button0 to "0", binding.button1 to "1", binding.button2 to "2",
            binding.button3 to "3", binding.button4 to "4", binding.button5 to "5", binding.button6 to "6",
            binding.button7 to "7", binding.button8 to "8", binding.button9 to "9",
            binding.buttonPoint to ".", binding.buttonAdd to " + ", binding.buttonSub to " - ",
            binding.buttonMul to " * ", binding.buttonDiv to " : ")
        for ((button, value) in mapButton) button.setOnClickListener() {write(value)}
        binding.buttonC.setOnClickListener() {
            binding.outputField.text = ""
        }
        binding.buttonDel.setOnClickListener() {
            val len = binding.outputField.text.length
            val checkE = binding.outputField.text.indexOf('E')
            if (checkE != -1) {
                binding.outputField.text = ""
            } else {
                if (len != 0) {
                    if (binding.outputField.text.last() == ' ') {
                        binding.outputField.text = binding.outputField.text.subSequence(0, len - 3)
                    } else {
                        if (binding.outputField.text.last()
                                .isDigit() || binding.outputField.text.last() == '.'
                        ) {
                            binding.outputField.text =
                                binding.outputField.text.subSequence(0, len - 1)
                        } else {
                            binding.outputField.text = ""
                        }
                    }
                }
            }
        }
        binding.buttonEquality.setOnClickListener() {
            val string = binding.outputField.text
            if (!isValid(string.toString())) {
                binding.outputField.text = "An incorrect expression was entered, please enter an arithmetically correct expression consisting of a single operation"
            } else {
                val flag = string.indexOf(' ')
                if (flag != -1) {
                    val term1 = string.subSequence(0, flag).toString().toDouble()
                    val term2 = string.subSequence(flag + 3, string.length).toString().toDouble()
                    val operation = string[flag + 1]
                    if (operation == '+') {
                        val result = term1 + term2
                        if (result.compareTo(result.toInt()) == 0) {
                            binding.outputField.text = result.toInt().toString()
                        } else {
                            binding.outputField.text = result.toString()
                        }
                    }
                    if (operation == '-') {
                        val result = term1 - term2
                        if (result.compareTo(result.toInt()) == 0) {
                            binding.outputField.text = result.toInt().toString()
                        } else {
                            binding.outputField.text = result.toString()
                        }
                    }
                    if (operation == '*') {
                        val result = term1 * term2
                        if (result.compareTo(result.toInt()) == 0) {
                            binding.outputField.text = result.toInt().toString()
                        } else {
                            binding.outputField.text = result.toString()
                        }
                    }
                    if (operation == ':') {
                        val result = term1 / term2
                        if (result.compareTo(result.toInt()) == 0) {
                            binding.outputField.text = result.toInt().toString()
                        } else {
                            binding.outputField.text = result.toString()
                        }
                    }
                }
            }
        }
        Log.d(TAG, "onResume")
    }

    private fun isValid(string: String): Boolean {
        val leftSpace = string.indexOf(' ')
        val rightSpace = string.lastIndexOf(' ')
        if (leftSpace == -1) {
            return true
        }
        if (rightSpace - leftSpace > 2 || rightSpace == string.length - 1 || leftSpace == 0) {
            return false
        }
        val firstNumber = string.substring(0, leftSpace)
        val secondNumber = string.substring(rightSpace + 1, string.length)
        return isValidNumber(firstNumber) && isValidNumber(secondNumber)
    }

    private fun isValidNumber(number: String): Boolean {
        val firstPoint = number.indexOf('.')
        val secondPoint = number.lastIndexOf('.')
        if (number[0].isLetter() && (firstPoint != -1 || number[number.length - 1].isDigit())) {
            return false
        }
        if (firstPoint == -1) {
            return true
        }
        return !(firstPoint != secondPoint || firstPoint == 0 || firstPoint == number.length - 1)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_NUMBER, binding.outputField.text.toString())
        Log.d(TAG, "onSaveInstanceState")
    }

    private fun write(term : String) {
        binding.outputField.append(term)
    }

    companion object{
        @JvmStatic val TAG = MainActivity::class.simpleName
        @JvmStatic private val KEY_NUMBER = "NUMBER"
    }

}