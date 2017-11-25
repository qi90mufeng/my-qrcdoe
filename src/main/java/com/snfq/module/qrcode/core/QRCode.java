/**
 * sinafenqi.com
 * Copyright (c) 2017 All Rights Reserved.
 */
package com.snfq.module.qrcode.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

import com.swetake.util.Qrcode;
/**
 * @author fujin
 * @version $Id: QRCode.java, v 0.1 2017-11-14 11:30 Exp $$
 */
public class QRCode {

    /**
     * 生成二维码(QRCode)图片
     */
    public static  void encoderQRCode(BuildQRCode bc) {

        try {
            Qrcode qrcodeHandler = new Qrcode();
            qrcodeHandler.setQrcodeErrorCorrect('M');
            qrcodeHandler.setQrcodeEncodeMode('B');
            qrcodeHandler.setQrcodeVersion(7);
            System.out.println(bc.getContent());
            byte[] contentBytes = bc.getContent().getBytes("gb2312");
            BufferedImage bufImg = new BufferedImage(140, 140,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = bufImg.createGraphics();
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, 140, 140);
            // 设定图像颜色> BLACK
            gs.setColor(Color.BLACK);
            // 设置偏移量 不设置可能导致解析出错
            int pixoff = 2;
            // 输出内容> 二维码
            if (contentBytes.length > 0 && contentBytes.length < 120) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            } else {
                System.err.println("QRCode content bytes length = " + contentBytes.length + " not in [ 0,120 ]. ");
            }
            gs.dispose();
            bufImg.flush();
            File imgFile = new File(bc.getImgPath());
            // 生成二维码QRCode图片
            ImageIO.write(bufImg, "png", imgFile);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 解码二维码
     * @param imgPath
     * @return String
     */
    public  static String decoderQRCode(String imgPath) {
        // QRCode 二维码图片的文件
        File imageFile = new File(imgPath);

        BufferedImage bufImg = null;
        String decodedData = null;
        try {
            bufImg = ImageIO.read(imageFile);

            QRCodeDecoder decoder = new QRCodeDecoder();
            decodedData = new String(decoder.decode(new J2SEImage(bufImg)));
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (DecodingFailedException dfe) {
            System.out.println("Error: " + dfe.getMessage());
            dfe.printStackTrace();
        }
        return decodedData;
    }

    public static void main(String[] args) {
        //生成二维码
        String imgPath = "D:/test/Michael_QRCode.png";
        String content = "http://www.baidu.com";
        BuildQRCode bc = new BuildQRCode(imgPath,content);
        encoderQRCode(bc);
        System.out.println("encoder QRcode success");


        //解读二维码
        String decoderContent =decoderQRCode(imgPath);
        System.out.println("解析结果如下：");
        System.out.println(decoderContent);
        System.out.println("========decoder success!!!");
    }

}
