package com.test;

import com.snfq.module.qrcode.core.TwoDimensionCode;

import java.util.Arrays;

public class Test {

	public static void main(String[] args){
//		String s = "{\"code\": \"00\",\"swift_number\": \"444333_20160317164440_2019\",	\"flag_specialList_c\": \"1\",	\"sl_id_abnormal\": \"0\",	\"sl_id_phone_overdue\": \"0\",	\"sl_id_court_bad\": \"1\",	\"sl_id_court_executed\": \"2\",	\"sl_lm_cell_nbank_mc_overdue\": \"1\",\"sl_lm_cell_nbank_mc_fraud\": \"2\",\"sl_lm_cell_nbank_mc_lost\": \"1\",\"sl_lm_cell_nbank_mc_refuse\": \"0\",\"sl_lm_cell_nbank_ca_bad\": \"1\",\"sl_lm_cell_nbank_ca_overdue\": \"0\",\"sl_lm_cell_nbank_ca_fraud\": \"0\",\"sl_lm_cell_nbank_ca_lost\": \"1\",\"sl_lm_cell_nbank_ca_refuse\": \"2\",\"sl_lm_cell_nbank_com_bad\": \"1\",\"sl_lm_cell_nbank_com_overdue\": \"0\",\"sl_lm_cell_nbank_com_fraud\": \"1\",\"sl_lm_cell_nbank_com_lost\": \"0\",\"sl_lm_cell_nbank_com_refuse\": \"0\",\"sl_lm_cell_nbank_cf_bad\": \"1\",\"sl_lm_cell_nbank_cf_overdue\": \"2\",\"sl_lm_cell_nbank_cf_fraud\": \"1\",\"sl_lm_cell_nbank_cf_lost\": \"0\",\"sl_lm_cell_nbank_cf_refuse\": \"1\",\"sl_lm_cell_nbank_other_bad\": \"0\",\"sl_lm_cell_nbank_other_overdue\": \"0\",\"sl_lm_cell_nbank_other_fraud\": \"1\",\"sl_lm_cell_nbank_other_lost\": \"2\",	\"sl_lm_cell_nbank_other_refuse\": \"1\"}";
//		int counter = 0;
//		System.out.println(countStr(s,"\"flag_specialList_c\": \"1\"",counter));
//		System.out.println(countStr(s,"\"0\"",counter));
//		System.out.println(countStr(s,"\"1\"",counter));
//		System.out.println(countStr(s,"\"2\"",counter));
		
//		System.out.println(12/10 + ( 12%10 >= 1 ? 1 : 0));
//		System.out.println(12/5 + ( 12%5 >= 1 ? 1 : 0));
		
//		System.out.println(String.format("%5s", "5").replaceAll("\\s", "0"));

//	    System.out.println(DateUtil.daysOfTwo(DateUtil.formatDatetoString(new Date(), DateUtil.DATE_FMT_3), "2017-06-08"));
	    
//	    int[] ages = new int[2];
//	    System.out.println(ages[0] + ";" + ages[1]);
	    
	    System.out.println(Arrays.asList("result","assignName").indexOf("result1"));
	}
	
	 public static int countStr(String str1, String str2,int counter) {  
	        if (str1.indexOf(str2) == -1) {  
	            return counter;  
	        } else if (str1.indexOf(str2) != -1) {  
	            counter++;  
	            return countStr(str1.substring(str1.indexOf(str2) +  
	                   str2.length()), str2,counter);  
	        }  
	        return counter;  
	    }

	    public void test2(){
			TwoDimensionCode handler = new TwoDimensionCode();

			String decoderContent = handler.decoderQRCode("");
			System.out.println("============解析结果如下：===============");
			System.out.println(decoderContent);
			System.out.println("=========解码成功===========");
		}
}
