package com.asiainfo.draw.persistence;

import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.domain.DrawPrizeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DrawPrizeMapper {
    int countByExample(DrawPrizeExample example);

    int deleteByExample(DrawPrizeExample example);

    int deleteByPrimaryKey(Integer prizeId);

    int insert(DrawPrize record);

    int insertSelective(DrawPrize record);

    List<DrawPrize> selectByExample(DrawPrizeExample example);

    DrawPrize selectByPrimaryKey(Integer prizeId);

    int updateByExampleSelective(@Param("record") DrawPrize record, @Param("example") DrawPrizeExample example);

    int updateByExample(@Param("record") DrawPrize record, @Param("example") DrawPrizeExample example);

    int updateByPrimaryKeySelective(DrawPrize record);

    int updateByPrimaryKey(DrawPrize record);
}