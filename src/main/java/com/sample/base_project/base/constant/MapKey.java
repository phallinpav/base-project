package com.sample.base_project.base.constant;

import java.util.Set;

public class MapKey {

    public static class User {
        public static final String ACCOUNT = "user.account";

        public static final Set<String> EXCLUDE_SELF_REF = Set.of(
                Account.USER
        );
    }

    public static class Account {
        public static final String USER = "account.user";

        public static final Set<String> EXCLUDE_SELF_REF = Set.of(
                User.ACCOUNT
        );
    }

}
