package org.dmc.services.notification;

public class Notification {

    /**     
     ToDo *** Service you added gets removed from one of your projects - link to service page inside project
     **/
    
    public enum NotificationEvent {
        /** <code>SERVICE_FINISH</code> Service run finishes - link to run **/
        SERVICE_FINISH,
        /** <code>SERVICE_ERROR</code> Server failed to connect for service - link to service **/
        SERVICE_ERROR,
        /** <code>SERVICE_RUN</code> Service is run in one of your projects - link to service run page **/
        SERVICE_RUN,
        
        /** <code>FOLLOW_SERVICE</code>   Someone follows your service - link to user profile **/
        FOLLOW_SERVICE,
        /** <code>FOLLOW_COMPONENT</code>   Someone follows your component - link to user profile **/
        FOLLOW_COMPONENT,
        /** <code>FOLLOW_USER</code>   Someone follows you - link to user profile **/
        FOLLOW_USER,
        /** <code>FOLLOW_COMPANY</code>   Someone follows your company - link to user profile **/
        FOLLOW_COMPANY,
        
        /** <code>SUBSCRIBE_YOUR_SERVICE_TO_PROJECT</code>   Someone adds your service to their project (public project will say what project) - link to either user or project (only if public project) **/
        SUBSCRIBE_YOUR_SERVICE_TO_PROJECT,
        /** <code>SUBSCRIBE_SERVICE_TO_PROJECT</code>   Someone adds your service to their project (public project will say what project) - link to either user or project (only if public project) **/
        SUBSCRIBE_SERVICE_TO_YOUR_PROJECT,
        /** <code>SUBSCRIBE_USER_TO_COMPANY</code>   Someone from your company became a member (later version) **/
        SUBSCRIBE_USER_TO_COMPANY,

        
        /** <code>UPDATE_SERVICE</code> Service you follow updates/changes - link to service page with history tab selected **/
        UPDATE_SERVICE,
        /** <code>UPDATE_PROJECT</code> Project you work on updates - link to project **/
        UPDATE_PROJECT,
        /** <code>UPDATE_COMPANY_STOREFRONT</code> Your company's storefront is updated - link to company storefront **/
        UPDATE_COMPANY_STOREFRONT,
        /** <code>UPDATE_COMPANY_PROFILE</code> Your company's profile is updated - link to company profile **/
        UPDATE_COMPANY_PROFILE,
        /** <code>UPDATE_USER_PROFILE</code> Someone you follow profile is updated - link to user profile **/
        UPDATE_USER_PROFILE,

        
        /** <code>REVIEW_SERVICE</code>  Someone reviews you/your service - link to review **/
        REVIEW_SERVICE,
        /** <code>REVIEW_COMPONENT</code>  Someone reviews you/your component - link to review **/
        REVIEW_COMPONENT,
        /** <code>REVIEW_COMPANY</code>  Someone reviews you/your company - link to review **/
        REVIEW_COMPANY,
        
        /** <code>REPLY_REVIEW</code>  Someone replies to your review - link to review **/
        REPLY_REVIEW,
        /** <code>REPLY_DISCUSSION</code>  Someone replies to a discussion you follow, made or commented on - link to the discussion **/
        REPLY_DISCUSSION,
        
        /** <code>TASK_ASSIGN</code>  Someone assigns you a task - link to project with task and pop up task modal **/
        TASK_ASSIGN,
        /** <code>TASK_DUE</code>   task/project is due soon - link to project with task and pop up task modal **/
        TASK_DUE,
        /** <code>TASK_DUE</code>   Someone updates a task that you own/work on - link to project with task and pop up task modal **/
        TASK_UPDATE,

        /** <code>PROJECT_DUE</code>   task/project is due soon - link to project with task and pop up task modal **/
        PROJECT_DUE,

        /** <code>ANNOUNCEMENT_DMC</code>  DMC announcement - link to discussion page **/
        ANNOUNCEMENT_DMC,
        /** <code>ANNOUNCEMENT_SYSTEM</code>  System notifications - links to discussion (for now we can make these just discussions) **/
        ANNOUNCEMENT_SYSTEM,

        /** <code>EVENT_DMC</code>  DMC event - link to Community Home with Event expanded **/
        EVENT_DMC,
        
        /** <code>JOIN_PUBLIC_PROJECT</code>  Someone you follow starts working on a public project - link to project **/
        JOIN_PUBLIC_PROJECT
    }
    
    private final int id;
    private final String image;
    private final String link;
    private final String type;
    private final String period;
    private final String date;
    private final String title;
    private final String linkTitle;

    Notification(int id, String image, String link, String type,
                 String period, String date, String title, String linkTitle) {
        this.id = id;
        this.image = image;
        this.link = link;
        this.type = type;
        this.period = period;
        this.date = date;
        this.title = title;
        this.linkTitle = linkTitle;
    }
    
    public int getId() {
        return id;
    }
    
    public String getImage() {
        return image;
    }
    
    public String getLink() {
        return link;
    }
    
    public String getType() {
        return type;
    }
    
    public String getPeriod() {
        return period;
    }
    
    public String getDate() {
        return date;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getLinkTitle() {
        return linkTitle;
    }
}