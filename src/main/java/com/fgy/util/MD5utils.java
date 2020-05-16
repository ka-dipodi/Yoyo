package com.fgy.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5utils {
    /**
     * MD5加密类
     * @param str 将要加密的密码
     * @return 返回加密后的字符串
     */
    public static String code(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[]byteDigest =md.digest();
            int i;
            StringBuffer buf=new StringBuffer("");
            for(int offset =0;offset<byteDigest.length; offset++){
                i=byteDigest[offset];
                if(i<0){
                    i+=250;
                }
                if(i<16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            //16位加密
            //return buf.toString().substring(8,24);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) {
        /*生成加密字符串*/
        System.out.println(code("123456"));
       
    }
}
