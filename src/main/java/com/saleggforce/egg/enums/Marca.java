package com.saleggforce.egg.enums;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Marca {
    VOLKSWAGEN("volkswagen"),
    PEUGEOT("peugeot"),
    RENAULT("renault"),
    FIAT("fiat"),
    NISSAN("nissan");

    private String marca;

    public String getMarca() {
        return this.marca;
    }

    private static final Map<String, Marca> lookup = new HashMap<>();

    static {
        for (Marca m : EnumSet.allOf(Marca.class)) {
            lookup.put(m.getMarca(), m);
        }
    }

    Marca(String marca) {
        this.marca = marca;
    }

    public static Marca buscarMarca(String marca) {
        return lookup.get(marca);
    }
}
