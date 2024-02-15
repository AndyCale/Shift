package com.example.shift

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FullInformation : AppCompatActivity() {

    companion object {
        const val number = "number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_information)
    }

    fun getNumber() {
        val people = intent.getIntExtra(number, 0)
    }
}