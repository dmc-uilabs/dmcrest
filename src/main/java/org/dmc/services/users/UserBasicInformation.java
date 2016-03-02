package org.dmc.services.users;

/**
 * Created by 200005921 on 2/8/2016.
 */


public class UserBasicInformation {

	private final int id;
	private final String email;
	private final String firstName;
    private final String lastName;
    private final String company;

	private UserBasicInformation(UserBasicInformationBuilder builder) {
		this.id = builder.id;
		this.email = builder.email;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.company = builder.company;
	}

    public int getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }

    public String getfirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getCompany() {
        return company;
    }
    
	public static class UserBasicInformationBuilder  {
		private int id;
		private String email;
		private String firstName;
		private String lastName;
		private String company;

		public UserBasicInformationBuilder (int id) {
			this.id = id;
		}

		public UserBasicInformationBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserBasicInformationBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public UserBasicInformationBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public UserBasicInformationBuilder company(String company) {
			this.company = company;
			return this;
		}

		public  UserBasicInformation build() {
			return new UserBasicInformation(this);
		}
	}

}

