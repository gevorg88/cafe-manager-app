package org.example.cafemanager.dto.table;

public class TableCreate {
    private String name;

    public TableCreate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
