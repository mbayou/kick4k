package com.mbayou.kick4k.categories

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import com.mbayou.kick4k.authorization.AuthorizationClient
import java.net.http.HttpClient

class CategoriesClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
    authorization: AuthorizationClient,
) : ApiClient(httpClient, mapper, configuration, authorization) {

    fun getCategories(query: String, page: Int = 1): List<Category> {
        return get(configuration.categories)
            .queryParams(mapOf("q" to query, "page" to page))
            .send(responseType<List<Category>>()) ?: emptyList()
    }

    fun getCategory(categoryId: Int): Category? {
        return get(configuration.categoriesId)
            .pathParams(mapOf("id" to categoryId))
            .send(responseType<Category>())
    }
}
