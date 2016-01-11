package com.asiainfo.draw.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.draw.domain.LinkMember;
import com.asiainfo.draw.domain.LinkMemberExample;
import com.asiainfo.draw.domain.Participant;

public interface LinkMemberMapper {
    int countByExample(LinkMemberExample example);

    int deleteByExample(LinkMemberExample example);

    int deleteByPrimaryKey(Integer memberId);

    int insert(LinkMember record);

    int insertSelective(LinkMember record);

    List<LinkMember> selectByExample(LinkMemberExample example);

    LinkMember selectByPrimaryKey(Integer memberId);

    int updateByExampleSelective(@Param("record") LinkMember record, @Param("example") LinkMemberExample example);

    int updateByExample(@Param("record") LinkMember record, @Param("example") LinkMemberExample example);

    int updateByPrimaryKeySelective(LinkMember record);

    int updateByPrimaryKey(LinkMember record);
    
  	/**
	 * ��ѯָ�����ڵĲ�����Ա�б�
	 * 
	 * @param linkId
	 *            ����ID
	 * @return ������Ա�б�
	 */
	List<Participant> selectParticipantByLink(@Param("linkId") Integer linkId);
}