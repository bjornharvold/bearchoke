/*
 * Copyright (c) 2015. Traveliko
 */

package com.bearchoke.platform.domain.user.test;

import org.jasypt.util.password.PasswordEncryptor;

/**
 * Created by Bjorn Harvold
 * Date: 9/12/15
 * Time: 18:31
 * Responsibility:
 */
public class TestPasswordEncryptor implements PasswordEncryptor {
    @Override
    public String encryptPassword(String s) {
        return s;
    }

    @Override
    public boolean checkPassword(String s, String s1) {
        return s.equals(s1);
    }
}
