package com.asiainfo.draw.persistence;

import com.asiainfo.draw.domain.DrawPrizes;
import com.asiainfo.draw.domain.DrawPrizesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DrawPrizesMapper {
    int countByExample(DrawPrizesExample example);

    int deleteByExample(DrawPrizesExample example);

    int deleteByPrimaryKey(Integer prizeId);

    int insert(DrawPrizes record);

    int insertSelective(DrawPrizes record);

    List<DrawPrizes> selectByExample(DrawPrizesExample example);

    DrawPrizes selectByPrimaryKey(Integer prizeId);

    int updateByExampleSelective(@Param("record") DrawPrizes record, @Param("example") DrawPrizesExample example);

    int updateByExample(@Param("record") DrawPrizes record, @Param("example") DrawPrizesExample example);

    int updateByPrimaryKeySelective(DrawPrizes record);

    int updateByPrimaryKey(DrawPrizes record);
}