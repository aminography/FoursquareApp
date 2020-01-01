package com.aminography.foursquareapp.data.datasource.remote.webservice.base

import com.aminography.foursquareapp.data.datasource.remote.webservice.IWebService
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.util.concurrent.TimeUnit

class WebServiceFactory {

    companion object {

        /**
         * Creates a concrete instance of [IWebService] using [Retrofit].
         *
         * @param endPointUrl the url of webservice endpoint
         * @return an instance of [IWebService]
         */
        fun create(endPointUrl: String): IWebService =
            createAdapter(
                endPointUrl
            ).create(IWebService::class.java)

        private fun createAdapter(baseUrl: String): Retrofit =
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(createHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build()

        private fun createHttpClient(): OkHttpClient =
            OkHttpClient.Builder().apply {
                connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
                readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
                cookieJar(JavaNetCookieJar(CookieManager()))
                interceptors().add(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }.build()
    }
}