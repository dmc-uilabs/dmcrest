package org.dmc.services.users;

import java.util.ArrayList;

import org.dmc.services.message.Message;

public class UserMessages {
	
	private final int totalItems;
	private final ArrayList<Message> items;
	
	private final String logTag = UserMessages.class.getName();
	
    public UserMessages(){
		this(0, new ArrayList<Message>());
	}
    
	public UserMessages(int totalItems, ArrayList<Message> items){
		this.totalItems = totalItems;
		this.items = items;
	}
	
	public int getTotalItems(){
		return totalItems;
	}
	
	public ArrayList<Message> getItems(){
		return items;
	}
}
