package com.mit.iot.handler;

import cn.hutool.core.util.EnumUtil;
import com.mit.iot.dto.hkwifi.WiFiDeviceStatusDTO;
import com.mit.iot.dto.hkwifi.WiFiGeolocationDTO;
import com.mit.iot.enums.hkwifi.InfoTypeEnum;
import com.mit.iot.model.WiFiProbeAp;
import com.mit.iot.model.WiFiProbeTerminal;
import com.mit.iot.protocol.hkwifi.APStruct;
import com.mit.iot.protocol.hkwifi.DeviceStatusStruct;
import com.mit.iot.protocol.hkwifi.GeolocationStruct;
import com.mit.iot.protocol.hkwifi.HeaderStruct;
import com.mit.iot.protocol.hkwifi.TerminalStruct;
import com.mit.iot.service.IWiFiProbeApService;
import com.mit.iot.service.IWiFiProbeService;
import com.mit.iot.service.IWiFiProbeTerminalService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 海康WIFI探针服务处理类
 */
@Slf4j
public class HkWiFiProtocolHandler extends SimpleChannelInboundHandler<Object> {

    // 最短长度，此处为报文头部长度
    private static final int MIN_LEN = 24;

    @Resource
    private IWiFiProbeService wiFiProbeService;

    @Resource
    private IWiFiProbeApService wiFiProbeApService;

    @Resource
    private IWiFiProbeTerminalService wiFiProbeTerminalService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        log.debug("-----启动海康WIFI探针解码器...");
        log.debug("-----目前数据缓存大小：" + in.readableBytes());

        if (in.readableBytes() < MIN_LEN) {
            log.debug("-----小于最小长度");
            return;
        }

        // 数据头
        byte[] header = new byte[24];
        in.readBytes(header);
        HeaderStruct headerStruct = new HeaderStruct(header);
        log.debug(headerStruct.toString());

        // 数据体
        byte[] body = new byte[in.readableBytes()];
        in.readBytes(body);

        InfoTypeEnum infoType = EnumUtil.likeValueOf(InfoTypeEnum.class, (int) headerStruct.getInfoType());
        switch (infoType) {
            case TERMINAL:
                List<TerminalStruct> terminalStructList = decodeTerminalInfo(headerStruct, body);
                List<WiFiProbeTerminal> terminalList = terminalStructList.stream().map(terminalStruct -> {
                    log.debug(terminalStruct.toString());
                    WiFiProbeTerminal wiFiProbeTerminal = new WiFiProbeTerminal(terminalStruct);
                    log.debug(wiFiProbeTerminal.toString());
                    return wiFiProbeTerminal;
                }).collect(Collectors.toList());
                wiFiProbeTerminalService.processUpStream(terminalList);
                break;
            case AP:
                List<APStruct> apStructList = decodeAPInfo(headerStruct, body);
                List<WiFiProbeAp> apList = apStructList.stream().map(apStruct -> {
                    log.debug(apStruct.toString());
                    WiFiProbeAp wiFiProbeAp = new WiFiProbeAp(apStruct);
                    log.debug(wiFiProbeAp.toString());
                    return wiFiProbeAp;
                }).collect(Collectors.toList());
                wiFiProbeApService.processUpStream(apList);
                break;
            case GEOGRAPHICAL_INFORMATION:
                List<GeolocationStruct> geolocationStructList = decodeGeolocationInfo(headerStruct, body);
                List<WiFiGeolocationDTO> geolocationDTOList = geolocationStructList.stream().map(geolocationStruct -> {
                    log.debug(geolocationStruct.toString());
                    WiFiGeolocationDTO wiFiGeolocationDTO = new WiFiGeolocationDTO(geolocationStruct);
                    log.debug(wiFiGeolocationDTO.toString());
                    return wiFiGeolocationDTO;
                }).collect(Collectors.toList());
                wiFiProbeService.processUpGeolocationStream(geolocationDTOList);
                break;
            case DEVICE_STATUS:
                List<DeviceStatusStruct> deviceStatusStructList = decodeDeviceStatusInfo(headerStruct, body);
                List<WiFiDeviceStatusDTO> deviceStatusDTOList = deviceStatusStructList.stream().map(deviceStatusStruct -> {
                    log.debug(deviceStatusStruct.toString());
                    if ((int)deviceStatusStruct.getType() == 0) {
                        WiFiDeviceStatusDTO wiFiDeviceStatusDTO = new WiFiDeviceStatusDTO(deviceStatusStruct);
                        log.debug(wiFiDeviceStatusDTO.toString());
                        return wiFiDeviceStatusDTO;
                    }
                    return null;
                }).collect(Collectors.toList());
                wiFiProbeService.processUpDeviceStatusStream(deviceStatusDTOList);
                break;
            default:
        }
    }

    /**
     * 解析终端信息
     * @param headerStruct 消息头
     * @param body 消息体字节
     * @return list
     */
    private List<TerminalStruct> decodeTerminalInfo(HeaderStruct headerStruct, byte[] body) {
        ByteBuf byteBuf = Unpooled.buffer(body.length);
        byteBuf.writeBytes(body);
        List<TerminalStruct> list = new ArrayList<>();
        for (int i=0; i<(int)headerStruct.getInfoNum(); i++) {
            int length = byteBuf.readUnsignedShort();
            short[] sourceMacAddr = new short[6];
            for (short b : sourceMacAddr) {
                b = byteBuf.readUnsignedByte();
            }

            long firstAcquisitionTime = byteBuf.readUnsignedInt();
            long lastAcquisitionTime = byteBuf.readUnsignedInt();
            int scanTime = byteBuf.readUnsignedShort();
            short wifiFieldIntensity = byteBuf.readUnsignedByte();
            short historySSIDNum = byteBuf.readUnsignedByte();

            short[][] historySSID = new short[5][32];
            for (int j=0; j<5; j++) {
                for (int k=0; k<32; k++) {
                    historySSID[j][k] = byteBuf.readUnsignedByte();
                }
            }

            short[] indexCode = new short[64];
            for (short b : indexCode) {
                b = byteBuf.readUnsignedByte();
            }

            short[] connectedMacAddr = new short[6];
            for (short b : connectedMacAddr) {
                b = byteBuf.readUnsignedByte();
            }

            short[] phoneBrand = new short[16];
            for (short b : phoneBrand) {
                b = byteBuf.readUnsignedByte();
            }

            short[] res = new short[10];
            for (short b : res) {
                b = byteBuf.readUnsignedByte();
            }

            TerminalStruct terminalStruct = new TerminalStruct(length, sourceMacAddr, firstAcquisitionTime,
                    lastAcquisitionTime, scanTime, wifiFieldIntensity, historySSIDNum, historySSID, indexCode,
                    connectedMacAddr, phoneBrand, res);

            list.add(terminalStruct);
        }
        return list;
    }

    /**
     * 解析AP热点信息
     * @param headerStruct 消息头
     * @param body 消息体字节
     * @return list
     */
    private List<APStruct> decodeAPInfo(HeaderStruct headerStruct, byte[] body) {
        ByteBuf byteBuf = Unpooled.buffer(body.length);
        byteBuf.writeBytes(body);
        List<APStruct> list = new ArrayList<>();
        for (int i=0; i<(int)headerStruct.getInfoNum(); i++) {
            short length = byteBuf.readShort();
            byte[] sourceMacAddr = new byte[6];
            byteBuf.readBytes(sourceMacAddr);

            int firstAcquisitionTime = byteBuf.readInt();
            int lastAcquisitionTime = byteBuf.readInt();
            short scanTime = byteBuf.readShort();
            byte wifiFieldIntensity = byteBuf.readByte();
            byte wifiSpotEncryptType = byteBuf.readByte();

            int channel = byteBuf.readInt();
            byte[] ssid = new byte[32];
            byteBuf.readBytes(ssid);

            byte[] indexCode = new byte[64];
            byteBuf.readBytes(indexCode);
            byte[] res = new byte[32];
            byteBuf.readBytes(res);

            APStruct apStruct = new APStruct(length, sourceMacAddr, firstAcquisitionTime, lastAcquisitionTime,
                    scanTime, wifiFieldIntensity, wifiSpotEncryptType, channel, ssid, indexCode, res);

            list.add(apStruct);
        }
        return list;
    }

    private List<GeolocationStruct> decodeGeolocationInfo(HeaderStruct headerStruct, byte[] body) {
        ByteBuf byteBuf = Unpooled.buffer(body.length);
        byteBuf.writeBytes(body);
        List<GeolocationStruct> list = new ArrayList<>();
        for (int i=0; i<(int)headerStruct.getInfoNum(); i++) {
            short length = byteBuf.readShort();
            byte[] indexCode = new byte[64];
            byteBuf.readBytes(indexCode);

            byte[] longitude = new byte[10];
            byteBuf.readBytes(longitude);
            byte[] latitude = new byte[10];
            byteBuf.readBytes(latitude);
            byte[] siteCode = new byte[14];
            byteBuf.readBytes(siteCode);

            int acquisitionTime = byteBuf.readInt();
            byte[] res = new byte[152];
            byteBuf.readBytes(res);

            GeolocationStruct geolocationStruct = new GeolocationStruct(length, indexCode, longitude, latitude,
                    siteCode, acquisitionTime, res);

            list.add(geolocationStruct);
        }
        return list;
    }

    /**
     * 解析设备状态信息
     * @param headerStruct 消息头
     * @param body 消息体字节
     * @return list
     */
    private List<DeviceStatusStruct> decodeDeviceStatusInfo(HeaderStruct headerStruct, byte[] body) {
        ByteBuf byteBuf = Unpooled.buffer(body.length);
        byteBuf.writeBytes(body);
        List<DeviceStatusStruct> list = new ArrayList<>();
        for (int i=0; i<(int)headerStruct.getInfoNum(); i++) {
            int length = byteBuf.readUnsignedShort();
            short[] indexCode = new short[64];
            for (short b : indexCode) {
                b = byteBuf.readUnsignedByte();
            }

            short status = byteBuf.readUnsignedByte();
            short[] sourceMacAddr = new short[6];
            for (short b : sourceMacAddr) {
                b = byteBuf.readUnsignedByte();
            }

            short type = byteBuf.readUnsignedByte();
            short[] res = new short[2];
            for (short b : res) {
                b = byteBuf.readUnsignedByte();
            }
            long acquisitionTime = byteBuf.readUnsignedInt();

            DeviceStatusStruct deviceStatusStruct = new DeviceStatusStruct(length, indexCode, status, sourceMacAddr,
                    type, res, acquisitionTime);

            list.add(deviceStatusStruct);
        }
        return list;
    }
}
