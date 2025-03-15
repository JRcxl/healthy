package com.itheima.config;

import org.apache.ibatis.type.*;
import java.sql.*;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@MappedJdbcTypes(JdbcType.VARCHAR) // 如果数据库字段是 JSON 类型，但 JDBC 驱动返回字符串
@MappedTypes(List.class)
public class JsonToListTypeHandler extends BaseTypeHandler<List<String>> {

    private static final Gson gson = new Gson();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, gson.toJson(parameter));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getString(columnName));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getString(columnIndex));
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getString(columnIndex));
    }

    private List<String> parseJson(String json) {
        return json == null ? null : gson.fromJson(json, new TypeToken<List<String>>(){}.getType());
    }
}