package com.mbayou.kick4k.livestreams;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LivestreamsStats {
    private final Integer totalCount;

    @JsonCreator
    public LivestreamsStats(@JsonProperty("total_count") Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }
}
