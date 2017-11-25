/**
 * sinafenqi.com
 * Copyright (c) 2017 All Rights Reserved.
 */
package com.snfq.module.qrcode.core;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author fujin
 * @version $Id: QRCodeDecoderHandler.java, v 0.1 2017-11-14 10:36 Exp $$
 */
public class QRCodeDecoderHandler {

    /**
     * 解码二维码
     * @param imgPath
     * @return String
     */
    public static String decoderQRCode(String imgPath) {

        // QRCode 二维码图片的文件
        File imageFile = new File(imgPath);

        BufferedImage bufImg = null;
        String decodedData = null;
        try {
            bufImg = ImageIO.read(imageFile);

            QRCodeDecoder decoder = new QRCodeDecoder();
            decodedData = new String(decoder.decode(new J2SEImage(bufImg)));

            // try {
            // System.out.println(new String(decodedData.getBytes("gb2312"),
            // "gb2312"));
            // } catch (Exception e) {
            // // TODO: handle exception
            // }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (DecodingFailedException dfe) {
            System.out.println("Error: " + dfe.getMessage());
            dfe.printStackTrace();
        }
        return decodedData;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("========start decode!!!");
        String imgPath = "C:/Users/boyuan/Desktop/IMG_4841.JPG";
        String decoderContent = QRCodeDecoderHandler.decoderQRCode(imgPath);
        System.out.println("解析结果如下：");
        System.out.println(decoderContent);
        System.out.println("========decoder success!!!");

        System.out.println("========start decode!!!");
        String imgPath2 = "C:/Users/boyuan/Desktop/IMG_4842.JPG";
        String decoderContent2 = QRCodeDecoderHandler.decoderQRCode(imgPath2);
        System.out.println("解析结果如下：");
        System.out.println(decoderContent2);
        System.out.println("========decoder success!!!");
    }

}
