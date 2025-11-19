package com.mbayou.kick4k.categories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbayou.kick4k.KickConfiguration;
import com.mbayou.kick4k.api.ApiClient;
import com.mbayou.kick4k.authorization.AuthorizationClient;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;

public class CategoriesClient extends ApiClient {
    public CategoriesClient(HttpClient httpClient, ObjectMapper mapper, KickConfiguration configuration, AuthorizationClient authorization) {
        super(httpClient, mapper, configuration, authorization);
    }

    public List<Category> getCategories(String query) {
        return this.getCategories(query, 1);
    }

    public List<Category> getCategories(String query, int page) {
        return this.get(this.configuration.getCategories())
                .queryParams(Map.of("q", query,
                        "page", page))
                .send(new TypeReference<>() {});
    }

    public Category getCategory(int categoryId) {
        return this.get(this.configuration.getCategoriesId())
                .pathParams(Map.of("id", categoryId))
                .send(new TypeReference<>() {});
    }
}
