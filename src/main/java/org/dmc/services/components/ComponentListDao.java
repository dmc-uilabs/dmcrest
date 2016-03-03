package org.dmc.services.components;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.components.Component;
import org.dmc.services.services.Service;

import java.util.Date;

public class ComponentListDao {
    
	private final String logTag = ComponentListDao.class.getName();
	private ResultSet resultSet;
	
	public ComponentListDao(){}
	
	public ArrayList<Component> getComponentList(){
		
		ArrayList<Component> components = new ArrayList<Component>();
		
        int id = 0, oldId = id;
        Date releaseDate = new Date();
        String title = "", oldTitle = "", description = "", owner = "None", query;
        String thumbnail = "";
        String largeUrl = "";
        FeatureImage image = new FeatureImage(thumbnail, largeUrl);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<Service> services = new ArrayList<Service>();
        String date = "";
        String tag = "";
        
        
        
        query = "SELECT c.cem_id AS id, c.name AS title, c.repo_creation_time AS releaseDate, "
        + "t.tag_name AS tagname,"
        + "c.description AS description, g.group_name AS username "
        +"FROM cem_objects c LEFT JOIN cem_tags_join j ON c.cem_id = j.cem_id "
        + "LEFT JOIN groups g ON c.group_id = g.group_id"
        + " LEFT JOIN cem_tags t "
        + "ON t.cem_tag_id = j.cem_tag_id ORDER BY c.cem_id";
        
        //ServiceLogger.log(logTag, "getComponent, Query: " + query);
        
        try {
            resultSet = DBConnector.executeQuery(query);
            
            while (resultSet.next()) {
                
                id = resultSet.getInt("id");
                
                
                if (id != oldId){
                    /*if (tags.isEmpty()){
                     tags.add("");
                     }*/
                    
                    components.add(new Component.ComponentBuilder(oldId, title, description)
                                   .releaseDate(releaseDate)
                                   .services(services).image(image)
                                   .owner(owner).tags(tags).build());
                    
                    title = resultSet.getString("title");
                    description = resultSet.getString("description");
                    
                    if (description == null)
                        description = "";
                    
                    releaseDate = new Date(1000L * resultSet.getLong("releaseDate"));
                    owner = resultSet.getString("username");
                    tags = new ArrayList<String>();
                    
                    
                    SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
                    
                    date = formatter.format(releaseDate);
                    oldId = id;
                }
                
                tag = resultSet.getString("tagname");
                
                
                if (tag != null)
                    tags.add(tag);
                
            }
            
            
            components.add(new Component.ComponentBuilder(id, title, description)
                           .releaseDate(releaseDate)
                           .services(services).image(image).owner(owner)
                           .tags(tags).build());
            
            components.remove(0);
            return components;
        }
        
        catch(SQLException e){
			ServiceLogger.log(logTag, e.getMessage());
		}
        
        return null;
    }
	
	
	
    public ArrayList<Component> getComponentList(int projectId){
		
		ArrayList<Component> components = new ArrayList<Component>();
		
        int id = 0, oldId = id;
        Date releaseDate = new Date();
        String title = "", oldTitle = "", description = "", owner = "None", query;
        String thumbnail = "";
        String largeUrl = "";
        FeatureImage image = new FeatureImage(thumbnail, largeUrl);
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<Service> services = new ArrayList<Service>();
        String date = "";
        String tag = "";
        
        
        
        query = "SELECT c.cem_id AS id, c.name AS title, c.repo_creation_time AS releaseDate, "
        + "t.tag_name AS tagname,"
        + "c.description AS description, g.group_name AS username "
        +"FROM cem_objects c LEFT JOIN cem_tags_join j ON c.cem_id = j.cem_id "
        + "LEFT JOIN groups g ON c.group_id = g.group_id"
        + " LEFT JOIN cem_tags t "
        + "ON t.cem_tag_id = j.cem_tag_id "
        + "WHERE c.group_id = " + projectId + " ORDER BY c.cem_id";
        
        //ServiceLogger.log(logTag, "getComponent, Query: " + query);
        
        try {
            resultSet = DBConnector.executeQuery(query);
            
            while (resultSet.next()) {
                
                id = resultSet.getInt("id");
                
                
                if (id != oldId){
                    /*if (tags.isEmpty()){
                     tags.add("");
                     }*/
                    
                    components.add(new org.dmc.services.components.Component.ComponentBuilder(oldId, title, description)
                                   .releaseDate(releaseDate)
                                   .services(services).image(image)
                                   .owner(owner).tags(tags).build());
                    
                    title = resultSet.getString("title");
                    description = resultSet.getString("description");
                    
                    if (description == null)
                        description = "";
                    
                    releaseDate = new Date(1000L * resultSet.getLong("releaseDate"));
                    owner = resultSet.getString("username");
                    tags = new ArrayList<String>();
                    
                    
                    SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
                    
                    date = formatter.format(releaseDate);
                    oldId = id;
                }
                
                tag = resultSet.getString("tagname");
                
                
                if (tag != null)
                    tags.add(tag);
                
            }
            
            
            components.add(new Component.ComponentBuilder(id, title, description)
                           .releaseDate(releaseDate)
                           .services(services).image(image)
                           .owner(owner).tags(tags).build());
            components.remove(0);
            return components;
        }
        
        catch(SQLException e){
			ServiceLogger.log(logTag, e.getMessage());
		}
        
        return null;
    }	
	
	
}
