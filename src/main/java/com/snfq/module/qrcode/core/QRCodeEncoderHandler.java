/**
 * sinafenqi.com
 * Copyright (c) 2017 All Rights Reserved.
 */
package com.snfq.module.qrcode.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

/**
 * @author fujin
 * @version $Id: QRCodeEncoderHandler.java, v 0.1 2017-11-14 10:30 boyuan Exp $$
 */
public class QRCodeEncoderHandler {
    /**
     * 生成二维码(QRCode)图片
     * @param content
     * @param imgPath
     */
    public void encoderQRCode(String content, String imgPath) {
        try {

            Qrcode qrcodeHandler = new Qrcode();
            qrcodeHandler.setQrcodeErrorCorrect('M');
            qrcodeHandler.setQrcodeEncodeMode('B');
            qrcodeHandler.setQrcodeVersion(7);

            System.out.println(content);
            byte[] contentBytes = content.getBytes("gb2312");

            BufferedImage bufImg = new BufferedImage(140, 140,
                    BufferedImage.TYPE_INT_RGB);

            Graphics2D gs = bufImg.createGraphics();

            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, 140, 140);

            // 设定图像颜色 &gt; BLACK
            gs.setColor(Color.BLACK);

            // 设置偏移量 不设置可能导致解析出错
            int pixoff = 2;
            // 输出内容 &gt; 二维码
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
                System.err.println("QRCode content bytes length = "
                        + contentBytes.length + " not in [ 0,120 ]. ");
            }

            gs.dispose();
            bufImg.flush();

            File imgFile = new File(imgPath);

            // 生成二维码QRCode图片
            ImageIO.write(bufImg, "png", imgFile);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String imgPath = "D:/QRCode.png";

        String content = "www.baidu.com";

        QRCodeEncoderHandler handler = new QRCodeEncoderHandler();
        handler.encoderQRCode(content, imgPath);

        System.out.println("encoder QRcode success");
    }
}
