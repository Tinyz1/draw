package com.asiainfo.draw.persistence;

import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.domain.DrawLinkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DrawLinkMapper {
	int countByExample(DrawLinkExample example);

	int deleteByExample(DrawLinkExample example);

	int deleteByPrimaryKey(Integer linkId);

	int insert(DrawLink record);

	int insertSelective(DrawLink record);

	List<DrawLink> selectByExample(DrawLinkExample example);

	DrawLink selectByPrimaryKey(Integer linkId);

	int updateByExampleSelective(@Param("record") DrawLink record, @Param("example") DrawLinkExample example);

	int updateByExample(@Param("record") DrawLink record, @Param("example") DrawLinkExample example);

	int updateByPrimaryKeySelective(DrawLink record);

	int updateByPrimaryKey(DrawLink record);

	/**
	 * 查询下一环节
	 * 
	 * @return
	 */
	DrawLink selectNextLink();
}