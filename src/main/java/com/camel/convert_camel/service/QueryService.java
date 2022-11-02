package com.camel.convert_camel.service;

import com.camel.convert_camel.config.DbConnectionPool;
import com.camel.convert_camel.domain.DbStatus;
import com.camel.convert_camel.domain.RsMetaData;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

@Service
public class QueryService {

    private String USE_TYPE = "vo";
    
    public DbStatus initDbStatus(){
        return DbStatus.builder()
                .useType(USE_TYPE)
                .build();
    }

    public ArrayList<String> colnums(DbStatus dbStatus){
        Connection conn;
        PreparedStatement ps;
        ResultSet rs;

        String sql = "select * from "+dbStatus.getDbTable();

        ArrayList<RsMetaData> camelCaseList = new ArrayList<RsMetaData>();

        try {
            conn = new DbConnectionPool().getConnection(dbStatus);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            for(int i=0;i < resultSetMetaData.getColumnCount();i++){
                RsMetaData rsMetaData = getRsMetaData(resultSetMetaData, i+1);
                camelCaseList.add(rsMetaData);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            return new ArrayList<String>(Collections.singleton("테이블명을 확인해라!!!"));
        } catch (Exception ex1){
            return new ArrayList<String>(Collections.singleton("DB정보 확인해라!!!"));
        }
        return createCase(camelCaseList, dbStatus.getUseType());
    }

    private ArrayList<String> createCase(ArrayList<RsMetaData> camelCaseList, String useType) {
        ArrayList<String> camelList = new ArrayList<>();
        if ( StringUtils.equals(useType,"mybatis")){
            camelList.add("<resultMap id=\"\" class=\"\">");
        }

        camelCaseList.stream().forEach(rsMetaData -> {
            switch (useType){
                case "vo":
                    camelList.add(createCamelName(rsMetaData));
                    camelList.add("");
                    break;
                case "hibernate":
                    camelList.add(createHivernateName(rsMetaData));
                    camelList.add(createCamelName(rsMetaData));
                    camelList.add("");
                    break;
                case "mybatis":
                    camelList.add(createMybatisName(rsMetaData));
                    break;
                default:
                    break;
            }
        });

        if ( StringUtils.equals(useType,"mybatis")){
            camelList.add("</resultMap>");
        }
        return camelList;
    }

    private static RsMetaData getRsMetaData(ResultSetMetaData resultSetMetaData, int i){
        try {
            return RsMetaData.builder()
                    .colName(StringUtils.toLowerCase(resultSetMetaData.getColumnName(i), Locale.KOREA))
                    .colType(resultSetMetaData.getColumnTypeName(i))
                    .camelName(JdbcUtils.convertUnderscoreNameToPropertyName(resultSetMetaData.getColumnName(i)))
                    .build();
        } catch (SQLException e) {
            return null;
        }
    }

    private String createHivernateName(RsMetaData rsMetaData) {
        String base = "@Column(name = \"%s\")";
        return String.format(base, rsMetaData.getColName());
    }

    private String createCamelName(RsMetaData rsMetaData){
        String base = "private %s %s;";
        String resultStr = "";
        switch (rsMetaData.getColType()){
            case "NUMBER" :
            case "BIGINT" :
                resultStr = String.format(base, "Long", rsMetaData.getCamelName());
                break;
            case "INTEGER" :
                resultStr = String.format(base, "int", rsMetaData.getCamelName());
                break;
            default:
                resultStr = String.format(base, "String", rsMetaData.getCamelName());
                break;
        }
        return resultStr;

    }

    private String createMybatisName(RsMetaData rsMetaData) {
        String base = "<result property=\"%s\" column=\"%s\" />";
        return String.format(base, rsMetaData.getCamelName(), rsMetaData.getColName());
    }
}
