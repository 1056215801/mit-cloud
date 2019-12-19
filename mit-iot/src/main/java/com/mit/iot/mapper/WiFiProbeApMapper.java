package com.mit.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mit.iot.model.WiFiProbeAp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WiFiProbeApMapper extends BaseMapper<WiFiProbeAp> {
    /**
     * 根据AP热点mac地址、indexCode获取终端信息
     * 两个条件确定唯一一条记录
     * @param apMac AP热点mac
     * @param indexCode 海康平台的索引
     * @return 如果存在返回唯一的一条记录，不存在则返回空
     */
    @Select("select t.* from wifi_probe_ap t where t.ap_mac = #{apMac} and t.index_code = #{indexCode}")
    WiFiProbeAp selectByApMacAndIndexCode(String apMac, String indexCode);
}
