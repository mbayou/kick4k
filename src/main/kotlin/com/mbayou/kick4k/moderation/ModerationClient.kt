package com.mbayou.kick4k.moderation

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import java.net.http.HttpClient

class ModerationClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
): ApiClient(httpClient, mapper, configuration) {

    fun postModerationBans(accessToken: String, request: PostModerationBansRequest) {
        post(configuration.moderation)
            .body(request)
            .withAccessToken(accessToken)
            .send(responseType<Unit>())
    }

    fun deleteModerationBans(accessToken: String, request: DeleteModerationBansRequest) {
        delete(configuration.moderation)
            .body(request)
            .withAccessToken(accessToken)
            .send(responseType<Unit>())
    }

    fun deleteModerationBans(accessToken: String, broadcasterUserId: Int, userId: Int) {
        deleteModerationBans(accessToken, DeleteModerationBansRequest(broadcasterUserId, userId))
    }
}
