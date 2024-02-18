package com.example.shift

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.shift.databinding.ActivityMainBinding
import com.example.shift.db.InfoPeopleDB
import com.example.shift.db.InfoPeopleEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw IllegalStateException("Binding in Main Activity must not be null")

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var db: InfoPeopleDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = InfoPeopleDB.getInfoPeopleDB(this)



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

                Thread {
                    db.getInfoPeopleDao().deleteInfoPeopleData()
                }.start()
                newPeople()
            }
        }

        Thread {
            val countStr = db.getInfoPeopleDao().countDB()

            if (countStr == 0) { // если база данных пустая
                newPeople()
            }
            else { // если не пустая
                loadFromDBInfo() {
                    Picasso.get().load(it[0]).into(binding.imgV1)
                    Picasso.get().load(it[1]).into(binding.imgV2)
                    Picasso.get().load(it[2]).into(binding.imgV3)
                    Picasso.get().load(it[3]).into(binding.imgV4)
                    Picasso.get().load(it[4]).into(binding.imgV5)
                }
            }
        }.start()
    }

    private fun fullInform(number : Int) {
        var inform : Array<String>
        getInfoSelectedPeople(number - 1) {
            inform = it
            val intent = Intent(this@MainActivity, FullInformation::class.java)
            intent.putExtra(FullInformation.number, inform)
            startActivity(intent)
        }
    }

    private fun newPeople() {
        var inform: String = ""
        getInfo {
            inform = "Name: ${it[0]} ${it[1]}\nAddress: ${it[2]} ${it[3]}\nPhone: ${it[4]}"
            binding.tv1.text = inform
            Picasso.get().load(it[5]).into(binding.imgV1)
        }

        getInfo {
            inform = "Name: ${it[0]} ${it[1]}\nAddress: ${it[2]} ${it[3]}\nPhone: ${it[4]}"
            binding.tv2.text = inform
            Picasso.get().load(it[5]).into(binding.imgV2)
        }

        getInfo {
            inform = "Name: ${it[0]} ${it[1]}\nAddress: ${it[2]} ${it[3]}\nPhone: ${it[4]}"
            binding.tv3.text = inform
            Picasso.get().load(it[5]).into(binding.imgV3)
        }

        getInfo {
            inform = "Name: ${it[0]} ${it[1]}\nAddress: ${it[2]} ${it[3]}\nPhone: ${it[4]}"
            binding.tv4.text = inform
            Picasso.get().load(it[5]).into(binding.imgV4)
        }

        getInfo {
            inform = "Name: ${it[0]} ${it[1]}\nAddress: ${it[2]} ${it[3]}\nPhone: ${it[4]}"
            binding.tv5.text = inform
            Picasso.get().load(it[5]).into(binding.imgV5)
        }
    }

    private fun getInfo(callback : (Array<String>) -> Unit) {
        thread {
            var flag = false
            while (!flag) {
                try {
                    var request = JSONObject(URL("https://randomuser.me/api/").readText())
                    flag = true
                    request = request.getJSONArray("results")[0] as JSONObject
                    val name = request.getJSONObject("name").getString("first")
                    val lastname = request.getJSONObject("name").getString("last")
                    val street = request.getJSONObject("location").
                    getJSONObject("street").getString("name")
                    val numHome = request.getJSONObject("location").
                    getJSONObject("street").getString("number")
                    val pic = request.getJSONObject("picture").getString("medium")
                    val phone = request.getString("phone")

                    val gender = request.getString("gender")
                    val title = request.getJSONObject("name").getString("title")
                    val country = request.getJSONObject("location").getString("country")
                    val city = request.getJSONObject("location").getString("city")
                    val postcode = request.getJSONObject("location").
                    getString("postcode")
                    val email = request.getString("email")
                    val offset = request.getJSONObject("location").
                    getJSONObject("timezone").getString("offset")
                    val dob = request.getJSONObject("dob").getString("date")
                    val age = request.getJSONObject("dob").getString("age")


                    addToDB(arrayOf(title, name, lastname, gender, country, city, street, numHome,
                        offset, phone, email, postcode, dob, age, pic))

                    handler.post {
                        callback.invoke(arrayOf(name, lastname, street, numHome, phone, pic))
                    }

                } catch (err: java.io.FileNotFoundException) {
                    Log.i(TAG, "FileNotFoundException")
                }
            }
        }
    }

    private fun addToDB(info : Array<String>) {
        val people = InfoPeopleEntity(0, info[0], info[1], info[2], info[3], info[4], info[5],
            info[6], info[7].toInt(), info[8], info[9], info[10], info[11], info[12],
            info[13].toInt(), info[14])

        Thread {
            db.getInfoPeopleDao().insertInfoPeople(people)
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun loadFromDBInfo(callback : (List<String>) -> Unit) {
        thread {
            var listPic = emptyList<String>()
            val peoples = db.getInfoPeopleDao().getAllInfoPeople()
            var people = peoples[0]

            with(binding) {
                tv1.text = "Name: ${people.name} ${people.lastname}\n" +
                        "Address: ${people.street} ${people.numHome}\n" +
                        "Phone: ${people.phone}"
                listPic = listPic + people.pic

                people = peoples[1]
                tv2.text = "Name: ${people.name} ${people.lastname}\n" +
                        "Address: ${people.street} ${people.numHome}\n" +
                        "Phone: ${people.phone}"
                listPic = listPic + people.pic

                people = peoples[2]
                tv3.text = "Name: ${people.name} ${people.lastname}\n" +
                        "Address: ${people.street} ${people.numHome}\n" +
                        "Phone: ${people.phone}"
                listPic = listPic + people.pic

                people = peoples[3]
                tv4.text = "Name: ${people.name} ${people.lastname}\n" +
                        "Address: ${people.street} ${people.numHome}\n" +
                        "Phone: ${people.phone}"
                listPic = listPic + people.pic

                people = peoples[4]
                tv5.text = "Name: ${people.name} ${people.lastname}\n" +
                        "Address: ${people.street} ${people.numHome}\n" +
                        "Phone: ${people.phone}"
                listPic = listPic + people.pic
            }
            handler.post {callback.invoke(listPic)}
        }
    }

    private fun getInfoSelectedPeople(number : Int, callback : (Array<String>) -> Unit)  {
        thread {
            val people = db.getInfoPeopleDao().getAllInfoPeople()[number]
            val name = "Name: ${people.title} ${people.name} ${people.lastname}"
            val info = "Gender: ${people.gender}\n\n" +
                    "Address: ${people.country}, ${people.city}, ${people.street} ${people.numHome}\n\n" +
                    "Time zone: ${people.offset}\n\n" +
                    "Phone: ${people.phone}\n\n" +
                    "Email: ${people.email}\n\n" +
                    "Postcode: ${people.postcode}\n\n" +
                    "Day of Birthday: ${people.dob}\n\n" +
                    "Age: ${people.age}"
            handler.post {
                callback.invoke(arrayOf(name, info, people.pic))
            }
        }
    }
}
