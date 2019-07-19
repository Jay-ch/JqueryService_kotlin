package com.jquery.service.android.entity;

/**
 * created by caiQiang on 2019/3/19 0019.
 * e-mail:cq807077540@foxmail.com
 * <p>
 * description:
 */

public class InfraredHeadEntity {

    //起始符
    private String startTag ;
    //头标识
    private String heardTag;
    //报文长度
    private String messageLength;
    //协议版本
    private String version;
    //功能码
    private String funcation;
    //传送方向
    private String distance;
    //从站编号
    private String sendOrResopnce;
    //请求响应标志
    private String deviceNumber;
    //报文ID
    private String messageId;
    //加密位
    private String encrypt;
    //数据域长度
    private String length;


    //optional parameters
    private boolean isGraphicsCardEnabled;
    private boolean isBluetoothEnabled;




    public boolean isGraphicsCardEnabled() {
        return isGraphicsCardEnabled;
    }

    public boolean isBluetoothEnabled() {
        return isBluetoothEnabled;
    }

    private InfraredHeadEntity(ComputerBuilder builder) {
        this.startTag=builder.startTag;
        this.heardTag=builder.heardTag;
    }

    //Builder Class
    public static class ComputerBuilder{

        private String startTag;
        private String heardTag;
        private String messageLength;


        public ComputerBuilder(String startTag, String heardTag,String messageLength){
            this.startTag=startTag;
            this.heardTag=heardTag;
            this.messageLength=messageLength;
        }

        public ComputerBuilder setMessageLength(String  messageLength) {
            this.messageLength = messageLength;
            return this;
        }

//        public ComputerBuilder setBluetoothEnabled(boolean isBluetoothEnabled) {
//            this.isBluetoothEnabled = isBluetoothEnabled;
//            return this;
//        }

        public InfraredHeadEntity build(){
            return new InfraredHeadEntity(this);
        }

    }

}
