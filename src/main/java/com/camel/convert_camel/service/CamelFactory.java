package com.camel.convert_camel.service;

import com.camel.convert_camel.domain.RsMetaData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CamelFactory {
    private final static Map<String, Function<RsMetaData, String>> map = new HashMap<>();
    private static final String BASE = "private %s %s;";

    static {
        map.put("BIGINT", r -> String.format(BASE, "Long", r.getCamelName()));
        map.put("NUMBER", r -> String.format(BASE, "Long", r.getCamelName()));
        map.put("INTEGER", r -> String.format(BASE, "int", r.getCamelName()));
    }

    private final RsMetaData rsMetaData;
    public CamelFactory(RsMetaData rsMetaData) {
        this.rsMetaData = rsMetaData;
    }

    public String createValue(){
        try {
            return map.get(rsMetaData.getColType()).apply(rsMetaData);
        }catch (NullPointerException e){
            return String.format(BASE, "String", rsMetaData.getCamelName());
        }
    }
}
