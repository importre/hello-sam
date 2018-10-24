package com.importre.example

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent as RequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent as ResponseEvent

class Hello : RequestHandler<RequestEvent, ResponseEvent> {

    override fun handleRequest(
        input: RequestEvent?,
        context: Context?
    ): ResponseEvent = ResponseEvent()
        .apply {
            val name = input?.queryStringParameters?.get("name") ?: "SAM"
            val response = Response(message = "Hello, ${name.capitalize()}!")

            statusCode = 200
            headers = mapOf("Content-Type" to "application/json; charset=utf-8")
            body = response.json()
        }
}
