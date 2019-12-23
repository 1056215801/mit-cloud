package com.mit.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mit.event.model.ControlPersonnelAppear;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;

@Mapper
public interface ControlPersonnelAppearMapper extends BaseMapper<ControlPersonnelAppear> {

    @SelectKey(keyProperty = "id", resultType = String.class, before = true,
            statement = "select uuid() as id from dual")
    @Insert("insert into event_control_personnel_appear (" +
            "id, event_base_info_id, capture_image_url, capture_time, username, sex, certificate_type," +
            "certificate_number, person_type, device_name, device_code) " +
            "values (#{id}, #{eventBaseInfoId}, #{captureImageUrl}, #{captureTime}, #{username}, " +
            "#{sex}, #{certificateType}, #{certificateNumber}, #{personType}, #{deviceName}, #{deviceCode})")
    int insert(ControlPersonnelAppear controlPersonnelAppear);
}
