package com.aminography.foursquareapp.data.datasource.local.db.base

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aminography.foursquareapp.data.datasource.local.db.AppDatabase
import com.aminography.foursquareapp.data.datasource.local.db.DATABASE_NAME
import org.jetbrains.anko.doAsync
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * @author aminography
 */
open class DatabaseInstanceBuilder {

    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (instance == null) {
            synchronized(AppDatabase::class.java) {
                if (instance == null) {
                    instance = buildDatabase(
                        context.applicationContext,
                        Executors.newSingleThreadExecutor()
                    )
                    doAsync {
                        print(instance)
                    }
                    instance?.updateDatabaseCreated(context.applicationContext)
                }
            }
        }
        return instance!!
    }

    /**
     * Build the database. [RoomDatabase.Builder.build] only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private fun buildDatabase(appContext: Context, executor: Executor): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java,
            DATABASE_NAME
        )
            .addCallback(object : RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    executor.execute {
                        // Generate the data for pre-population
                        val database = getInstance(appContext)
                        // notify that the database was created and it's ready to be used
                        database.setDatabaseCreated()
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

}