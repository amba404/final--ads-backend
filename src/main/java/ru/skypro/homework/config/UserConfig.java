package ru.skypro.homework.config;

public class UserConfig {

    public final static int EMAIL_MIN_LENGTH = 4;
    public final static int EMAIL_MAX_LENGTH = 32;

    public final static int FIRST_NAME_MIN_LENGTH = 3;
    public final static int FIRST_NAME_MAX_LENGTH = 10;

    public final static int LAST_NAME_MIN_LENGTH = 3;
    public final static int LAST_NAME_MAX_LENGTH = 10;

    public final static int PHONE_LENGTH = 18;
    public final static String PHONE_PATTERN = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}";

    public final static int PASSWORD_MIN_LENGTH = 8;
    public final static int PASSWORD_MAX_LENGTH = 16;

}
