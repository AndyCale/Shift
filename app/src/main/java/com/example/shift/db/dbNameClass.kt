package com.example.shift.db

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Database(entities = [InfoPeopleEntity::class], version = 1)
abstract class InfoPeopleDB : RoomDatabase() {

    abstract fun getInfoPeopleDao(): InfoPeopleDao
    companion object{
        fun getInfoPeopleDB(context: Context): InfoPeopleDB { //создается база данных, если еще не создана
            return Room.databaseBuilder(            // возвращается база данных
                context.applicationContext,
                InfoPeopleDB::class.java,
                "info.db"
            ).build()
        }
    }
}

@Entity (tableName = "infoPeople")
data class InfoPeopleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val name: String,
    val lastname: String,
    val gender: String,
    val country: String,
    val city: String,
    val street: String,
    val numHome: Int,
    val offset: String,
    val phone: String,
    val email: String,
    val postcode: String,
    val dob: String,
    val age: Int,
    val pic: String
)

@Dao
interface InfoPeopleDao {
    @Insert
    fun insertInfoPeople(info: InfoPeopleEntity)
    @Query("SELECT * FROM infoPeople ORDER BY id")
    fun getAllInfoPeople(): List<InfoPeopleEntity>
    @Query("DELETE FROM infoPeople")
    fun deleteInfoPeopleData()
    @Query("SELECT count(*) FROM infoPeople")
    fun countDB(): Int
    @Query("SELECT * FROM infoPeople WHERE id = :idPeople")
    fun getById(idPeople : Int): InfoPeopleEntity
}

