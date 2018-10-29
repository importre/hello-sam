package com.importre.example

import com.google.gson.Gson
import kotlin.reflect.KClass
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent as ResponseEvent

val gson by lazy { Gson() }

fun <T> T?.json(): String = gson.toJson(this)

fun <T : Any> String?.from(
    kClass: KClass<T>
): T? = gson.fromJson(this, kClass.java)

internal fun makeGreeting(
    name: String
): String = "Hello, ${name.capitalize()}!"

internal fun makeResponseEvent(message: String) = ResponseEvent()
    .apply {
        statusCode = 200
        headers = mapOf("Content-Type" to "application/json; charset=utf-8")
        body = Response(message = message).json()
    }
