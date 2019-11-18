package com.mit.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mit.event.dto.EventBaseInfoQueryDTO;
import com.mit.event.model.EventBaseInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Mapper
public interface EventBaseInfoMapper extends BaseMapper<EventBaseInfo> {

    @SelectKey(keyProperty = "id", resultType = String.class, before = true,
            statement = "select uuid() as id from dual")
    @Insert("insert into event_base_info (" +
            "id, event_name, event_code, happened_time, emergency_level, severity, community_code, " +
            "longitude, latitude, status, process_instance_id) " +
            "values (#{id}, #{eventName}, #{eventCode}, #{happenedTime}, #{emergencyLevel}, #{severity}, " +
            "#{communityCode}, #{longitude}, #{latitude}, #{status}, #{processInstanceId})")
    int insertWithAutoUuid(EventBaseInfo eventBaseInfo);

    @Select("select b.* from event_base_info b, event_config c where b.event_code = c.event_code and " +
            "c.source = #{eventSource} order by b.happened_time desc")
    List<EventBaseInfo> getBaseInfoByEventSource(String eventSource);

    @Select("select b.* from event_base_info b, event_config c where b.event_code = c.event_code and " +
            "c.source = #{eventSource} and b.status in #{status} order by b.happened_time desc")
    List<EventBaseInfo> getBaseInfoByEventSourceAndStatusIn(String eventSource, List<Integer> status);

    @SelectProvider(type = BaseInfoQueryProvider.class, method = "queryBaseInfoByCondition")
    List<EventBaseInfo> getBaseInfoByCondition(EventBaseInfoQueryDTO eventBaseInfoQueryDTO);

    class BaseInfoQueryProvider {
        public String queryBaseInfoByCondition(EventBaseInfoQueryDTO baseInfoQueryDTO) {
            StringBuffer sql = new StringBuffer("select b.* from event_base_info as b, event_config as c where 1=1 ");
            if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getEventCode())) {
                sql.append("and c.event_code in #{eventCode} ");
            }
            if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getClassification())) {
                sql.append("and c.classification in #{classification} ");
            }
            if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getType())) {
                sql.append("and c.type in #{type} ");
            }
            if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getSource())) {
                sql.append("and c.source in #{source} ");
            }
            if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getStatus())) {
                sql.append("and b.status in #{status} ");
            }
            if (baseInfoQueryDTO.getHappenedTimeStart() != null) {
                sql.append("and b.happened_time >= #{happenedTimeStart} ");
            }
            if (baseInfoQueryDTO.getHappenedTimeEnd() != null) {
                sql.append("and b.happened_time <= #{happenedTimeEnd} ");
            }
            sql.append("and b.event_code = c.event_code order by b.happened_time desc ");
            if (baseInfoQueryDTO.getPageNumber() != null && baseInfoQueryDTO.getPageSize() != null) {
                sql.append("limit #{offset},#{limit}");
            }
            return sql.toString();
        }
    }

}
