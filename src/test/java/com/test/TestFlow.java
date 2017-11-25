package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.snfq.module.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class TestFlow {

	@Test
	public void sendMail(){
		//MailUtil.sendAndCc(ConfConstant.smtp, ConfConstant.from, "fujin@sinafenqi.com", ConfConstant.copyto, "邮件警报",
		//		"客户信息有误", ConfConstant.username, ConfConstant.password, ConfConstant.filename);
	}
	//------------------------------------------------------------------------------------------------------------------------------







}
