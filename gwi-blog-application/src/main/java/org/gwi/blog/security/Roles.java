package org.gwi.blog.security;

public final class Roles {

    private static final String PREFIX = "ROLE_";
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";
    public static final String ROLE_USER = PREFIX + USER;
    public static final String ROLE_ADMIN = PREFIX + ADMIN;

    private Roles() {

    }

}
