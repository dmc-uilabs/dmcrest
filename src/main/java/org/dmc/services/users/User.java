package org.dmc.services.users;

/**
 * Created by 200005921 on 2/8/2016.
 */
public class User {

    private int id;
    private final String userName;
    private final String realName;

    public User (int id, String userName, String realName) {

        this.id = id;
        this.userName = userName;
        this.realName = realName;
    }

    public User (UserBuilder userBuilder) {
        this.id = userBuilder.id;
        this.userName = userBuilder.userName;
        this.realName = userBuilder.realName;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getRealName() {
        return realName;
    }


    public static class UserBuilder {

        private int id;
        private String userName;
        private String realName;

        public UserBuilder (int id) {
            this.id = id;
        }

        public UserBuilder (int id, String userName) {
            this.id = id;
            this.userName = userName;
        }

        public UserBuilder (int id, String userName, String realName) {
            this.id = id;
            this.realName = realName;
            this.userName = userName;
        }

        public void userName (String userName) {
            this.userName = userName;
        }

        public void realName (String realName) {
            this.realName = realName;
        }

        public User build() {
            return new User(this);
        }
    }


}

