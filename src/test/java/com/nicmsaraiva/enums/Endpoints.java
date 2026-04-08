package com.nicmsaraiva.enums;

public enum Endpoints {
    LOGIN("/login"),
    USERS("/usuarios"),
    PRODUCTS("/produtos"),
    CARTS("/carrinhos"),;

    private final String path;

    Endpoints(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
