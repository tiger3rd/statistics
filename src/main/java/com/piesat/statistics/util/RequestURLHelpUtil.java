package com.piesat.statistics.util;

import java.util.*;

public class RequestURLHelpUtil {

    public static  String transfer(String originalUrl){
        TreeMap<String,Object> paramsMap  = new TreeMap<>();

        int i = originalUrl.indexOf("api?");
        String prefix = originalUrl.substring(0,i + 4);
        String substring = originalUrl.substring(i + 4);
        String[] split = substring.split("&");
        for (String str:split) {
            String[] split1 = str.split("=");
            paramsMap.put(split1[0],split1[1]);
        }

        //TreeMap<String,Object> paramsMap  = new TreeMap<>();
        Calendar cal =  Calendar.getInstance();
        TimeZone timeZone = cal.getTimeZone();
        int rawOffset = timeZone.getRawOffset();
        cal.add(Calendar.MILLISECOND,-rawOffset);

        String timestamp= String.valueOf(System.currentTimeMillis());
        String nonce = UUID.randomUUID().toString();
        //String timestamp = String.valueOf(cal.getTimeInMillis());

        //String userid = "userId";
        //String passwd = "pwd";

        //paramsMap.put("serviceNodeId","NMIC_MUSIC_CMADAAS");
        //put(userid,"NMIC_YWNWUSER");
        //paramsMap.put(passwd,"Nmic@68406825");
        //paramsMap.put("interfaceid","getRadaFileByTimeRange");
        //paramsMap.put("dataCode","RADA_L3_MST_PRE1H_PNG");
        //paramsMap.put("dataFormat","json");
        //paramsMap.put("timeRange","[20220601091500,20220602091600]");
        //paramsMap.put("limitCnt","10");
        paramsMap.put("timestamp",timestamp);
        paramsMap.put("nonce",nonce);


        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        Set<String> strings = paramsMap.keySet();
        for (String str:strings) {
            String value = paramsMap.get(str).toString();
            sb.append(str+"="+value+"&");
            if(!"pwd".equals(str)){
                sb2.append(str+"="+value+"&");
            }
        }
        String stringSignTemp = sb.subSequence(0,sb.length()-1).toString();
        String tempUrl = sb2.subSequence(0,sb2.length()-1).toString();

        String encode = MD5Util.encode(stringSignTemp).toUpperCase();

        String url = prefix+tempUrl+"&sign="+encode;

        //System.out.println("wget -O abc.json \""+url+"\"");

        return url;
    }

}
