package org.dmc.services.users;

import java.util.ArrayList;

import org.dmc.services.services.Service;

public class UserRunningServices {
	
	private final ArrayList<Service> items;
	
	private final String logTag = UserRunningServices.class.getName();
	
    public UserRunningServices(){
		this(new ArrayList<Service>());
	}
    
	public UserRunningServices(ArrayList<Service> items){
		this.items = items;
	}
	
	public int getTotalItems(){
		return items.size();
	}
	
	public ArrayList<Service> getItems(){
		return items;
	}
}
