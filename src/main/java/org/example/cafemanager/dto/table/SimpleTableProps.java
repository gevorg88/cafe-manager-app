package org.example.cafemanager.dto.table;

public interface SimpleTableProps {
    Long getId();

    String getName();

    FetchedUser getUser();

    interface FetchedUser {
        Long getId();

        String getUsername();
    }
}
