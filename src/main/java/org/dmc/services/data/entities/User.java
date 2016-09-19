package org.dmc.services.data.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer id;

	@Column(name = "user_name")
	private String username;

	@Column(name = "accept_term_cond_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date termsAndCondition;

	@Column(name = "user_pw")
	private String password;

	@Column(name = "realname")
	private String realname;

	@Column(name = "title")
	private String title;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "lastname")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "address")
	private String address;

	@Column(name = "phone")
	private String phone;

	@Column(name = "image")
	private String image;

	@Column(name = "about_me")
	private String aboutMe;

	@Column(name = "people_resume")
	private String resume = "";

	/**
	 * Time record was inserted into database stored as millis?
	 */
	@Column(name = "add_date")
	private Long addDate;

	@OneToMany(mappedBy = "user", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	private List<UserRoleAssignment> roles;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_contact_info_id")
	private UserContactInfo userContactInfo;

	@OneToOne(mappedBy = "user")
	private OrganizationUser organizationUser;

	@OneToOne(cascade = CascadeType.MERGE, orphanRemoval = true)
	@JoinColumn(table = "onboarding_status", name = "user_id")
	private OnboardingStatus onboarding;
	
	@OneToMany(mappedBy = "createdFor", cascade = {CascadeType.DETACH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
	@OrderBy("created DESC")
	private List<Notification> notifications;

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getTermsAndCondition() {
		return termsAndCondition;
	}

	public void setTermsAndCondition(Date termsAndCondition) {
		this.termsAndCondition = termsAndCondition;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public Long getAddDate() {
		return addDate;
	}

	public void setAddDate(long addDate) {
		this.addDate = addDate;
	}

	public List<UserRoleAssignment> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRoleAssignment> roles) {
		this.roles = roles;
	}

	public UserContactInfo getUserContactInfo() {
		return userContactInfo;
	}

	public void setUserContactInfo(UserContactInfo userContactInfo) {
		this.userContactInfo = userContactInfo;
	}

	public OrganizationUser getOrganizationUser() {
		return organizationUser;
	}

	public void setOrganizationUser(OrganizationUser organizationUser) {
		this.organizationUser = organizationUser;
	}

	public OnboardingStatus getOnboarding() {
		return onboarding;
	}

	public void setOnboarding(OnboardingStatus onboarding) {
		this.onboarding = onboarding;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aboutMe == null) ? 0 : aboutMe.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((organizationUser == null) ? 0 : organizationUser.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((userContactInfo == null) ? 0 : userContactInfo.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (aboutMe == null) {
			if (other.aboutMe != null)
				return false;
		} else if (!aboutMe.equals(other.aboutMe))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (organizationUser == null) {
			if (other.organizationUser != null)
				return false;
		} else if (!organizationUser.equals(other.organizationUser))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (userContactInfo == null) {
			if (other.userContactInfo != null)
				return false;
		} else if (!userContactInfo.equals(other.userContactInfo))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
