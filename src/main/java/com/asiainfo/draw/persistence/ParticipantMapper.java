package com.asiainfo.draw.persistence;

import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.domain.ParticipantExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ParticipantMapper {
    int countByExample(ParticipantExample example);

    int deleteByExample(ParticipantExample example);

    int deleteByPrimaryKey(Integer participantId);

    int insert(Participant record);

    int insertSelective(Participant record);

    List<Participant> selectByExample(ParticipantExample example);

    Participant selectByPrimaryKey(Integer participantId);

    int updateByExampleSelective(@Param("record") Participant record, @Param("example") ParticipantExample example);

    int updateByExample(@Param("record") Participant record, @Param("example") ParticipantExample example);

    int updateByPrimaryKeySelective(Participant record);

    int updateByPrimaryKey(Participant record);
}