package com.mbayou.kick4k.users;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbayou.kick4k.KickConfiguration;
import com.mbayou.kick4k.api.ApiClient;
import com.mbayou.kick4k.authorization.AuthorizationClient;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;

public class UsersClient extends ApiClient {
    public UsersClient(HttpClient httpClient, ObjectMapper mapper, KickConfiguration configuration, AuthorizationClient authorization) {
        super(httpClient, mapper, configuration, authorization);
    }

    public TokenIntrospect tokenIntrospect() {
        return this.post(this.configuration.getTokenIntrospect())
                .send(new TypeReference<>() {});
    }

    public User getCurrentUser() {
        return this.getUsers().getFirst();
    }

    public List<User> getUsers(int... ids) {
        return this.get(this.configuration.getUsers())
                .queryParams(Map.of("id", ids))
                .send(new TypeReference<>() {});
    }

    public List<User> getUsers(List<Integer> ids) {
        return this.get(this.configuration.getUsers())
                .queryParams(Map.of("id", ids))
                .send(new TypeReference<>() {});
    }
}
