package org.dmc.services.company;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanyVideo {
	
    private final String logTag = CompanyVideo.class.getName();   
    
    private int id;  
    private int companyId;
    private String title; 
    private String link; 
    
    
    public CompanyVideo() {
    	this.id = -1;
    	this.companyId = -1;
    	this.title =  new String();
    	this.link = new String();
    }
    
    public CompanyVideo(int id, int companyId, String title, String link) {
    	this.id = id;
    	this.companyId = companyId;
    	this.title = title;
    	this.link = link;
    }
    
    @JsonProperty("id")
	public int getId() {
		return id;
	}
    public void setId(String id) {
		this.id = Integer.parseInt(id);
	}
    
    @JsonProperty("companyId")
    public int getCompanyId(){
    	return this.companyId;
    }
    public void setCompanyId(String companyId){
    	this.companyId = Integer.parseInt(companyId);
    }
    
    @JsonProperty("title")
    public String getTitle(){
    	return this.title;
    }
    public void setTitle(String title){
    	this.title = title;
    }
    
    @JsonProperty("link")
    public String getLink(){
    	return this.link;
    }
    public void setLink(String link){
    	this.link = link;
    }
}