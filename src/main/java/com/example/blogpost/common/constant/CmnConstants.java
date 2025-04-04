package com.example.blogpost.common.constant;

import com.example.blogpost.user.dto.UserSessionInfo;
import com.example.blogpost.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public final class CmnConstants {

    public static final Short STATUS_ACTIVE = 1;
    public static final Short STATUS_INACTIVE = 0;
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String USER_SESSION_INFO = "USER_SESSION_INFO";
    public static final String PUBLIC_END_POINTS = "/public/**";
    public static final String[] PUBLIC_END_POINT_EXCEPTIONS = new String[]{
            "/auth/change-password",
            "/auth/logout",
            "/auth/forgot-password"
    };


    // Prevent instantiation
    private CmnConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }

    public static UserSessionInfo getUserSessionInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return (UserSessionInfo) request.getAttribute(USER_SESSION_INFO);
    }

    public static User getLoggedInUser() {
        UserSessionInfo userSessionInfo = getUserSessionInfo();
        User loggedInUser = new User();
        loggedInUser.setId(userSessionInfo.getUserDTO().getId());
        return loggedInUser;
    }
}
