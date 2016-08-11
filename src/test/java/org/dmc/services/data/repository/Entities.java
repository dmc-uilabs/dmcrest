package org.dmc.services.data.repository;

import org.apache.commons.lang3.RandomStringUtils;
import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIType;
import org.dmc.services.data.entities.DMDIITypeCategory;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.OrganizationUser;
import org.dmc.services.data.entities.User;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Created by kskronek on 8/10/2016.
 */
public class Entities {

	private final static Random RANDOM = new Random();

	private static final Calendar today = GregorianCalendar.getInstance();
	public static final String INDUSTRY = "Industry";

	public static Organization organization() {
		Organization o = new Organization();
		o.setAccountId(RANDOM.nextInt());
		o.setName(RandomStringUtils.randomAlphabetic(10));
		o.setLocation(RandomStringUtils.randomAlphabetic(10));
		o.setDescription(RandomStringUtils.randomAlphabetic(10));
		o.setDivision(RandomStringUtils.randomAlphabetic(10));
		o.setIndustry(RandomStringUtils.randomAlphabetic(10));
		o.setNaicsCode(RandomStringUtils.randomAlphabetic(10));
		o.setRdFocus(RandomStringUtils.randomAlphabetic(10));
		o.setCustomers(RandomStringUtils.randomAlphabetic(10));
		o.setAwards(RandomStringUtils.randomAlphabetic(10));
		o.setTechExpertise(RandomStringUtils.randomAlphabetic(10));
		o.setToolsSoftwareEquipMach(RandomStringUtils.randomAlphabetic(10));
		o.setPostCollaboration(RandomStringUtils.randomAlphabetic(10));
		o.setCollaborationInterest(RandomStringUtils.randomAlphabetic(10));
		o.setPastProjects(RandomStringUtils.randomAlphabetic(10));
		o.setUpcomingProjectInterests(RandomStringUtils.randomAlphabetic(10));
		o.setEmail(RandomStringUtils.randomAlphabetic(10));
		o.setPhone(RandomStringUtils.randomAlphabetic(10));
		o.setWebsite(RandomStringUtils.randomAlphabetic(10));
		o.setSocialMediaLinkedin(RandomStringUtils.randomAlphabetic(10));
		o.setSocialMediaTwitter(RandomStringUtils.randomAlphabetic(10));
		o.setSocialMediaInthenews(RandomStringUtils.randomAlphabetic(10));
		o.setPerferedCommMethod(RandomStringUtils.randomAlphabetic(10));
		o.setCategoryTier(RANDOM.nextInt());
		o.setDateJoining(RandomStringUtils.randomAlphabetic(10));
		o.setReasonJoining(RandomStringUtils.randomAlphabetic(10));
		o.setFeatureImage(RANDOM.nextInt());
		o.setLogoImage(RandomStringUtils.randomAlphabetic(10));
		o.setFollow(RandomStringUtils.randomAlphabetic(10));
		o.setFavoritesCount(RANDOM.nextInt());
		o.setIsOwner(RandomStringUtils.randomAlphabetic(10));
		o.setOwner(RandomStringUtils.randomAlphabetic(10));
		return o;
	}

	public static DMDIIMember member(Organization org, DMDIIType type) {
		DMDIIMember m = new DMDIIMember();
		m.setDmdiiType(type);
		m.setOrganization(org);
		m.setStartDate(getDateOneMonthAgo());
		m.setExpireDate(getDateOneMonthInFuture());
		return m;
	}

	public static DMDIIType dmdiiType(DMDIITypeCategory typeCategory) {
		DMDIIType t = new DMDIIType();
		t.setTier(1);
		t.setDmdiiTypeCategory(typeCategory);
		return t;
	}

	public static DMDIITypeCategory dmdiiTypeCategory() {
		DMDIITypeCategory typeCategory = new DMDIITypeCategory();
		typeCategory.setCategory(INDUSTRY);
		return typeCategory;
	}

	public static User user(){
		User u = new User();
		u.setUsername(RandomStringUtils.randomAlphabetic(10));
		u.setFirstName(RandomStringUtils.randomAlphabetic(10));
		u.setLastName(RandomStringUtils.randomAlphabetic(10));
		u.setEmail(RandomStringUtils.randomAlphabetic(10));
		u.setAddress(RandomStringUtils.randomAlphabetic(10));
		u.setPhone(RandomStringUtils.randomAlphabetic(10));
		u.setAboutMe(RandomStringUtils.randomAlphabetic(10));
		return u;
	}

	public static OrganizationUser organizationUser(Organization org, User user) {
		OrganizationUser ou = new OrganizationUser();
		ou.setUser(user);
		ou.setOrganization(org);
		ou.setIsVerified(true);
		return ou;
	}

	private static Date getDateOneMonthAgo() {
		return getSomeOtherDate(Calendar.MONTH, -1);
	}

	private static Date getDateOneMonthInFuture() {
		return getSomeOtherDate(Calendar.MONTH, 1);
	}

	private static Date getSomeOtherDate(int field, int amount) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(field, amount);
		return calendar.getTime();
	}

}
