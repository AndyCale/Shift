package com.example.shift

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shift.FullInformation.Companion.number
import com.example.shift.databinding.ActivityFullInformationBinding
import com.example.shift.databinding.ActivityMainBinding
import kotlin.concurrent.thread
import com.example.shift.MainActivity
import com.squareup.picasso.Picasso

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
        if (number != null) {
            binding.nameInFullInfo.text = number[0]
            binding.fullInfo.text = number[1]
            Picasso.get().load(number[2]).into(binding.imgInFullInfo)
        }

        binding.btnBack.setOnClickListener{
            finish()
        }
    }

    fun getNumber() : Array<out String>? {
        val numPeople = intent.getStringArrayExtra(number)
        return numPeople
    }
}