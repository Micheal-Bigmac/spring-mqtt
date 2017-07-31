package spring.jt808.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spring.jt808.codec.JT808Header;
import spring.jt808.codec.JT808Message;
import spring.jt808.util.BCD8421Operater;
import spring.jt808.util.CommonUtils;

import java.util.List;

/**
 * Created by Micheal-Bigmac on 2017/7/31.
 */

/**
 * 3.1 读操作
 * 读操作主要提供以下功能：
 * readByte：取1字节的内容；
 * readBoolean：取1字节的内容，返回readByte() != 0；
 * readUnsignedByte：取1字节的内容，返回（(short) (readByte() & 0xFF)）；（能把负数转换为无符号吗？）
 * readShort：取2字节的内容，返回转换后的short类型；
 * readUnsignedShort：取2字节的内容，返回readShort() & 0xFFFF；
 * readMedium：取3字节的内容，返回转换后的int类型；
 * readUnsignedMedium：取3字节的内容，返回转换后的int类型；
 * readInt：取4字节的内容；
 * readUnsignedInt：取4字节的内容，返回readInt() & 0xFFFFFFFFL；
 * readLong：取8字节的内容；
 * readChar：取1字节的内容；
 * readFloat：取4字节的int内容，转换为float类型；
 * readDouble：取8字节的long内容，转换为double类型；
 * readBytes：取指定长度的内容，返回ByteBuf类型；
 * readSlice：取指定长度的内容，返回ByteBuf类型；
 * readBytes：取指定长度的内容到目标容器。
 */


@ChannelHandler.Sharable
public class JT808Decoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(JT808Decoder.class);
    private BCD8421Operater bcdOperator = new BCD8421Operater();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf duplicate = in.duplicate();

        JT808Message Message = new JT808Message();
        if (in.readableBytes() < 0) {
            return;
        }
        JT808Header header = parseJT808Header(in);
        Message.setMsgHeader(header);
        int MsgBodyBytesStartIndex = 12;
        if (header.isHasSubPackage()) {
            MsgBodyBytesStartIndex = 16;
        }

        byte[] tmp = new byte[header.getMsgBodyLength()];
        in.readBytes(tmp, MsgBodyBytesStartIndex, tmp.length);
        Message.setMsgBodyBytes(tmp);

        int checkSumPackage = (int) in.readByte();  //  取出最后一位 包中的校验和
        int checkSumForJT808 = CommonUtils.getCheckSumForJT808(duplicate.array(), 0, duplicate.readableBytes());
        if (checkSumForJT808 != checkSumPackage) {
            logger.error(" CheckSum is different ");
        } else {
            out.add(Message);
        }

    }


    public JT808Header parseJT808Header(ByteBuf in) {
        JT808Header header = new JT808Header();
        header.setMsgId(in.readShort());  //   0 2 字节 解析
        short msgBodyPropsField = in.readShort();
        header.setMsgBodyPropsField(msgBodyPropsField);  // 2 4 字节解析

        header.setMsgBodyLength(msgBodyPropsField & 0x3ff); // 消息体长度
        header.setEncryptionType((msgBodyPropsField & 0x1c00 >> 10));  // 是否加密
        header.setHasSubPackage(((msgBodyPropsField & 0x2000) >> 13) == 1); //是否有子包
        header.setReservedBit(((msgBodyPropsField & 0xc000) >> 14) + "");  // 保留位
//        ByteBuf bcdData=ctx.alloc().buffer(6);
//        ByteBuf bcdData2= Unpooled.buffer(6);
        byte[] tmp = new byte[6];
        in.readBytes(tmp, 4, 6);
        header.setTerminalPhone(bcdOperator.bcd2String(tmp));
        header.setFlowId(in.readShort());

        if (header.isHasSubPackage()) {
            header.setPackageInfoField(in.readInt());   // 消息包封装项
            header.setTotalSubPackage(in.readShort());  // 消息包总数
            header.setSubPackageSeq(in.readShort());
        }
        return header;
    }
}
