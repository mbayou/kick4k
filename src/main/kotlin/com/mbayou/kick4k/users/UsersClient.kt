package com.mbayou.kick4k.users

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import com.mbayou.kick4k.authorization.AuthorizationClient
import java.net.http.HttpClient

class UsersClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
    authorization: AuthorizationClient,
) : ApiClient(httpClient, mapper, configuration, authorization) {

    fun tokenIntrospect(): TokenIntrospect? {
        return post(configuration.tokenIntrospect)
            .send(responseType<TokenIntrospect>())
    }

    fun getCurrentUser(): User = getUsers().first()

    fun getUsers(): List<User> {
        return get(configuration.users)
            .send(responseType<List<User>>()) ?: emptyList()
    }

    fun getUsers(vararg ids: Int): List<User> {
        return get(configuration.users)
            .queryParams(mapOf("id" to ids))
            .send(responseType<List<User>>()) ?: emptyList()
    }

    fun getUsers(ids: List<Int>): List<User> {
        return get(configuration.users)
            .queryParams(mapOf("id" to ids))
            .send(responseType<List<User>>()) ?: emptyList()
    }
}
