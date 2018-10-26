package com.importre.example

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test

class AppTest {

    private lateinit var app: Hello

    @Before
    fun setUp() {
        app = Hello()
    }

    @Test
    fun successfulResponseWithoutParams() {
        val result = app.handleRequest(null, null)
        assertEquals(result.statusCode, 200)
        assertEquals(
            result.headers["Content-Type"],
            "application/json; charset=utf-8"
        )

        val response = result.body.from(Response::class)
        assertEquals(response?.message, "Hello, SAM!")
    }

    @Test
    fun successfulResponseWithParams() {
        val input = APIGatewayProxyRequestEvent()
            .apply { queryStringParameters = mapOf("name" to "heo") }
        val result = app.handleRequest(input, null)
        assertEquals(result.statusCode, 200)
        assertEquals(
            result.headers["Content-Type"],
            "application/json; charset=utf-8"
        )

        val content = result.body
        assertNotNull(content)

        val response = result.body.from(Response::class)
        assertEquals(response?.message, "Hello, Heo!")
    }
}
