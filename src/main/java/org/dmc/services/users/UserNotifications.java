package org.dmc.services.users;

import java.util.Objects;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserNotifications userNotifications = (UserNotifications) o;
        return Objects.equals(totalItems, userNotifications.totalItems) &&
        Objects.equals(items, userNotifications.items);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(totalItems, items);
    }
    
    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserDetailsNotifications {\n");
        
        sb.append("  totalItems: ").append(totalItems).append("\n");
        sb.append("  items: ").append(items).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

}
