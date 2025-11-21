package com.mbayou.kick4k.categories

import com.fasterxml.jackson.databind.ObjectMapper
import com.mbayou.kick4k.KickConfiguration
import com.mbayou.kick4k.api.ApiClient
import java.net.http.HttpClient

class CategoriesClient(
    httpClient: HttpClient,
    mapper: ObjectMapper,
    configuration: KickConfiguration,
): ApiClient(httpClient, mapper, configuration) {

    fun getCategories(accessToken: String, query: String, page: Int = 1): List<Category> {
        return get(configuration.categories)
            .queryParams(mapOf("q" to query, "page" to page))
            .withAccessToken(accessToken)
            .send(responseType<List<Category>>()) ?: emptyList()
    }

    fun getCategory(accessToken: String, categoryId: Int): Category? {
        return get(configuration.categoriesId)
            .pathParams(mapOf("id" to categoryId))
            .withAccessToken(accessToken)
            .send(responseType<Category>())
    }
}
