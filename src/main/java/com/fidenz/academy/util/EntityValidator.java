package com.fidenz.academy.util;

import com.fidenz.academy.entity.GenericEntity;

import java.util.Date;

public class EntityValidator {
    public static boolean isExpired(GenericEntity entity) {
        Date now = new Date();
        long timeDifference = (now.getTime() - entity.getTimestamp().getTime()) / 1000;
        if (timeDifference > 3600) { //stored entity is one hour old (3600 sec.)
            return true;
        } else {
            return false;
        }
    }
}
