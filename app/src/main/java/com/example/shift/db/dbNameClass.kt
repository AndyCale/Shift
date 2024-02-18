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


/*
@Entity(
    tableName = "infoPeople",
    indices = [Index("id")])
data class InfoPeopleDBEntity(
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
    @Insert(entity = InfoPeopleDBEntity::class)
    fun insertNewInfoPeopleData(info: InfoPeopleDBEntity)

    @Query("SELECT * FROM infoPeople")
    fun getAllInfoPeopleData() : List<InfoPeopleDBEntity>

    @Query("DELETE FROM infoPeople")
    fun deleteInfoPeopleData()

    object Dependencies {
        private lateinit var applicationContext: Context

        fun init(context: Context) {
            applicationContext = context
        }

        private val appDatabase: AppDatabasa by lazy {
            Room.databaseBuilder(applicationContext, AppDatabasa::class.java, "database.db")
                .createFromAsset("room_article.db").build()
        }
    }
}

@Database(
    version = 1,
    entities = [InfoPeopleDBEntity::class]
)
abstract  class AppDatabasa : RoomDatabase() {
    abstract fun getInfoPeopleDao() : InfoPeopleDao
}

data class InfoPeople(
    val id: Int,
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
) {
    fun toInfoPeopleDBEntity() : InfoPeopleDBEntity = InfoPeopleDBEntity(
        id = 0,
        title = title,
        name = name,
        lastname = lastname,
        gender = gender,
        country = country,
        city = city,
        street = street,
        numHome = numHome,
        offset = offset,
        phone = phone,
        email = email,
        postcode = postcode,
        dob = dob,
        age = age,
        pic = pic
    )
}

class InfoPeopleRepository(private val InfoPeopleDao : InfoPeopleDao) {

    suspend fun insertNewInfoPeopleData(InfoPeopleDBEntity : InfoPeopleDBEntity) {
        withContext(Dispatchers.IO) {
            InfoPeopleDao.insertNewInfoPeopleData(InfoPeopleDBEntity)
        }
    }

    suspend fun getAllInfoPeopleData() : List<InfoPeopleDBEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext InfoPeopleDao.getAllInfoPeopleData()
        }
    }

    suspend fun removeInfoPeopleData() {
        withContext(Dispatchers.IO) {
            InfoPeopleDao.deleteInfoPeopleData()
        }
    }
}
*/