package com.fidenz.academy.entity.response;

import javax.persistence.Embeddable;

@Embeddable
public class Cloud {
    private int all;

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }
}
