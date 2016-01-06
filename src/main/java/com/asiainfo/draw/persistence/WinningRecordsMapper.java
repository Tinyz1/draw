package com.asiainfo.draw.persistence;

import com.asiainfo.draw.domain.WinningRecords;
import com.asiainfo.draw.domain.WinningRecordsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WinningRecordsMapper {
    int countByExample(WinningRecordsExample example);

    int deleteByExample(WinningRecordsExample example);

    int deleteByPrimaryKey(Integer recordId);

    int insert(WinningRecords record);

    int insertSelective(WinningRecords record);

    List<WinningRecords> selectByExample(WinningRecordsExample example);

    WinningRecords selectByPrimaryKey(Integer recordId);

    int updateByExampleSelective(@Param("record") WinningRecords record, @Param("example") WinningRecordsExample example);

    int updateByExample(@Param("record") WinningRecords record, @Param("example") WinningRecordsExample example);

    int updateByPrimaryKeySelective(WinningRecords record);

    int updateByPrimaryKey(WinningRecords record);
}