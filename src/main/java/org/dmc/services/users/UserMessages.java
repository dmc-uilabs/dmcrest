package org.dmc.services.users;

import java.util.ArrayList;

import org.dmc.services.message.Message;

public class UserMessages {
	
	private final ArrayList<Message> items;
	
	private final String logTag = UserMessages.class.getName();
	
    public UserMessages(){
		this(new ArrayList<Message>());
	}
    
	public UserMessages(ArrayList<Message> items){
        this.items = items;
	}
	
	public int getTotalItems(){
		return items.size();
	}
	
	public ArrayList<Message> getItems(){
		return items;
	}
}
