package com.mit.iot.dto.sensor;

import cn.hutool.core.util.EnumUtil;
import com.mit.iot.enums.sensor.DataStatusEnum;
import com.mit.iot.enums.sensor.MeasurementUnitEnum;
import com.mit.iot.protocol.sensor.Type01Device1To4Struct;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 传感器采样数据，设备类型为 0x01、0x02、0x03、0x04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SensorSamplingDevice1To4Data extends SensorSamplingBaseData {

    public SensorSamplingDevice1To4Data(Type01Device1To4Struct protocol) {
        super(protocol);
        this.dataStatus = protocol.getDataStatus();
        this.measurementUnitInt = protocol.getMeasurementUnit();
        this.measurementUnitString = EnumUtil.likeValueOf(MeasurementUnitEnum.class, measurementUnitInt).getValue();
        StringBuilder sb = new StringBuilder();
        if (measurementUnitInt == MeasurementUnitEnum.TEMPERATURE.getKey()) {
            // 为温度时
            if (protocol.getPosition1() == 1) {
                // 标识负值
                sb.append("-");
            }
            sb.append(protocol.getPosition2())
                    .append(protocol.getPosition3())
                    .append(protocol.getPosition4())
                    .append(protocol.getPosition5());
        } else {
            sb.append(protocol.getPosition1())
                    .append(protocol.getPosition2())
                    .append(protocol.getPosition3())
                    .append(protocol.getPosition4())
                    .append(protocol.getPosition5());
        }
        int decimalPointPosition = protocol.getDecimalPointPosition();
        String temp = sb.toString();
        this.value = (decimalPointPosition != 0) ? temp.substring(0, temp.length() - decimalPointPosition) + "." +
                temp.substring(temp.length() - decimalPointPosition)
                : temp;
    }

    /**
     * 数据状态
     */
    private int dataStatus;
    /**
     * 单位
     */
    private int measurementUnitInt;
    private String measurementUnitString;

    /**
     * 数据值
     */
    private String value;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("|数据状态---").append(dataStatus).append("---")
                .append(EnumUtil.likeValueOf(DataStatusEnum.class, dataStatus).getDesc()).append(System.lineSeparator());
        sb.append("|数据值-----").append(value).append(System.lineSeparator());
        sb.append("|计量单位---").append(measurementUnitInt).append("---").append(measurementUnitString).append(System.lineSeparator());
        return sb.toString();
    }

}
