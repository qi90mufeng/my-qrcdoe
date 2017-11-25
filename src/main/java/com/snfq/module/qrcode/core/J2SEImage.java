/**
 * sinafenqi.com
 * Copyright (c) 2017 All Rights Reserved.
 */
package com.snfq.module.qrcode.core;

import jp.sourceforge.qrcode.data.QRCodeImage;

import java.awt.image.BufferedImage;

/**
 * @author fujin
 * @version $Id: J2SEImage.java, v 0.1 2017-11-14 11:53 Exp $$
 */
public class J2SEImage implements QRCodeImage {
    BufferedImage bufImg;

    public J2SEImage(BufferedImage bufImg) {
        this.bufImg = bufImg;
    }

    @Override
    public int getWidth() {
        return bufImg.getWidth();
    }
    @Override
    public int getHeight() {
        return bufImg.getHeight();
    }
    @Override
    public int getPixel(int x, int y) {
        return bufImg.getRGB(x, y);
    }
}
