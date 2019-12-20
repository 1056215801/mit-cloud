package com.mit.iot.handler;

import com.mit.common.utils.SpringUtils;
import com.mit.iot.dto.sensor.SensorSamplingDevice1To4Data;
import com.mit.iot.protocol.sensor.Type01Device1To4Struct;
import com.mit.iot.service.IFireHydrantService;
import com.mit.iot.service.impl.FireHydrantServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 服务handler处理
 */
@Slf4j
public class SensorProtocolHandler extends SimpleChannelInboundHandler<Object> {

    private static IFireHydrantService fireHydrantService;

    static {
        fireHydrantService = SpringUtils.getBean(FireHydrantServiceImpl.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof Type01Device1To4Struct) {
            SensorSamplingDevice1To4Data data = new SensorSamplingDevice1To4Data((Type01Device1To4Struct) msg);
            log.debug(data.toString());
            fireHydrantService.processUpDataStream(data);
        }
    }
}
