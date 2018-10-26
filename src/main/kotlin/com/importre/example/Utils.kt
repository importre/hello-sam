package com.importre.example

import com.google.gson.Gson
import kotlin.reflect.KClass

val gson by lazy { Gson() }

fun <T> T?.json(): String = gson.toJson(this)

fun <T : Any> String?.from(kClass: KClass<T>): T? = gson.fromJson(this, kClass.java)
