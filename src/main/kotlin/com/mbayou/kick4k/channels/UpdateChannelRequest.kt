package com.mbayou.kick4k.channels

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UpdateChannelRequest @JsonCreator constructor(
    @JsonProperty("category_id") val categoryId: Int?,
    @JsonProperty("custom_tags") val customTags: List<String>?,
    @JsonProperty("stream_title") val streamTitle: String?,
) {
    companion object {
        @JvmStatic
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var categoryId: Int? = null
        private var customTags: List<String>? = null
        private var streamTitle: String? = null

        fun categoryId(categoryId: Int?) = apply { this.categoryId = categoryId }
        fun customTags(customTags: List<String>?) = apply { this.customTags = customTags }
        fun streamTitle(streamTitle: String?) = apply { this.streamTitle = streamTitle }

        fun build(): UpdateChannelRequest = UpdateChannelRequest(categoryId, customTags, streamTitle)
    }
}
