
package org.acwar.impersonator.beans.worklogissue.request;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Visibility
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "value"
})
public class Visibility {

    @JsonProperty("type")
    private Visibility.Type type;
    @JsonProperty("value")
    private String value;

    @JsonProperty("type")
    public Visibility.Type getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(Visibility.Type type) {
        this.type = type;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    public enum Type {

        GROUP("group"),
        ROLE("role");
        private final String value;
        private final static Map<String, Visibility.Type> CONSTANTS = new HashMap<String, Visibility.Type>();

        static {
            for (Visibility.Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Visibility.Type fromValue(String value) {
            Visibility.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
