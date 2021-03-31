package com.shivamkumarjha.supaflix.di

import com.shivamkumarjha.supaflix.network.*
import com.shivamkumarjha.supaflix.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun getFcdnCloudRepository(apiFcdnCloud: ApiFcdnCloud): FcdnCloudRepository {
        return FcdnCloudRepositoryImpl(apiFcdnCloud)
    }

    @Provides
    @Singleton
    fun getGocdnCloudRepository(apiGocdnCloud: ApiGocdnCloud): GocdnCloudRepository {
        return GocdnCloudRepositoryImpl(apiGocdnCloud)
    }

    @Provides
    @Singleton
    fun getMovCloudRepository(apiMovCloud: ApiMovCloud): MovCloudRepository {
        return MovCloudRepositoryImpl(apiMovCloud)
    }

    @Provides
    @Singleton
    fun getVidCloudRepository(apiVidCloud: ApiVidCloud): VidCloudRepository {
        return VidCloudRepositoryImpl(apiVidCloud)
    }

    @Provides
    @Singleton
    fun getXmoviesRepository(apiXmovies: ApiXmovies): XmoviesRepository {
        return XmoviesRepositoryImpl(apiXmovies)
    }
}