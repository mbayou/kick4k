package com.mbayou.kick4k.chat

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import java.net.http.HttpClient

class ChatClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
): ApiClient(httpClient, mapper, configuration) {

    fun postChatMessage(accessToken: String, request: PostChatMessageRequest): PostChatMessageResponse? {
        return post(configuration.chat)
            .body(request)
            .withAccessToken(accessToken)
            .send(responseType<PostChatMessageResponse>())
    }
}
