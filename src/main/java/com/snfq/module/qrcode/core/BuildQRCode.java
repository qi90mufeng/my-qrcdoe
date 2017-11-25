/**
 * sinafenqi.com
 * Copyright (c) 2017 All Rights Reserved.
 */
package com.snfq.module.qrcode.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author fujin
 * @version $Id: BuildQRCode.java, v 0.1 2017-11-14 11:29 boyuan Exp $$
 */
public class BuildQRCode {
    /**
     * 二维码存放地址
     */
    private   String imgPath ;

    /**
     * 二维码内容
     */
    private  String content ;

    /**
     * 无参构造方法
     */
    public BuildQRCode() {

    }

    /**
     * 有参构造方法
     * @param imgPath
     * @param content
     */
    public BuildQRCode(String imgPath, String content) {
        super();
        this.imgPath = imgPath;
        this.content = content;
    }



    public void initParam(BuildQRCode bqrc){

        //  生成临时订单号和有效时间
        Calendar ca = Calendar.getInstance();
        Date date  = ca.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        System.out.println(sdf.format(date));
        String orderNo = sdf.format(date)+(int)Math.random()*1000;//生成订单号
        //生成有效时间五分钟
        ca.add(Calendar.MINUTE,5);
        System.out.println(sdf.format(ca.getTime()));
        String timeout = sdf.format(ca.getTime());

        //对参数进行初始化
        bqrc.setImgPath( "C:/Users/boyuan/Desktop/IMG_4841.JPG");
        bqrc.setContent("HTTPS://QR.ALIPAY.COM/FKX06348BLP9T1AJ2V9790");
        //bqrc.setContent( "http://localhost:13080/boot-qrcode/qr/toPay?orderNo="+orderNo+"&timeout="+timeout);
    }

    public static void main(String[] args) {
        BuildQRCode qc = new BuildQRCode();
        qc.initParam(qc);//设置参数
        QRCode.encoderQRCode(qc);
    }



    public  String getImgPath() {
        return imgPath;
    }

    public  void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public  String getContent() {
        return content;
    }

    public  void setContent(String content) {
        this.content = content;
    }
}
