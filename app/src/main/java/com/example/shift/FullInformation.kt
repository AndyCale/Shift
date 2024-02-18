package com.example.shift

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shift.databinding.ActivityFullInformationBinding
import com.example.shift.databinding.ActivityMainBinding
import kotlin.concurrent.thread
import com.example.shift.MainActivity

class FullInformation : AppCompatActivity() {

    private var _binding: ActivityFullInformationBinding? = null
    private val binding: ActivityFullInformationBinding
        get() = _binding ?: throw IllegalStateException("Binding in Main Activity must not be null")


    companion object {
        const val number = "number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFullInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val number = getNumber()


    }

    fun getNumber() : Int {
        val numPeople = intent.getIntExtra(number, 0)
        return numPeople
    }
}