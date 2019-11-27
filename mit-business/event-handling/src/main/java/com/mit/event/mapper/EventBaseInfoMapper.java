package com.mit.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mit.event.dto.EventBaseInfoQueryDTO;
import com.mit.event.model.EventBaseInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

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

    @Select("select status as 'key', count(*) as 'value' from event_base_info group by status")
    List<Map<String, Object>> metricsByStatus();

    @Select("select c.type as 'key', count(*) as 'value' from event_base_info b, event_config c " +
            "where b.event_code = c.event_code group by c.type")
    List<Map<String, Object>> metricsByType();

    @Select("select c.source as 'source', b.emergency_level as 'emergencyLevel', count(*) as 'value' from event_base_info b, event_config c " +
            "where b.event_code = c.event_code and b.emergency_level is not null group by c.source, b.emergency_level")
    List<Map<String, Object>> metricsBySourceAndEmergencyLevel();

    /**
     * 根据事件来源、状态及时间统计
     * @param startTime yyyy-MM-dd
     * @param endTime yyyy-MM-dd
     */
    @Select("select c.source as 'source', b.status as 'status', count(*) as 'value' from event_base_info b, event_config c " +
            "where b.event_code = c.event_code and concat(year(happened_time),'-',month(happened_time),'-',day(happened_time)) " +
            "between #{startTime} and #{endTime} group by c.source, b.status")
    List<Map<String, Object>> metricsBySourceAndStatusBetweenTime(@Param("startTime") String startTime,
                                                                  @Param("endTime")String endTime);

    /**
     * 根据事件类型及时间统计
     * @param startTime yyyy-MM
     * @param endTime yyyy-MM
     */
    @Select("select c.type as 'type', concat(year(happened_time),'-',month(happened_time)) as 'time', count(*) as 'value' " +
            "from event_base_info b, event_config c " +
            "where b.event_code = c.event_code and " +
            "concat(year(happened_time),'-',month(happened_time)) between #{startTime} and #{endTime}" +
            "group by c.type, concat(year(happened_time),'-',month(happened_time)) " +
            "order by c.type asc, concat(year(happened_time),'-',month(happened_time)) asc")
    List<Map<String, Object>> metricsByTypeBetweenTime(@Param("startTime") String startTime,
                                                       @Param("endTime")String endTime);


    class BaseInfoQueryProvider {
        public String queryBaseInfoByCondition(EventBaseInfoQueryDTO baseInfoQueryDTO) {
            StringBuffer sql = new StringBuffer("<script>");
            sql.append("select b.* from event_base_info as b, event_config as c where 1=1 ");
            if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getEventCode())) {
                sql.append("and c.event_code in ");
                sql.append("<foreach collection='eventCode' item='item' open='(' separator=',' close=')'>#{item}</foreach>");
            }
            if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getClassification())) {
                sql.append("and c.classification in ");
                sql.append("<foreach collection='classification' item='item' open='(' separator=',' close=')'>#{item}</foreach>");
            }
            if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getType())) {
                sql.append("and c.type in ");
                sql.append("<foreach collection='type' item='item' open='(' separator=',' close=')'>#{item}</foreach>");
            }
            if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getSource())) {
                sql.append("and c.source in ");
                sql.append("<foreach collection='source' item='item' open='(' separator=',' close=')'>#{item}</foreach>");
            }
            if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getStatus())) {
                sql.append("and b.status in ");
                sql.append("<foreach collection='status' item='item' open='(' separator=',' close=')'>#{item}</foreach>");
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
            sql.append("</script>");
            return sql.toString();
        }
    }

}
