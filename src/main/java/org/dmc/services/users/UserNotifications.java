package org.dmc.services.users;

import java.util.ArrayList;

import org.dmc.services.notification.Notification;

public class UserNotifications {
	
	private final int totalItems;
	private final ArrayList<Notification> items;
	
	private final String logTag = UserNotifications.class.getName();
	
    public UserNotifications(){
		this(0, new ArrayList<Notification>());
	}
    
	public UserNotifications(int totalItems, ArrayList<Notification> items){
		this.totalItems = totalItems;
		this.items = items;
	}
	
	public int getTotalItems(){
		return totalItems;
	}
	
	public ArrayList<Notification> getItems(){
		return items;
	}
}
