package com.mit.community.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mit.community.entity.hik.PersonFaceImages;
import com.mit.community.entity.hik.Vo.FaceComparsionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonFaceImagesMapper extends BaseMapper<PersonFaceImages> {

    List<FaceComparsionVo> getFaceComparison(@Param("name") String name);
}
