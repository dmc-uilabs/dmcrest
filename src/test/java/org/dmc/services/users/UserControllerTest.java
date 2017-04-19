package org.dmc.services.users;

import org.dmc.services.UserService;
import org.dmc.services.data.models.UserModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Created by claytaylor on 4/13/17.
 */
public class UserControllerTest {

    private UserController tested = null;

    @Mock
    private UserService mockService;

    @Mock
    private UserModel mockUserModel;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        tested = new UserController();

        ReflectionTestUtils.setField(tested, "userService", mockService);
        when(mockService.findOne(anyInt())).thenReturn(mockUserModel);

    }

    @Test
    public void getUserName_UserNameExists() throws Exception {
        when(mockUserModel.getDisplayName()).thenReturn("DMC");
        assertEquals(tested.getUserName(111).getBody().get("displayName"), "DMC");
    }

    @Test
    public void getUserName_NoUserName() throws Exception {
        when(mockService.findOne(anyInt())).thenReturn(null);
        assertEquals(tested.getUserName(112).getBody().get("errorMessage"), "User with the id of 112 not found.");
    }

}
