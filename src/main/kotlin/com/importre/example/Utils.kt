package com.importre.example

import kotlinx.serialization.json.JSON

inline fun <reified T : Any> T.json(): String = JSON.stringify(this)
