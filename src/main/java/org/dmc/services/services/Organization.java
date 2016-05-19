package org.dmc.services.services;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Organization {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "organization_id")
	private Integer id;
	private Integer account_id;
	private String name;
	private String location;
	private String description;
	private String division;
	private String industry;
	private String naicsCode;
	private String rdFocus;
	private String customers;
	private String awards;
	private String techExpertise;
	private String toolsSoftwareEquipMach;
	private String postCollaboration;
	private String collaborationInterest;
	private String pastProjects;
	private String upcomingProjectInterests;
	private Integer adressId;
	private String email;
	private String phone;
	private String website;
	private String socialMediaLinkedin;
	private String socialMediaTwitter;
	private String socialMediaInthenews;
	private String perferedCommMethod;
	private Integer categoryTier;
	private String dateJoining;
	private String reasonJoining;
	private Integer featureImage;
	private String logoImage;
	private String follow;
	private Integer favoritesCount;
	// TODO make this a boolean (it's a string in the database)
	private String isOwner;
	private String owner;
}
