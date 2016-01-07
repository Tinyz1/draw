package com.asiainfo.draw.persistence;

import com.asiainfo.draw.domain.RoomMember;
import com.asiainfo.draw.domain.RoomMemberExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoomMemberMapper {
    int countByExample(RoomMemberExample example);

    int deleteByExample(RoomMemberExample example);

    int deleteByPrimaryKey(Integer memberId);

    int insert(RoomMember record);

    int insertSelective(RoomMember record);

    List<RoomMember> selectByExample(RoomMemberExample example);

    RoomMember selectByPrimaryKey(Integer memberId);

    int updateByExampleSelective(@Param("record") RoomMember record, @Param("example") RoomMemberExample example);

    int updateByExample(@Param("record") RoomMember record, @Param("example") RoomMemberExample example);

    int updateByPrimaryKeySelective(RoomMember record);

    int updateByPrimaryKey(RoomMember record);
}