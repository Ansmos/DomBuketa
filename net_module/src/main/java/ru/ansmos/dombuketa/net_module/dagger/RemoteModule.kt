package ru.ansmos.dombuketa.net_module.dagger

import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ansmos.dombuketa.net_module.ApiConstants
import ru.ansmos.dombuketa.net_module.api.IDomBuketaApi2
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RemoteModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        //Создаём кастомный клиент
        OkHttpClient.Builder()
            //Настраиваем таймауты для медленного интернета
            .callTimeout(ApiConstants.NETWORKTIMEOUT, TimeUnit.SECONDS)
            .readTimeout(ApiConstants.NETWORKTIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(ApiConstants.NETWORKTIMEOUT, TimeUnit.SECONDS)
            //Добавляем логгер
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        //Создаем Ретрофит
        Retrofit.Builder()
            //Указываем базовый URL из констант
            .baseUrl(ru.ansmos.dombuketa.net_module.ApiConstants.BASE_URL)
            //Добавляем конвертер
            .addConverterFactory(GsonConverterFactory.create())
            //Добавляем кастомный клиент
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideRetrofirService(retrofit: Retrofit): IDomBuketaApi2 = retrofit.create(IDomBuketaApi2::class.java)
}