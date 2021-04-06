package org.acwar.impersonator.beans.worklogid.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Worklog Ids Request
 * <p>
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ids"
})
public class WorkLogIdRequest {

    @JsonProperty("ids")
    private List<Integer> ids = null;

    @JsonProperty("ids")
    public List<Integer> getIds() {
        return ids;
    }

    @JsonProperty("ids")
    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

}