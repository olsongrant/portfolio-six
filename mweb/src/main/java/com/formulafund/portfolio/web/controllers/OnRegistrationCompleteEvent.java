package com.formulafund.portfolio.web.controllers;

import java.util.Locale;

import com.formulafund.portfolio.data.model.ApplicationUser;

import org.springframework.context.ApplicationEvent;

/*
 * This class was copied from a baeldung tutorial.
 */
@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final String appUrl;
    private final Locale locale;
    private final ApplicationUser user;

    public OnRegistrationCompleteEvent(final ApplicationUser user, final Locale locale, final String appUrl) {
        super(user);
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    //

    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public ApplicationUser getUser() {
        return user;
    }

}
