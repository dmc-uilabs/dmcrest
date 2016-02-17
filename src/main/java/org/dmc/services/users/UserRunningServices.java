package org.dmc.services.users;

import java.util.ArrayList;

import org.dmc.services.services.Service;

public class UserRunningServices {
	
	private final int totalItems;
	private final ArrayList<Service> items;
	
	private final String logTag = UserRunningServices.class.getName();
	
    public UserRunningServices(){
		this(0, new ArrayList<Service>());
	}
    
	public UserRunningServices(int totalItems, ArrayList<Service> items){
		this.totalItems = totalItems;
		this.items = items;
	}
	
	public int getTotalItems(){
		return totalItems;
	}
	
	public ArrayList<Service> getItems(){
		return items;
	}
}
