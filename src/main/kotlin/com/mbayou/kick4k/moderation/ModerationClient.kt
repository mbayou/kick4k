package com.mbayou.kick4k.moderation

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import com.mbayou.kick4k.authorization.AuthorizationClient
import java.net.http.HttpClient

class ModerationClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
    authorization: AuthorizationClient,
) : ApiClient(httpClient, mapper, configuration, authorization) {

    fun postModerationBans(request: PostModerationBansRequest) {
        post(configuration.moderation)
            .body(request)
            .send(responseType<Unit>())
    }

    fun deleteModerationBans(request: DeleteModerationBansRequest) {
        delete(configuration.moderation)
            .body(request)
            .send(responseType<Unit>())
    }

    fun deleteModerationBans(broadcasterUserId: Int, userId: Int) {
        deleteModerationBans(DeleteModerationBansRequest(broadcasterUserId, userId))
    }
}
