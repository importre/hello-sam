package com.importre.example

import arrow.core.Option
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent as RequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent as ResponseEvent

class Hello : RequestHandler<RequestEvent, ResponseEvent> {

    override fun handleRequest(
        request: RequestEvent?,
        context: Context?
    ): ResponseEvent = Option
        .fromNullable(request?.queryStringParameters?.get("name"))
        .toEither { "SAM" }
        .fold(::makeGreeting, ::makeGreeting)
        .let(::makeResponseEvent)
}
