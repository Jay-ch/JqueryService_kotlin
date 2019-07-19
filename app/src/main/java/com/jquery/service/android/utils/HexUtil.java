package com.jquery.service.android.utils;


import java.io.UnsupportedEncodingException;

public class HexUtil {
    private static final int[] DEC = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15};
    private static final byte[] HEX = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    private static final char[] hex = "0123456789abcdef".toCharArray();

    public HexUtil() {
    }

    public static int getDec(int index) {
        try {
            return DEC[index - 48];
        } catch (ArrayIndexOutOfBoundsException var2) {
            return -1;
        }
    }

    public static byte getHex(int index) {
        return HEX[index];
    }

    public static String hexString2BinaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }


    public static String binaryString2HexString(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

    public static String toHexString(byte[] bytes) {
        if (null == bytes) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder(bytes.length << 1);

            for (int i = 0; i < bytes.length; ++i) {
                sb.append(hex[(bytes[i] & 240) >> 4]).append(hex[bytes[i] & 15]);
            }

            return sb.toString();
        }
    }

    public static byte[] fromHexString(String input) {
        if (input == null) {
            return null;
        } else if ((input.length() & 1) == 1) {
            throw new IllegalArgumentException("The input must consist of an even number of hex digits");
        } else {
            char[] inputChars = input.toCharArray();
            byte[] result = new byte[input.length() >> 1];

            for (int i = 0; i < result.length; ++i) {
                int upperNibble = getDec(inputChars[2 * i]);
                int lowerNibble = getDec(inputChars[2 * i + 1]);
                if (upperNibble < 0 || lowerNibble < 0) {
                    throw new IllegalArgumentException("The input must consist only of hex digits");
                }

                result[i] = (byte) ((upperNibble << 4) + lowerNibble);
            }

            return result;
        }
    }

    public static String decodeFromHexString(String hexString) {
        return new String(fromHexString(hexString));
    }

    /**
     * 高低位交换
     *
     * @param data           被交换数据，长度为switchUnitSize的整数倍
     * @param switchUnitSize 交换单元字节数，2的倍数
     * @return
     */
    public static byte[] byteHighLowSwitch(byte[] data, int switchUnitSize) {
        if (data == null || data.length < switchUnitSize
                || (switchUnitSize < 2 && switchUnitSize % 2 != 0)
                || data.length % switchUnitSize != 0)
            return null;
        int switchUnitNum = data.length / switchUnitSize;
        int byteLen = switchUnitSize / 2;
        byte[] result = new byte[data.length];
        for (int i = 0; i < switchUnitNum; i++) {
            for (int j = 0; j < byteLen; j++) {
                result[(i + 1) * switchUnitSize - byteLen * 2 + j] = data[(i + 1) * switchUnitSize - byteLen + j];
                result[(i + 1) * switchUnitSize - byteLen + j] = data[(i + 1) * switchUnitSize - byteLen * 2 + j];
            }
        }
        return result;
    }

    public static byte[] byteReverse(byte[] data) {
        if (data == null) return null;
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++)
            result[i] = data[data.length - i - 1];
        return result;
    }

    /**
     * 字节数组转换为ASCII码
     *
     * @param b
     * @return
     */
    public static String byteToAscii(byte[] b) {

        String s = new String(b);

        char[] chars = s.toCharArray(); //把字符中转换为字符数组

        String temp = "";

        for (int i = 0; i < chars.length; i++) {//输出结果

            temp = temp + chars[i];
        }
        return temp;
    }

    /**
     * AscII码转换为字节数组
     *
     * @param str
     * @return
     */
    public static byte[] asciiToByte(String str) {
        try {
            byte[] tBytes = str.getBytes("US-ASCII");
            return tBytes;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean byteArrayCompare(byte[] b1, byte[] b2) {
        if (null == b1 || null == b2)
            return false;
        else if (b1.length != b2.length)
            return false;
        else {
            for (int i = 0; i < b1.length; i++) {
                if (b1[i] != b2[i])
                    return false;
            }
            return true;
        }
    }


    public static String byteArray2HexString(byte[] b, boolean flag) {
        StringBuffer buffer = new StringBuffer();
        int temp = 0;

        for (int i = 0; i < b.length; i++) {
            if (b[i] < 0)
                temp = 256 + b[i];
            else
                temp = b[i];

            String ts = Integer.toHexString(temp);
            if (ts.length() == 1) {
                if (flag)
                    buffer.append("0" + ts + " ");
                else
                    buffer.append("0" + ts);
            } else {
                if (flag)
                    buffer.append(ts + " ");
                else
                    buffer.append(ts);
            }
        }
        return buffer.toString();
    }

    /**
     * 十六进制字符串->字节数组
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStringTobyteArray(String hexString) {
        int length = hexString.length();
        if (length % 2 != 0) {
            hexString = "0" + hexString;
            length = hexString.length();
        }

        byte[] b = new byte[length / 2];

        for (int i = 0; i < length / 2; i++) {
            b[i] = (byte) ((int) Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16));
        }

        return b;
    }

    /**
     * 去掉16进制字符串前面的0X
     *
     * @param hexString
     * @return
     */
    public static String remove0x(String hexString) {
        if (hexString.toUpperCase().startsWith("0X")) {
            hexString = hexString.substring(2);
        }
        return hexString;
    }

//    public static String bytesToBinString(byte[] data) {
//        StringBuffer s = new StringBuffer();
//        if (data == null || data.length <= 0) return "";
//        for (byte b : data)
//            s.append(StringUtils.leftPad(Integer.toBinaryString(b & 0xFF), 8, "0"));
//        return s.toString();
//    }

    /**
     * IEEE754
     *
     * @param a
     * @return
     */
    public static byte[] floatToBytes(float a) {
        byte[] data = new byte[4];
        if (a == 0) {
            for (int i = 0; i < 4; i++) {
                data[i] = 0x00;
            }
            return data;
        }
        Integer[] intdata = {0, 0, 0, 0};
        a = Math.abs(a);
        // 首先将浮点数转化为二进制浮点数
        float floatpart = a % 1;
        int intpart = (int) (a / 1);

        System.out.println(intpart + " " + floatpart);
        // 将整数部分化为2进制,并转化为string类型
        String intString = "";
        String floatString = "";
        String result = "";
        String subResult = "";
        int zhishu = 0;
        if (intpart == 0) {
            intString += "0";
        }
        while (intpart != 0) {
            intString = intpart % 2 + intString;
            intpart = intpart / 2;
        }
        while (floatpart != 0) {
            floatpart *= 2;
            if (floatpart >= 1) {
                floatString += "1";
                floatpart -= 1;
            } else {
                floatString += "0";
            }

        }

        result = intString + floatString;
        System.out.println(intString + "." + floatString);
        intpart = (int) (a / 1);
        if (intpart > 0) {// 整数部分肯定有1，且以1开头..这样的话，小数点左移
            zhishu = intString.length() - 1;
        } else {// 整数位为0，右移
            for (int i = 0; i < floatString.length(); i++) {
                zhishu--;
                if (floatString.charAt(i) == '1') {
                    break;
                }
            }
            // while(floatString.charAt(index)){}
        }
        // 对指数进行移码操作

        if (zhishu >= 0) {
            subResult = result.substring(intString.length() - zhishu);
        } else {
            subResult = floatString.substring(-zhishu);
        }
        zhishu += 127;
        if (subResult.length() <= 7) {// 若长度

            for (int i = 0; i < 7; i++) {
                if (i < subResult.length()) {
                    intdata[1] = intdata[1] * 2 + subResult.charAt(i) - '0';
                } else {
                    intdata[1] *= 2;
                }

            }

            if (zhishu % 2 == 1) {// 如果质数是奇数，则需要在这个最前面加上一个‘1’
                intdata[1] += 128;
            }
            data[1] = intdata[1].byteValue();
        } else if (subResult.length() <= 15) {// 长度在（7,15）以内
            int i = 0;
            for (i = 0; i < 7; i++) {// 计算0-7位，最后加上第一位
                intdata[1] = intdata[1] * 2 + subResult.charAt(i) - '0';
            }
            if (zhishu % 2 == 1) {// 如果质数是奇数，则需要在这个最前面加上一个‘1’
                intdata[1] += 128;
            }
            data[1] = intdata[1].byteValue();

            for (i = 7; i < 15; i++) {// 计算8-15位
                if (i < subResult.length()) {
                    intdata[2] = intdata[2] * 2 + subResult.charAt(i) - '0';
                } else {
                    intdata[2] *= 2;
                }

            }
            data[2] = intdata[2].byteValue();
        } else {// 长度大于15
            int i = 0;
            for (i = 0; i < 7; i++) {// 计算0-7位，最后加上第一位
                intdata[1] = intdata[1] * 2 + subResult.charAt(i) - '0';
            }
            if (zhishu % 2 == 1) {// 如果质数是奇数，则需要在这个最前面加上一个‘1’
                intdata[1] += 128;
            }
            data[1] = intdata[1].byteValue();

            for (i = 7; i < 15; i++) {// 计算8-15位
                intdata[2] = intdata[2] * 2 + subResult.charAt(i) - '0';
            }
            data[2] = intdata[2].byteValue();

            for (i = 15; i < 23; i++) {// 计算8-15位
                if (i < subResult.length()) {
                    intdata[3] = intdata[3] * 2 + subResult.charAt(i) - '0';
                } else {
                    intdata[3] *= 2;
                }

            }
            data[3] = intdata[3].byteValue();
        }

        intdata[0] = zhishu / 2;
        if (a < 0) {
            intdata[0] += 128;
        }
        data[0] = intdata[0].byteValue();
        byte[] data2 = new byte[4];// 将数据转移，目的是倒换顺序
        for (int i = 0; i < 4; i++) {
            data2[i] = data[3 - i];
        }
        return data2;
    }

 /*   public static String ipV4(byte[] b) {
        String ip = "";
        for (int i = 0; i < b.length; i++) {
            int temp = 0;
            if (b[i] < 0)
                temp = 256 + b[i];
            else
                temp = b[i];
            ip = ip + temp + ".";
        }
        if (StringUtils.isNotBlank(ip)) {
            ip = ip.substring(0, ip.length() - 1);
        }
        return ip;
    }*/

    /**
     * Short Int转byte[]
     *
     * @param s
     * @return
     */
    public static byte[] shortToByteArray(short s) {
        byte[] shortBuf = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (shortBuf.length - 1 - i) * 8;
            shortBuf[i] = (byte) ((s >>> offset) & 0xFF);
        }
        return shortBuf;
    }

    /**
     * byte[]转Short Int
     *
     * @param b
     * @return
     */
    public static int byteArrayToShort(byte[] b) {
        return (b[0] << 8)
                + (b[1] & 0xFF);
    }

    /**
     * Object转double
     *
     * @param sma
     * @return
     */
    public static double objToDouble(Object sma) {
        double amount = 0.0;
        if (sma instanceof Integer) {
            amount = (Integer) sma / 1.0;
        } else if (sma instanceof String) {
            amount = Double.parseDouble((String) sma);
        } else {
            amount = (double) sma;
        }
        return amount;
    }

    /**
     * int转byte[]
     *
     * @param n
     * @return
     */
    public static byte[] intToBytes2(int n) {
        byte[] b = new byte[4];

        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (n >> (24 - i * 8));

        }
        return b;
    }

    /**
     * byte[]转int
     *
     * @param b
     * @return
     */
    public static int byteToInt2(byte[] b) {
        int mask = 0xff;
        int temp = 0;
        int n = 0;
        for (int i = 0; i < b.length; i++) {
            n <<= 8;
            temp = b[i] & mask;
            n |= temp;
        }
        return n;
    }

       /**
     * int 转 hex ,高位在前
     * @param inn int
     * @return hex
     */
    public static String IntToHexReversed(int inn) {
        return toHexString(byteReverse(intToBytes2(inn)));

    }

    /**
     * int 转 hex ,高位在前
     * @param inn int
     * @return hex
     */
    public static String intToHex(int inn) {
        return toHexString(intToBytes2(inn));

    }

    /**
     * short 转 hex ,高位在前
     * @param inn int
     * @return hex
     */
    public static String shortToHex(short inn) {
        return toHexString(shortToByteArray(inn));

    }

    /**
     * int 转 hex ,高位在前
     * @param hex hex
     * @return ascii2 string
     */
    public static String hexString2Ascii(String hex) {

        return byteToAscii(hexStringTobyteArray(hex));
    }

    /**
     * ascii 转 hex ,高位在前
     * @param asciiStr String
     * @return hex string
     */
    public static String asciiToHex(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString((int) ch));
        }
        return hex.toString();
    }


    /**
     * 将BCD码转成String
     *
     * @param b byte []
     * @return
     */
    public static String bcdToString(byte [] b){
        StringBuffer sb = new StringBuffer ();
        for (int i = 0;i < b.length;i++ )
        {
            int h = ((b [i] & 0xff) >> 4) + 48;
            sb.append ((char) h);
            int l = (b [i] & 0x0f) + 48;
            sb.append ((char) l);
        }

        return sb.toString () ;
    }


    /**
     * @功能: BCD码转为10进制串(阿拉伯数据)
     * @参数: BCD码
     * @结果: 10进制串
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
                .toString().substring(1) : temp.toString();
    }

    public static short BcdToHex(short data)
    {
        short temp;

        temp = (short) ((data >> 4) * 10 + (data & 0x0f));
        return temp;
    }

//    public static int HexToInt(String value) throws Exception{
//        String v=StringUtils.trimToEmpty(value).toUpperCase();
//        if (v.length()>0){
//            if (v.startsWith("0X"))
//                v=v.substring(2);
//            if (v.length()>8)
//                throw new Exception("Invalid hex integer value.");
//            BigInteger bi = new BigInteger(value, 16);
//            int a=bi.intValue();
//            return a;
//        }
//        else throw new Exception("Value can't be null.");
//    }

//    public static short HexToShort(String value) throws Exception{
//        String v=StringUtils.trimToEmpty(value).toUpperCase();
//        if (v.length()>0){
//            if (v.startsWith("0X"))
//                v=v.substring(2);
//            if (v.length()>4)
//                throw new Exception("Invalid hex short value.");
//            BigInteger bi = new BigInteger(value, 16);
//            short a=bi.shortValue();
//            return a;
//        }
//        else throw new Exception("Value can't be null.");
//    }

}
