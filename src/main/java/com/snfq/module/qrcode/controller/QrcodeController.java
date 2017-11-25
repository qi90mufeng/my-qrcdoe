package com.snfq.module.qrcode.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.snfq.module.qrcode.core.BuildQRCode;
import com.snfq.module.qrcode.core.QRCode;
import com.snfq.module.qrcode.service.QrcodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author fujin
 * @version $Id: QrcodeController.java, v 0.1 2017-11-14 15:19 Exp $$
 */
@RestController
@RequestMapping("/qr")
public class QrcodeController {

    private static final Logger logger = LoggerFactory.getLogger(QrcodeController.class);

    @Autowired
    QrcodeService qrcodeService;
    /**
     * 跳转到二维码页面
     * @param request
     * @return
     */
    @RequestMapping("/toShowQRCode")
    public String showQRCode(HttpServletRequest request){
        logger.info("跳转到二维码页面...");
        return "qr";
    }

    /**
     * 扫描二维码
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping("/ocrEwm")
    public void ocrEwm(Model model, HttpServletRequest request, HttpServletResponse response){
        qrcodeService.ocrEem("C:/Users/boyuan/Desktop/IMG_4841.JPG");
    }

    /**
     * 通过流的形式实现在jsp上面显示二维码
     * @param model
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/readImage")
    public void readImage(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException{
        BuildQRCode bc = new BuildQRCode();//调用无参构造方法
        bc.initParam(bc);//对参数进行初始化
        QRCode.encoderQRCode(bc);//生成二维码


        String filePath =  bc.getImgPath();
        File filePic = new File(filePath);
        if(filePic.exists()){
            FileInputStream is = new FileInputStream(filePic);
            int i = is.available(); // 得到文件大小
            byte data[] = new byte[i];
            is.read(data); // 读数据
            is.close();
            response.setContentType("image/*"); // 设置返回的文件类型
            OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
            toClient.write(data); // 输出数据
            toClient.close();
        }
    }

    /**
     * 预支付接口
     * @param timeout
     * @param orderNo
     * @return
     */
    @RequestMapping("/toPay")
    public String toPay(@RequestParam("timeout") String timeout,@RequestParam("orderNo")String orderNo){
        logger.info("开始进行支付");
        if(StringUtils.isEmpty(orderNo)||StringUtils.isEmpty(timeout)){
            logger.info("订单号或者时间超时为空");
            return "faile";
        }

        Calendar ca = Calendar.getInstance();
        String afterTime = new SimpleDateFormat("yyyyMMddHHmmssSS").format(ca.getTime());

        if(!afterTime.equals(timeout)){
            logger.info(afterTime+"---->"+timeout);
            logger.info("二维码已失效，请重新支付");
            return "faile";
        }
        /**
         * ..... 这段可以根据二维码参数不一样，去相关支付平台进行支付，
         * 具体代码可以根据支付平台的api文档进行支付操作
         */
        logger.info("支付成功");
        return "success";

    }

}
