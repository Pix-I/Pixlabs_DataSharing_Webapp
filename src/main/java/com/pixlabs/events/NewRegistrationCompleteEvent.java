package com.pixlabs.events;

import com.pixlabs.data.entities.user.User;
import org.springframework.context.ApplicationEvent;

/**
 * Created by pix-i on 20/01/2017.
 * ${Copyright}
 */
public class NewRegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private String appUrl;
    private String locale;

    public NewRegistrationCompleteEvent(final User user) {
        super(user);
        this.user = user;
    }

    public NewRegistrationCompleteEvent(User user, String appUrl, String locale) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
        this.locale = locale;
    }

    public NewRegistrationCompleteEvent(User user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }

    public User getUser() {
        return user;
    }

    public String getLocale() {
        return locale;
    }

    public String getAppUrl() {
        return appUrl;
    }
}
