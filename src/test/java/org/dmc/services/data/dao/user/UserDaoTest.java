package org.dmc.services.data.dao.user;

import org.dmc.services.DBConnector;
import org.dmc.services.data.entities.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by kskronek on 8/31/2016.
 */
public class UserDaoTest {

	private static final String FIND_ALL_USERS = "select user_id, user_name, accept_term_cond_time, realname, title, "
			+ "firstname, lastname, email, address, phone, image, about_me, add_date from users";

	private UserDao userDao;

	private List<User> users;

	private static final Random RANDOM = new Random();

	@Before
	public void setup() {
		userDao = new UserDao();
		users = DBConnector.jdbcTemplate().query(FIND_ALL_USERS, new UserRowMapper());
	}

	@Test
	public void testSetup() {
		assertNotNull(users);
		assertTrue(users.size() > 0);
	}

	@Test
	public void testGetUserName() throws Exception {
		User user = users.get(RANDOM.nextInt(users.size()));
		final String userName = userDao.getUserName(user.getId());
		assertEquals(user.getUsername(), userName);
	}
}
