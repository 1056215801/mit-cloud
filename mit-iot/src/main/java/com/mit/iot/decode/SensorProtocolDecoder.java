package com.mit.iot.decode;

import com.mit.iot.enums.sensor.SensorDeviceTypeEnum;
import com.mit.iot.protocol.sensor.BaseStruct;
import com.mit.iot.protocol.sensor.Type01Device1To4Struct;
import com.mit.iot.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Description 传感器设备解码类
 */
@Slf4j
public class SensorProtocolDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        log.debug("-----启动传感器解码器...");
        log.debug("-----目前数据缓存大小：" + in.readableBytes());

        if (!checkByte(in)) {
            return;
        }

        // 数据类型
        byte type = in.readByte();
        // 数据体长度
        short length = in.readShort();
        byte[] body = null;
        if (length > 0) {
            // 读取数据体
            body = new byte[length];
            in.readBytes(body);
        }
        // 校验值
        short crc = in.readShort();
        // 帧尾
        byte[] tail = new byte[3];
        tail[0] = in.readByte();
        tail[1] = in.readByte();
        tail[2] = in.readByte();

        if (!BaseStruct.tail_string.equals(new String(tail))) {
            log.debug("-----帧尾格式不符，停止解析");
        }

        BaseStruct protocol = decode2Object(type, length, body, crc);
        list.add(protocol);
        log.debug("-----完成解析");
    }

    /**
     * 校验数据格式，长度、帧头
     * @param byteBuf 数据
     * @return 符合格式返回true，否则返回false
     */
    private boolean checkByte(ByteBuf byteBuf) {
        // 长度必须大于基本最小长度
        if (byteBuf.readableBytes() < BaseStruct.MIN_LEN) {
            log.debug("-----数据长度小于最小长度");
            return false;
        }
        // 防止socket字节流攻击，客户端传来的数据过大，对数据进行过滤掉
        if (byteBuf.readableBytes() >= 4096) {
            byteBuf.skipBytes(byteBuf.readableBytes());
            log.debug("-----数据过长");
            return false;
        }

        //记录包头开始位置
        int beginReader = 0;
        while(true) {
            beginReader = byteBuf.readerIndex(); //记录包头开始位置
            byteBuf.markReaderIndex(); //标记包头开始index
            //读取协议开始标志
            if(byteBuf.readInt() == BaseStruct.header_int){
                break; //如果是开始标记，那么就结束查找
            }
            //如果找不到包头，这里要一个一个字节跳过
            byteBuf.resetReaderIndex();
            byteBuf.readByte();
            //当跳过后，如果数据包又不符合长度的，结束本次协议解析
            if(byteBuf.readableBytes() < BaseStruct.MIN_LEN){
                log.debug("-----数据不符合协议格式");
                return false;
            }
        }
        return true;
    }

    /**
     * 将解析出来的数据转换为对象
     * @param type 数据类型
     * @param length 数据长度
     * @param body 数据体
     * @param crc 校验值
     * @return BaseProtocol
     */
    private BaseStruct decode2Object(byte type, short length, byte[] body, short crc) {
        BaseStruct protocol = null;
        // 根据不同的数据类型解析对应的数据体
        switch (ByteUtils.byte2HexString(type)) {
            // 传感器采样数据，上行
            case "01":
                // 设备类型，不同设备类型解析不同的Sample_data
                String deviceType = ByteUtils.byte2HexString(body[0]);
                if (SensorDeviceTypeEnum.TYPE01.getHexString().equals(deviceType) ||
                        SensorDeviceTypeEnum.TYPE02.getHexString().equals(deviceType) ||
                        SensorDeviceTypeEnum.TYPE03.getHexString().equals(deviceType) ||
                        SensorDeviceTypeEnum.TYPE04.getHexString().equals(deviceType)) {
                    protocol = new Type01Device1To4Struct(type, length, body, crc);
                }
                break;
            default:
                protocol = new BaseStruct();
                break;
        }
        return protocol;
    }

}
