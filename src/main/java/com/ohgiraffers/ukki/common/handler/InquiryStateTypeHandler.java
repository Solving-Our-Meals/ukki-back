package com.ohgiraffers.ukki.common.handler;

import com.ohgiraffers.ukki.common.InquiryState;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InquiryStateTypeHandler extends BaseTypeHandler<InquiryState> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, InquiryState parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getInquiryState());
    }

    @Override
    public InquiryState getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String state = rs.getString(columnName);
        return state != null ? InquiryState.fromValue(state) : null;
    }

    @Override
    public InquiryState getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String state = rs.getString(columnIndex);
        return state != null ? InquiryState.fromValue(state) : null;
    }

    @Override
    public InquiryState getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String state = cs.getString(columnIndex);
        return state != null ? InquiryState.fromValue(state) : null;
    }
}
