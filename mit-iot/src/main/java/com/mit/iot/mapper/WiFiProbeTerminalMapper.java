package com.mit.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mit.iot.model.WiFiProbeTerminal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WiFiProbeTerminalMapper extends BaseMapper<WiFiProbeTerminal> {
    /**
     * 根据终端mac地址、indexCode获取终端信息
     * 两个条件确定唯一一条记录
     * @param terminalMac 终端mac
     * @param indexCode 海康平台的索引
     * @return 如果存在返回唯一的一条记录，不存在则返回空
     */
    @Select("select t.* from wifi_probe_terminal t where t.terminal_mac = #{terminalMac} and t.index_code = #{indexCode}")
    WiFiProbeTerminal selectByTerminalMacAndIndexCode(String terminalMac, String indexCode);
}
