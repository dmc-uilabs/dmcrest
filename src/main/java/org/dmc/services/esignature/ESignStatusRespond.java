package org.dmc.services.esignature;

import java.util.ArrayList;
import java.lang.Long;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ESignStatusRespond{

    @JsonIgnore
    private ArrayList<Item> items;

    private int total;

    @JsonIgnore
    private int current_page;
    @JsonIgnore
    private int per_page;
    @JsonIgnore
    private String prev_page_url;
    @JsonIgnore
    private String next_page_url;

    public ESignStatusRespond() {}

    public ArrayList<Item> getItems(){
        return items;
    }

    public void setItems(ArrayList<Item> items){
        this.items.addAll(items);
    }

    public int getTotal(){
        return total;
    }

    public void setTotal(int total){
        this.total = total;
    }

    public int getCurrentPage(){
        return current_page;
    }

    public void setCurrentPage(int current_page){
        this.current_page = current_page;
    }

    public int getPerPage(){
        return per_page;
    }

    public void setPerPage(int per_page){
        this.per_page = per_page;
    }

    public String getPrevPageURL(){
        return prev_page_url;
    }

    public void setPrevPageURL(String prev_page_url){
        this.prev_page_url = prev_page_url;
    }

    public String getNextPageURL(){
        return next_page_url;
    }

    public void setNextPageURL(String next_page_url){
        this.next_page_url = next_page_url;
    }


}


class Item{
    private Long filled_form_id;
    private Long document_id;
    private String name;
    private String email;
    private Long date;
    private String ip;
    private ArrayList<String> token;
    private ArrayList<Long> additional_documents;

    public Long getFilledFormId(){
        return filled_form_id;
    }

    public void setFilledFormId(Long id){
        this.filled_form_id = id;
    }

    public Long getDocumentId(){
        return document_id;
    }

    public void setDocumentId(Long id){
        this.document_id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public long getDate(){
        return date;
    }

    public void setDate(Long date){
        this.date = date;
    }

    public String getIP(){
        return ip;
    }

    public void setIP(String ip){
        this.ip = ip;
    }

    public ArrayList<String> getToken(){
        return token;
    }

    public void setToken(ArrayList<String> token){
        this.token.addAll(token);
    }

    public ArrayList<Long> getAdditionalDocuments(){
        return additional_documents;
    }

    public void setAdditionalDocuments(ArrayList<Long> additionalDocuments){
        this.additional_documents.addAll(additionalDocuments);
    }

}
