package Helperclasses;

/**
 * Created by Rowin on 12-4-2017.
 * Helper class to make handling bytes easier
 */

public class ByteHandler {

    public static void main(String args[]){
        // Some testing

        byte a = 0;
        a = setBitAtPosition(a,0,true);
        System.out.println(String.format("%8s", Integer.toBinaryString(a & 0xFF)).replace(' ', '0'));
        a = setBitAtPosition(a,5,true);
        System.out.println(String.format("%8s", Integer.toBinaryString(a & 0xFF)).replace(' ', '0'));
        a = setBitAtPosition(a,0,false);
        System.out.println(String.format("%8s", Integer.toBinaryString(a & 0xFF)).replace(' ', '0'));
        byte b = 127;

        a = byteCopy(b,2,a,2,4);
        System.out.println(String.format("%8s", Integer.toBinaryString(a & 0xFF)).replace(' ', '0'));
        a = 60;
        b = getValueFromBits(a,2,4);
        System.out.println(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
    }

    // Checks a specific bit
    public static boolean getBitAtPosition(byte src, int position) {
        return ((src >> position) & 1) == 1;
    }

    public static boolean getBitAtPosition(int src, int position) {
        return ((src >> position) & 1) == 1;
    }

    public static byte setBitAtPosition(byte dest, int position, boolean value) {
        if(value) {
            return (byte)(dest | (1 << position));
        } else {
            return (byte)(dest & ~(1 << position));
        }
    }

    public static int setBitAtPosition(int dest, int position, boolean value) {
        if(value) {
            return (dest | (1 << position));
        } else {
            return (dest & ~(1 << position));
        }
    }

    public static byte byteCopy(byte src, int srcposition, byte destination, int destposition, int length) {
        byte temp = destination;
            for(int i = 0; i < length; i++) {
               temp = setBitAtPosition(temp, i+destposition,getBitAtPosition(src,i + srcposition));
            }
        return temp;
    }

    public static byte byteCopy(int src, int srcposition, byte destination, int destposition, int length) {
        byte temp = destination;
        for(int i = 0; i < length; i++) {
            temp = setBitAtPosition(temp, i+destposition,getBitAtPosition(src,i + srcposition));
        }
        return temp;
    }

    public static byte getValueFromBits(byte src, int position, int length) {
        byte temp = 0;
        return byteCopy(src,position,temp,0,length);
    }

    public static byte[] intToBytes(int src) {
        byte[2] res = new byte[2];

        res[0] = byteCopy(src,0,res[0],0,8);
        res[1] = byteCopy(src,8,res[1],0,8);

        return res;
    }
}
