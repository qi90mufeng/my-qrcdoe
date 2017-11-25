/**
 * test.com
 * Copyright (c) 2017 All Rights Reserved.
 */
package com.snfq.module.qrcode.service;

import com.snfq.module.qrcode.core.QRCodeDecoderHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author fujin
 * @version $Id: QrcodeService.java, v 0.1 2017-11-14 15:44 Exp $$
 */
@Service
public class QrcodeService {

    private static Logger logger = LoggerFactory.getLogger(QrcodeService.class);


    /**
     * 合并二维码
     *
     */
    public void MergeQrCode(){

    }


    /**
     * 解析二维码跳转
     * @param imgPath
     */
    public void ocrEem(String imgPath){
        QRCodeDecoderHandler.decoderQRCode(imgPath);
    }
}
