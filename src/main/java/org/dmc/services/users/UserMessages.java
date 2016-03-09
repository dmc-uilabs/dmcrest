package org.dmc.services.users;

import java.util.ArrayList;
import java.util.Objects;

import org.dmc.services.message.Message;

public class UserMessages {
	
	private final ArrayList<Message> items;
    private final int totalItems;

	private final String logTag = UserMessages.class.getName();
	
    public UserMessages(){
		this(new ArrayList<Message>());
	}
    
	public UserMessages(ArrayList<Message> items){
        this.items = items;
        this.totalItems = items.size();
	}
	
	public int getTotalItems(){
		return items.size();
	}
	
	public ArrayList<Message> getItems(){
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
        UserMessages userMessages = (UserMessages) o;
        return Objects.equals(totalItems, userMessages.totalItems) &&
        Objects.equals(items, userMessages.items);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(totalItems, items);
    }
    
    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserDetailsMessages {\n");
        
        sb.append("  totalItems: ").append(totalItems).append("\n");
        sb.append("  items: ").append(items).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

}
