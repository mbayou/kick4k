package com.mbayou.kick4k.livestreams

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonValue

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GetLivestreamsRequest(
    val broadcasterUserId: List<Int>?,
    val category: Int?,
    val language: String?,
    val limit: Int?,
    val sort: Sort?,
) {
    companion object {
        @JvmStatic
        fun builder(): Builder = Builder()
    }

    enum class Sort {
        VIEWER_COUNT,
        STARTED_AT;

        @JsonValue
        fun toLower(): String = name.lowercase()
    }

    class Builder {
        private var broadcasterUserId: List<Int>? = null
        private var category: Int? = null
        private var language: String? = null
        private var limit: Int? = null
        private var sort: Sort? = null

        fun broadcasterUserId(ids: List<Int>?) = apply { this.broadcasterUserId = ids }
        fun broadcasterUserId(id: Int) = apply { this.broadcasterUserId = listOf(id) }
        fun category(category: Int?) = apply { this.category = category }
        fun language(language: String?) = apply { this.language = language }
        fun limit(limit: Int?) = apply { this.limit = limit }
        fun sort(sort: Sort?) = apply { this.sort = sort }

        fun build(): GetLivestreamsRequest =
            GetLivestreamsRequest(broadcasterUserId, category, language, limit, sort)
    }
}
