package com.mit.community.entity.hik;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author qishengjun
 * @Date Created in 17:18 2020/1/8
 * @Company: mitesofor </p>
 * @Description:~
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventInfoDTO {
            private String communityCode;
            private String communityName;
            private Integer emergencyLevel;
            private String eventCode;
            private String extension;
            private Date happenedTime;
            private String latitude;
            private String location;
            private String longitude;
            private Integer severity;
            private String zoneId;
            private String zoneName;
}
