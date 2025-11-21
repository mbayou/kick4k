package com.mbayou.kick4k.users

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import java.net.http.HttpClient

class UsersClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
): ApiClient(httpClient, mapper, configuration) {

    fun tokenIntrospect(accessToken: String): TokenIntrospect? {
        return post(configuration.tokenIntrospect)
            .withAccessToken(accessToken)
            .send(responseType<TokenIntrospect>())
    }

    fun getCurrentUser(accessToken: String): User = getUsers(accessToken).first()

    fun getUsers(accessToken: String): List<User> {
        return get(configuration.users)
            .withAccessToken(accessToken)
            .send(responseType<List<User>>()) ?: emptyList()
    }

    fun getUsers(accessToken: String, vararg ids: Int): List<User> {
        return get(configuration.users)
            .queryParams(mapOf("id" to ids))
            .withAccessToken(accessToken)
            .send(responseType<List<User>>()) ?: emptyList()
    }

    fun getUsers(accessToken: String, ids: List<Int>): List<User> {
        return get(configuration.users)
            .queryParams(mapOf("id" to ids))
            .withAccessToken(accessToken)
            .send(responseType<List<User>>()) ?: emptyList()
    }
}
