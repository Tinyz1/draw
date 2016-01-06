package com.asiainfo.draw.persistence;

import com.asiainfo.draw.domain.WinningRecord;
import com.asiainfo.draw.domain.WinningRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WinningRecordMapper {
    int countByExample(WinningRecordExample example);

    int deleteByExample(WinningRecordExample example);

    int deleteByPrimaryKey(Integer recordId);

    int insert(WinningRecord record);

    int insertSelective(WinningRecord record);

    List<WinningRecord> selectByExample(WinningRecordExample example);

    WinningRecord selectByPrimaryKey(Integer recordId);

    int updateByExampleSelective(@Param("record") WinningRecord record, @Param("example") WinningRecordExample example);

    int updateByExample(@Param("record") WinningRecord record, @Param("example") WinningRecordExample example);

    int updateByPrimaryKeySelective(WinningRecord record);

    int updateByPrimaryKey(WinningRecord record);
}