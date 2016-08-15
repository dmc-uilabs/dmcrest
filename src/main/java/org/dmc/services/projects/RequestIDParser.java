package org.dmc.services.projects;

import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;

public class RequestIdParser {

    private int requesterId;
    private int memberId;
    private int projectId;

    public RequestIdParser(String requestID) {
        final String[] parts = requestID.split("-");
        if (parts.length != 3)
            throw new DMCServiceException(DMCError.BadURL, "Invalid request ID");
        requesterId = Integer.parseInt(parts[2]);
        memberId = Integer.parseInt(parts[1]);
        projectId = Integer.parseInt(parts[0]);
    }

    public int getRequesterId() {
        return requesterId;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getProjectId() {
        return projectId;
    }

}
