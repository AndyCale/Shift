package com.example.shift

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.shift.databinding.ActivityMainBinding
import com.google.gson.JsonObject
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw IllegalStateException("Binding in Main Activity must not be null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            tv1.setOnClickListener {
                fullInform(1)
            }
        }

        pep {
            binding.tv1.text = it
        }
    }

    fun fullInform(number : Int) {
        val intent = Intent(this@MainActivity, FullInformation::class.java)
        intent.putExtra(FullInformation.number, 1)
        startActivity(intent)
    }

    private fun createNewPeople() : String {
        val request : ExecutorService = Executors.newFixedThreadPool(1)
        val result = request.submit(Callable<String> {
            var now = JSONObject(URL("https://randomuser.me/api/").readText())
            now = now.getJSONArray("result")[0] as JSONObject
            now.getJSONObject("name").getString("first")
        })
        return result.toString()
    }

    private fun pep(callback : (String) -> Unit) {
        thread {
            var now = JSONObject(URL("https://randomuser.me/api/").readText())
            now = now.getJSONArray("results")[0] as JSONObject
            callback.invoke(now.getJSONObject("name").getString("first"))
        }
    }
}
