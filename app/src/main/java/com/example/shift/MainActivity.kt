package com.example.shift

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.shift.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw IllegalStateException("Binding in Main Activity must not be null")

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            tv1.setOnClickListener {
                fullInform(1)
            }
            tv2.setOnClickListener {
                fullInform(2)
            }
            tv3.setOnClickListener {
                fullInform(3)
            }
            tv4.setOnClickListener {
                fullInform(4)
            }
            tv5.setOnClickListener {
                fullInform(5)
            }
            btnUpdate.setOnClickListener {
                tv1.text = "Загрузка..."
                tv2.text = "Загрузка..."
                tv3.text = "Загрузка..."
                tv4.text = "Загрузка..."
                tv5.text = "Загрузка..."
                imgV1.setImageResource(R.drawable.pic_loading)
                imgV2.setImageResource(R.drawable.pic_loading)
                imgV3.setImageResource(R.drawable.pic_loading)
                imgV4.setImageResource(R.drawable.pic_loading)
                imgV5.setImageResource(R.drawable.pic_loading)

                new_people()
            }
        }

        if (true) {
            new_people()
        }
    }

    private fun fullInform(number : Int) {
        val intent = Intent(this@MainActivity, FullInformation::class.java)
        intent.putExtra(FullInformation.number, 1)
        startActivity(intent)
    }

    private fun new_people() {

        var inform: String = ""
        getInfo {
            if (it.isNotEmpty()) {
                inform = "Name: ${it[0]} ${it[1]}\nAddress: ${it[2]} ${it[3]}\nPhone: ${it[4]}";
                binding.tv1.text = inform
                Picasso.get().load(it[5]).into(binding.imgV1)
            }
        }

        getInfo {
            if (it.isNotEmpty()) {
                inform = "Name: ${it[0]} ${it[1]}\nAddress: ${it[2]} ${it[3]}\nPhone: ${it[4]}";
                binding.tv2.text = inform
                Picasso.get().load(it[5]).into(binding.imgV2)
            }
        }

        getInfo {
            if (it.isNotEmpty()) {
                inform = "Name: ${it[0]} ${it[1]}\nAddress: ${it[2]} ${it[3]}\nPhone: ${it[4]}";
                binding.tv3.text = inform
                Picasso.get().load(it[5]).into(binding.imgV3)
            }
        }

        getInfo {
            if (it.isNotEmpty()) {
                inform = "Name: ${it[0]} ${it[1]}\nAddress: ${it[2]} ${it[3]}\nPhone: ${it[4]}";
                binding.tv4.text = inform
                Picasso.get().load(it[5]).into(binding.imgV4)
            }
        }

        getInfo {
            if (it.isNotEmpty()) {
                inform = "Name: ${it[0]} ${it[1]}\nAddress: ${it[2]} ${it[3]}\nPhone: ${it[4]}";
                binding.tv5.text = inform
                Picasso.get().load(it[5]).into(binding.imgV5)
            }
        }
    }

    private fun getInfo(callback : (List<String>) -> Unit) {
        var flag = false
        thread {
            while (!flag) {
                try {
                    var request = JSONObject(URL("https://randomuser.me/api/").readText())
                    request = request.getJSONArray("results")[0] as JSONObject
                    val name = request.getJSONObject("name")
                    val address = request.getJSONObject("location").getJSONObject("street")
                    val pic = request.getJSONObject("picture").getString("medium")
                    flag = true
                    handler.post {
                        callback.invoke(
                            listOf(
                                name.getString("first"), name.getString("last"),
                                address.getString("name"), address.getString("number"),
                                request.getString("phone"), pic
                            )
                        )
                    }
                } catch (err: java.io.FileNotFoundException) {
                    Log.i(TAG, "FileNotFoundException")
                    handler.post { listOf<String>() }
                }
            }
        }
    }
}
