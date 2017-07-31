import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * Created by Micheal-Bigmac on 2017/7/31.
 Derived buffers
 duplicate 返回当前ByteBuf的复制对象，共享缓冲区，读写索引独立
 copy复制一个新的ByteBuf对象，内容和索引都独立
 slice 可读子缓冲区(起始位置从readerlndex到 writerlndex)，共享内容，读写索引独立维护
 */
public class TestByte {
    public static void main(String []args){
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(100);
        for( int i=0; i< 10 ; i++){
            buf.writeByte(i);
        }

        System.out.println(buf);
        System.out.println(" first length current is "+ buf.readableBytes());
        ByteBuf duplicate = buf.duplicate();
        byte b = buf.readByte();
        System.out.println((int)b);
        byte c = buf.readByte();
        System.out.println((int)c);

        int i = buf.readableBytes();
        System.out.println(" length current is "+i);

        System.out.println("==========================");
        System.out.println(" duplicate length "+duplicate.readableBytes());
        System.out.println(duplicate);
    }
}
