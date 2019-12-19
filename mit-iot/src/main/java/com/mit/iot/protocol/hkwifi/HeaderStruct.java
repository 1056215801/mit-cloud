package com.mit.iot.protocol.hkwifi;

import com.mit.iot.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.eac.UnsignedInteger;

/**
 * @Description 海康WIFI探针报文头定义，报文头长度为 24 个字节
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeaderStruct {

    public HeaderStruct(byte[] bytes) {
        ByteBuf byteBuf = Unpooled.buffer(24);
        byteBuf.writeBytes(bytes);
        this.totalLength = byteBuf.readUnsignedInt();
        this.checkSum = byteBuf.readUnsignedInt();
        this.singleInfoLength = byteBuf.readUnsignedShort();
        this.infoNum = byteBuf.readUnsignedShort();
        this.infoType = byteBuf.readUnsignedByte();
        this.res1 = byteBuf.readUnsignedByte();
        this.version = byteBuf.readUnsignedShort();
        short[] res = new short[8];
        for (short b : res) {
            b = byteBuf.readUnsignedByte();
        }
        this.res = res;
    }

    /**
     * 报文总长度
     */
    private long totalLength;
    /**
     * 加密秘钥的校验和
     */
    private long checkSum;
    /**
     * 将要接收的单个消息的长度
     */
    private int singleInfoLength;
    /**
     * 将要接收的消息个数
     */
    private int infoNum;
    /**
     * 消息类型
     */
    private short infoType;
    private short res1;
    /**
     * 版本号
     */
    private int version;
    /**
     * 8 个无符号字节长度
     */
    private short[] res;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------报文头部数据-------").append(System.lineSeparator());
        sb.append("|报文总长度-----").append(ByteUtils.byte2HexString(ByteUtils.unsignedInt2Bytes(totalLength))).append(System.lineSeparator());
        sb.append("|加密秘钥校验和--").append(ByteUtils.byte2HexString(ByteUtils.unsignedInt2Bytes(checkSum))).append(System.lineSeparator());
        sb.append("|单个消息长度---").append(ByteUtils.byte2HexString(ByteUtils.unsignedShort2Bytes(singleInfoLength))).append(System.lineSeparator());
        sb.append("|消息个数-------").append(ByteUtils.byte2HexString(ByteUtils.unsignedShort2Bytes(infoNum))).append(System.lineSeparator());
        sb.append("|消息类型-------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(infoType))).append(System.lineSeparator());
        sb.append("|res1----------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(res1))).append(System.lineSeparator());
        sb.append("|版本号---------").append(ByteUtils.byte2HexString(ByteUtils.unsignedShort2Bytes(version))).append(System.lineSeparator());
        sb.append("|res-----------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(res))).append(System.lineSeparator());
        return sb.toString();
    }

}
