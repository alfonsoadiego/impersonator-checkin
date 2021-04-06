
package org.acwar.impersonator.beans.worklogissue.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * User
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "self",
    "name",
    "key",
    "emailAddress",
    "avatarUrls",
    "displayName",
    "active",
    "timeZone"
})
public class Author {

    @JsonProperty("self")
    private String self;
    @JsonProperty("name")
    private String name;
    @JsonProperty("key")
    private String key;
    @JsonProperty("emailAddress")
    private String emailAddress;
    @JsonProperty("avatarUrls")
    private AvatarUrls avatarUrls;
    @JsonProperty("displayName")
    private String displayName;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("timeZone")
    private String timeZone;

    @JsonProperty("self")
    public String getSelf() {
        return self;
    }

    @JsonProperty("self")
    public void setSelf(String self) {
        this.self = self;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("emailAddress")
    public String getEmailAddress() {
        return emailAddress;
    }

    @JsonProperty("emailAddress")
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @JsonProperty("avatarUrls")
    public AvatarUrls getAvatarUrls() {
        return avatarUrls;
    }

    @JsonProperty("avatarUrls")
    public void setAvatarUrls(AvatarUrls avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("displayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("active")
    public Boolean getActive() {
        return active;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("active")
    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonProperty("timeZone")
    public String getTimeZone() {
        return timeZone;
    }

    @JsonProperty("timeZone")
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

}
