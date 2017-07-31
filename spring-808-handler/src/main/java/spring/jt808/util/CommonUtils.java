package spring.jt808.util;

/**
 * Created by Micheal-Bigmac on 2017/7/31.
 */
public class CommonUtils {

    public static int getCheckSumForJT808(byte[] buf, int start, int end){
        if(start<0 || end > buf.length){
            throw new ArrayIndexOutOfBoundsException("ArrayOutOfBound ");
        }
        int checksum=0;
        for(byte tmp : buf){
            checksum ^= tmp;
        }
        return checksum;
    }
}
