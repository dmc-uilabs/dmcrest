package org.dmc.services.search;

import java.io.Serializable;

/**
 * Created by 200005921 on 9/1/2016.
 */
public class SearchQueueMessage implements Serializable {

    private static final long serialVersionUID = -122225930513421581L;
    private String message;

    public SearchQueueMessage () {
        this(null);
    }

    public SearchQueueMessage (String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString () {
        return "{" +
                "\"message\"" + " : " + "\"" +message + "\"" +
                "}";
    }
}
