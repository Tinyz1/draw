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

	/**
	 * 根据用户编号获取用户信息
	 * 
	 * @param participantNum
	 *            用户编号
	 * 
	 * @return 用户信息
	 */
	Participant selectByParticipantNum(Integer participantNum);

	/**
	 * 根据用户编号和用户名称获取用户信息
	 * 
	 * @param participantNum
	 *            用户编号
	 * @param participantName
	 *            用户名称
	 * @return 用户信息
	 */
	Participant findParticipantByNumAndName(@Param("participantNum") Integer participantNum, @Param("participantName") String participantName);

	int updateByExampleSelective(@Param("record") Participant record, @Param("example") ParticipantExample example);

	int updateByExample(@Param("record") Participant record, @Param("example") ParticipantExample example);

	int updateByPrimaryKeySelective(Participant record);

	int updateByPrimaryKey(Participant record);
}