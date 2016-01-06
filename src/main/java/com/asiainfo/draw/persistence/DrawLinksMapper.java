package com.asiainfo.draw.persistence;

import com.asiainfo.draw.domain.DrawLinks;
import com.asiainfo.draw.domain.DrawLinksExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DrawLinksMapper {
    int countByExample(DrawLinksExample example);

    int deleteByExample(DrawLinksExample example);

    int deleteByPrimaryKey(Integer linkId);

    int insert(DrawLinks record);

    int insertSelective(DrawLinks record);

    List<DrawLinks> selectByExample(DrawLinksExample example);

    DrawLinks selectByPrimaryKey(Integer linkId);

    int updateByExampleSelective(@Param("record") DrawLinks record, @Param("example") DrawLinksExample example);

    int updateByExample(@Param("record") DrawLinks record, @Param("example") DrawLinksExample example);

    int updateByPrimaryKeySelective(DrawLinks record);

    int updateByPrimaryKey(DrawLinks record);
}