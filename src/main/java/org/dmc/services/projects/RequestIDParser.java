package org.dmc.services.projects;

import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.users.UserDao;

public class RequestIDParser {
	
	private static int requesterId;
	private static int memberId;
	private static int projectId;
	
	public static void interpret(String requestID){
		String[] parts = requestID.split("-");
		if (parts.length != 3)
			throw new DMCServiceException(DMCError.BadURL, "Invalid request ID");
		requesterId = Integer.parseInt(parts[2]);
		memberId = Integer.parseInt(parts[1]);
		projectId = Integer.parseInt(parts[0]);
	}
	
	public static int getRequesterId(){
		return requesterId;
	}
	
	public static int getMemberId(){
		return memberId;
	}
	
	public static int getProjectId(){
		return projectId;
	}

}
