package com.mbayou.kick4k.chat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbayou.kick4k.KickConfiguration;
import com.mbayou.kick4k.api.ApiClient;
import com.mbayou.kick4k.authorization.AuthorizationClient;

import java.net.http.HttpClient;

public class ChatClient extends ApiClient {
    public ChatClient(HttpClient httpClient, ObjectMapper mapper, KickConfiguration configuration, AuthorizationClient authorization) {
        super(httpClient, mapper, configuration, authorization);
    }

    public PostChatMessageResponse postChatMessage(PostChatMessageRequest request) {
        return this.post(this.configuration.getChat())
                .body(request)
                .send(new TypeReference<>() {});
    }
}
