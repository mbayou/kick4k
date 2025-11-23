package com.mbayou.kick4k.events.type

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.mbayou.kick4k.categories.Category

data class LivestreamMetadata @JsonCreator constructor(
    @JsonProperty("title") val title: String,
    @JsonProperty("language") val language: String,
    @JsonProperty("has_mature_content") val hasMatureContent: Boolean,
    @JsonProperty("category") @JsonAlias("Category") val category: Category,
)
