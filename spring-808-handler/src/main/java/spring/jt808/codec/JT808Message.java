package spring.jt808.codec;

import com.alibaba.fastjson.annotation.JSONField;
import io.netty.channel.Channel;
import spring.mqtt.api.protocol.Protocol;

import java.util.Arrays;

/**
 * Created by Micheal-Bigmac on 2017/7/31.
 */
public class JT808Message implements Protocol {
    /**
     * 16byte 消息头
     */
    private JT808Header msgHeader;

    // 消息体字节数组
    @JSONField(serialize=false)
    protected byte[] msgBodyBytes;

    /**
     * 校验码 1byte
     */
    protected int checkSum;

    @JSONField(serialize=false)
    protected Channel channel;

    public JT808Header getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(JT808Header msgHeader) {
        this.msgHeader = msgHeader;
    }

    public byte[] getMsgBodyBytes() {
        return msgBodyBytes;
    }

    public void setMsgBodyBytes(byte[] msgBodyBytes) {
        this.msgBodyBytes = msgBodyBytes;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "PackageData [msgHeader=" + msgHeader + ", msgBodyBytes=" + Arrays.toString(msgBodyBytes) + ", checkSum="
                + checkSum + ", address=" + channel + "]";
    }

}
