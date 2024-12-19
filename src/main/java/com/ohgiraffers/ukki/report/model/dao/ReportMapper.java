package com.ohgiraffers.ukki.report.model.dao;

import com.ohgiraffers.ukki.report.model.dto.ReportDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportMapper {
    List<ReportDTO> findReportList(int userNo);
}
