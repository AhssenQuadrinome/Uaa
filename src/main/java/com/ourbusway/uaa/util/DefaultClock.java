package com.ourbusway.uaa.util;

import io.jsonwebtoken.Clock;

import java.util.Date;

public class DefaultClock implements Clock {
    public static final Clock INSTANCE = new DefaultClock();

    private DefaultClock() {
    }

    public Date now() {
        return new Date();
    }
}
