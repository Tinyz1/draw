package com.asiainfo.draw.persistence;

import com.asiainfo.draw.domain.DrawScene;
import com.asiainfo.draw.domain.DrawSceneExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DrawSceneMapper {
    int countByExample(DrawSceneExample example);

    int deleteByExample(DrawSceneExample example);

    int deleteByPrimaryKey(Integer sceneId);

    int insert(DrawScene record);

    int insertSelective(DrawScene record);

    List<DrawScene> selectByExample(DrawSceneExample example);

    DrawScene selectByPrimaryKey(Integer sceneId);

    int updateByExampleSelective(@Param("record") DrawScene record, @Param("example") DrawSceneExample example);

    int updateByExample(@Param("record") DrawScene record, @Param("example") DrawSceneExample example);

    int updateByPrimaryKeySelective(DrawScene record);

    int updateByPrimaryKey(DrawScene record);
}