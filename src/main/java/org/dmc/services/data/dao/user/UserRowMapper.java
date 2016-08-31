package org.dmc.services.data.dao.user;

import org.dmc.services.data.entities.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by kskronek on 8/31/2016.
 */
public class UserRowMapper implements RowMapper<User> {
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("user_id"));
		user.setUsername(rs.getString("user_name"));
		user.setTermsAndCondition(rs.getDate("accept_term_cond_time"));
		user.setRealname(rs.getString("realname"));
		user.setTitle(rs.getString("title"));
		user.setFirstName(rs.getString("firstname"));
		user.setLastName(rs.getString("lastname"));
		user.setEmail(rs.getString("email"));
		user.setAddress(rs.getString("address"));
		user.setPhone(rs.getString("phone"));
		user.setImage(rs.getString("image"));
		user.setAboutMe(rs.getString("about_me"));
		user.setAddDate(rs.getLong("add_date"));
		return user;
	}
}
