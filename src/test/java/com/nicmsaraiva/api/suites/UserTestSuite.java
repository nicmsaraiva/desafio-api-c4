package com.nicmsaraiva.api.suites;

import com.nicmsaraiva.api.users.CreateUserTest;
import com.nicmsaraiva.api.users.DeleteUserTest;
import com.nicmsaraiva.api.users.GetUserTest;
import com.nicmsaraiva.api.users.UpdateUserTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        CreateUserTest.class,
        GetUserTest.class,
        UpdateUserTest.class,
        DeleteUserTest.class
})
public class UserTestSuite {
}