package com.mycompany.quickchatpoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private Login login;

    @BeforeEach
    public void setUp() {
        login = new Login();
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&sec@ke99!", "+27838968976");
    }

    @Test public void testUsernameCorrectlyFormatted()   { assertTrue(login.checkUserName("kyl_1")); }
    @Test public void testUsernameIncorrectNoUnderscore(){ assertFalse(login.checkUserName("kyle!!!!!!!")); }
    @Test public void testUsernameIncorrectTooLong()     { assertFalse(login.checkUserName("kyle!!!!!!")); }
    @Test public void testPasswordMeetsComplexity()      { assertTrue(login.checkPasswordComplexity("Ch&sec@ke99!")); }
    @Test public void testPasswordDoesNotMeetComplexity() { assertFalse(login.checkPasswordComplexity("password")); }
    @Test public void testCellPhoneCorrectlyFormatted()  { assertTrue(login.checkCellPhoneNumber("+27838968976")); }
    @Test public void testCellPhoneIncorrectlyFormatted(){ assertFalse(login.checkCellPhoneNumber("08966553")); }

    @Test public void testRegisterUserSuccessMessage() {
        Login fresh = new Login();
        String result = fresh.registerUser("Kyle", "Smith", "kyl_1", "Ch&sec@ke99!", "+27838968976");
        assertTrue(result.contains("Username successfully captured."));
    }

    @Test public void testRegisterUserBadUsernameMessage() {
        Login fresh = new Login();
        String result = fresh.registerUser("Kyle", "Smith", "kyle!!!!!!!", "Ch&sec@ke99!", "+27838968976");
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", result);
    }

    @Test public void testRegisterUserBadPasswordMessage() {
        Login fresh = new Login();
        String result = fresh.registerUser("Kyle", "Smith", "kyl_1", "password", "+27838968976");
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", result);
    }

    @Test public void testRegisterUserBadCellMessage() {
        Login fresh = new Login();
        String result = fresh.registerUser("Kyle", "Smith", "kyl_1", "Ch&sec@ke99!", "08966553");
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.", result);
    }

    @Test public void testLoginSuccessful() { assertTrue(login.loginUser("kyl_1", "Ch&sec@ke99!")); }
    @Test public void testLoginFailed()     { assertFalse(login.loginUser("kyl_1", "wrongPass1!")); }

    @Test public void testReturnLoginStatusSuccess() {
        assertEquals("Welcome Kyle, Smith it is great to see you again.", login.returnLoginStatus("kyl_1", "Ch&sec@ke99!"));
    }

    @Test public void testReturnLoginStatusFailure() {
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus("kyl_1", "wrong"));
    }
}