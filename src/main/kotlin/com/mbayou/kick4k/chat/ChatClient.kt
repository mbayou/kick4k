package com.mbayou.kick4k.chat

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import com.mbayou.kick4k.authorization.AuthorizationClient
import java.net.http.HttpClient

class ChatClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
    authorization: AuthorizationClient,
) : ApiClient(httpClient, mapper, configuration, authorization) {

    fun postChatMessage(request: PostChatMessageRequest): PostChatMessageResponse? {
        return post(configuration.chat)
            .body(request)
            .send(responseType<PostChatMessageResponse>())
    }
}
