package dev.ashutoshwahane.matchmate.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ashutoshwahane.matchmate.data.cloud.MatchMateAPI
import dev.ashutoshwahane.matchmate.data.repositoryImpl.ProfileRepositoryImpl
import dev.ashutoshwahane.matchmate.domain.repository.ProfileRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://randomuser.me/"


    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context): MatchMateAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                    .addInterceptor(ChuckerInterceptor(context))
                    .build()
            )
            .build()
            .create(MatchMateAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideImageRepository(repositoryImpl: ProfileRepositoryImpl): ProfileRepository {
        return repositoryImpl
    }
}