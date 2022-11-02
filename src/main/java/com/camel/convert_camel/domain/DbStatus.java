package com.camel.convert_camel.domain;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class DbStatus {
    private String dbDriver;
    private String dbJdbcUrl;
    private String dbUserName;
    private String dbPassword;
    private String dbTable;
    private String useType;
}
