package org.dmc.services.company;

import java.util.ArrayList;

public class CompanySkill {
    private final String logTag = CompanySkill.class.getName();    
    private final int id;  
    private final int companyId;
    private final String skillName;       
    public CompanySkill(int i, int ci, String s) {
    	this.id = i;
    	this.companyId = ci;
    	this.skillName = s;
    }
    public int getId() {
        return this.id;
    }
    public int getCompanyId(){
    	return this.companyId;
    }
    public String getSkill() {
    	return this.skillName;
    }
}