package com.fidenz.academy.entity.response.weather;

import java.util.List;


public class Response {
    private int cnt;

    private List<Element> list;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<Element> getList() {
        return list;
    }

    public void setList(List<Element> list) {
        this.list = list;
    }
}


