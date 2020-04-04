package org.example.cafemanager.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum Status {
    OPEN, CLOSED, CANCELED;

    public static Map<String, String> getEnumMapValues() {
        Map<String, String> statusesMap = new HashMap<>();

        for (Status status : Status.values()) {
            String statusStr = status.toString();
            statusesMap.put(statusStr, statusStr.substring(0, 1).toUpperCase() + statusStr.substring(1));
        }

        return statusesMap;
    }
}
