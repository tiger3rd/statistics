package com.piesat.statistics.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class EmailUtil {
	/**
	 * 调用邮件服务发送邮件
	 * @param mailServiceStr 邮件服务地址，来自BMD_DSAccessDef表MAIL-REST-TXT
	 * @param recipients 收件人
	 * @param subject 邮件标题
	 * @param html 邮件正文
	 * @return true发送成功 false发送失败
	 */
	public static boolean postMail(String mailServiceStr,String recipients,String subject,String html) {  
        // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        // 创建httppost    
        HttpPost httppost = new HttpPost(mailServiceStr);  
        // 创建参数队列    
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        formparams.add(new BasicNameValuePair("recipients", recipients));  
        formparams.add(new BasicNameValuePair("subject", subject)); 
        formparams.add(new BasicNameValuePair("html", html)); 
        //formparams.add(new BasicNameValuePair("emailId", "11"));
        UrlEncodedFormEntity uefEntity;  
        try {  
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            httppost.setEntity(uefEntity);  
            //System.out.println("executing request " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                HttpEntity entity = response.getEntity();  
                if (entity != null) {  
                   if(EntityUtils.toString(entity, "UTF-8").equals("{\"returnCode\":0,\"returnMsg\":\"success\",\"sendResult\":1}")){
                	   return true;
                   }   
                }  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }
		return false;  
    }

    public static void main(String[] args) {
	    String emailUrl = "http://10.0.86.136:9080/comApi/commonService/mailService?dataFormat=json&interfaceId=sendMail";
        boolean b = postMail(emailUrl, "1103746634@qq.com", "测试邮件", "测试内容:"+new Date());
        System.out.println("send Email flag:"+b);
    }
}
