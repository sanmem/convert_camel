package com.camel.convert_camel.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class RsMetaData {
    String colName;
    String camelName;
    String colType;
}
