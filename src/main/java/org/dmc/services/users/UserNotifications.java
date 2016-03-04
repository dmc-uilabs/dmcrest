package org.dmc.services.users;

import java.util.ArrayList;

import org.dmc.services.notification.Notification;

public class UserNotifications {
	
	private final ArrayList<Notification> items;
    private final int totalItems;
	
	private final String logTag = UserNotifications.class.getName();
	
    public UserNotifications(){
		this(new ArrayList<Notification>());
	}
    
	public UserNotifications(ArrayList<Notification> items){
		this.items = items;
        this.totalItems = items.size();
	}
	
	public int getTotalItems(){
		return items.size();
	}
	
	public ArrayList<Notification> getItems(){
		return items;
	}
}