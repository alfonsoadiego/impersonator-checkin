
package org.acwar.impersonator.beans.worklogissue.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Worklog
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "author",
    "updateAuthor",
    "comment",
    "created",
    "updated",
    "visibility",
    "started",
    "timeSpent",
    "timeSpentSeconds",
    "id",
    "issueId"
})
public class Worklog {

    /**
     * User
     * <p>
     * 
     * 
     */
    @JsonProperty("author")
    private Author author;
    /**
     * User
     * <p>
     * 
     * 
     */
    @JsonProperty("updateAuthor")
    private UpdateAuthor updateAuthor;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("created")
    private String created;
    @JsonProperty("updated")
    private String updated;
    /**
     * Visibility
     * <p>
     * 
     * 
     */
    @JsonProperty("visibility")
    private Visibility visibility;
    @JsonProperty("started")
    private String started;
    @JsonProperty("timeSpent")
    private String timeSpent;
    @JsonProperty("timeSpentSeconds")
    private Integer timeSpentSeconds;
    @JsonProperty("id")
    private String id;
    @JsonProperty("issueId")
    private String issueId;

    /**
     * User
     * <p>
     * 
     * 
     */
    @JsonProperty("author")
    public Author getAuthor() {
        return author;
    }

    /**
     * User
     * <p>
     * 
     * 
     */
    @JsonProperty("author")
    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * User
     * <p>
     * 
     * 
     */
    @JsonProperty("updateAuthor")
    public UpdateAuthor getUpdateAuthor() {
        return updateAuthor;
    }

    /**
     * User
     * <p>
     * 
     * 
     */
    @JsonProperty("updateAuthor")
    public void setUpdateAuthor(UpdateAuthor updateAuthor) {
        this.updateAuthor = updateAuthor;
    }

    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    @JsonProperty("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    @JsonProperty("created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("updated")
    public String getUpdated() {
        return updated;
    }

    @JsonProperty("updated")
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    /**
     * Visibility
     * <p>
     * 
     * 
     */
    @JsonProperty("visibility")
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Visibility
     * <p>
     * 
     * 
     */
    @JsonProperty("visibility")
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    @JsonProperty("started")
    public String getStarted() {
        return started;
    }

    @JsonProperty("started")
    public void setStarted(String started) {
        this.started = started;
    }

    @JsonProperty("timeSpent")
    public String getTimeSpent() {
        return timeSpent;
    }

    @JsonProperty("timeSpent")
    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    @JsonProperty("timeSpentSeconds")
    public Integer getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    @JsonProperty("timeSpentSeconds")
    public void setTimeSpentSeconds(Integer timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("issueId")
    public String getIssueId() {
        return issueId;
    }

    @JsonProperty("issueId")
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

}
