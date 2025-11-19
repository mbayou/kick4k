package com.mbayou.kick4k.livestreams

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class LivestreamsStats @JsonCreator constructor(
    @JsonProperty("total_count") val totalCount: Int?,
)
