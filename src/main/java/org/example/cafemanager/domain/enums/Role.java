package org.example.cafemanager.domain.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    MANAGER, WAITER;

    @Override
    public String getAuthority() {
        return name();
    }
}
