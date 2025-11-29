package com.sample.base_project.base.constant;

import java.util.Set;

public class MapKey {
    public static class Broadcast {
        public static final String DEV_APP = "broadcast.devApp";
        public static final String DEV_TOTP = "broadcast.devTotp";
        public static final String DETAIL = "broadcast.detail";

        public static final Set<String> EXCLUDE_SELF_REF = Set.of();
    }

    public static class User {
        public static final String STORAGE = "user.storage";
        public static final String KYC_BASIC = "user.kycBasic";
        public static final String KYC_FACE = "user.kycFace";

        public static final Set<String> EXCLUDE_SELF_REF = Set.of(
                KycBasic.USER, KycFace.USER
        );
    }

    public static class Developer {
        public static final String STORAGE = "dev.storage";

        public static final Set<String> EXCLUDE_SELF_REF = Set.of(
                DeveloperApp.DEV
        );
    }

    public static class KycBasic {
        public static final String STORAGE = "kycBasic.storage";
        public static final String USER = "kycBasic.user";

        public static final Set<String> EXCLUDE_SELF_REF = Set.of();
    }

    public static class KycFace {
        public static final String STORAGE = "kycFace.storage";
        public static final String USER = "kycFace.user";

        public static final Set<String> EXCLUDE_SELF_REF = Set.of();
    }

    public static class UserTotp {
        public static final String DEV_TOTP = "userTotp.devTotp";

        public static final Set<String> EXCLUDE_SELF_REF = Set.of();
    }

    public static class DeveloperTotp {
        public static final String DEV_APP = "devTotp.devApp";

        public static final Set<String> EXCLUDE_SELF_REF = Set.of();
    }

    public static class DeveloperApp {
        public static final String STORAGE = "devApp.storage";
        public static final String DEV = "devApp.dev";

        public static final Set<String> EXCLUDE_SELF_REF = Set.of(
                DeveloperAppKey.DEV_APP
        );
    }

    public static class DeveloperAppKey {
        public static final String DEV_APP = "devAppKey.devApp";

        public static final Set<String> EXCLUDE_SELF_REF = Set.of();
    }
}
