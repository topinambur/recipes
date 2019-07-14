/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.martianlab.drunkennavigation.di

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import com.android.example.github.util.LiveDataCallAdapterFactory
import com.martianlab.drunkennavigation.data.db.RecipesDb
import com.martianlab.drunkennavigation.data.db.UserDao
import com.martianlab.drunkennavigation.domain.RecipesService
import com.martianlab.drunkennavigation.model.tools.AppExecutors
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideDNaviService(): RecipesService {
        return Retrofit.Builder()
            .baseUrl("http://example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(RecipesService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): RecipesDb {
        return Room
            .databaseBuilder(app, RecipesDb::class.java, "recipes.db")
            .fallbackToDestructiveMigration()
            .build()
    }


    @Singleton
    @Provides
    fun provideUserDao(db: RecipesDb): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideExecutors() : AppExecutors = AppExecutors()


    @Singleton
    @Provides
    fun providePreferences( app: Application ) : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)


}
