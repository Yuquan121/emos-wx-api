package com.example.emos.wx.config.xss;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @description: 覆盖多种读取客户端数据的方法，在其中做转义，防止xss攻击
 * @author: Moon
 */

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name){
        String value = super.getParameter(name);
        if(!StrUtil.hasEmpty((value))){
            value = HtmlUtil.filter(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name){
        String[] values = super.getParameterValues(name);
        if(values != null){
            for(int i = 0;i < values.length;i++){
                String value = values[i];
                if(!StrUtil.hasEmpty((value))){
                    value = HtmlUtil.filter(value);
                }
                values[i] = value;
            }
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap(){
        Map<String, String[]> parameters = super.getParameterMap();
        LinkedHashMap<String, String[]> map = new LinkedHashMap<>();
        if(parameters != null){
            for(String key: parameters.keySet()){
                String[] values = parameters.get(key);
                for(int i = 0;i < values.length;i++){
                    String value = values[i];
                    if(!StrUtil.hasEmpty(value)){
                        value = HtmlUtil.filter(value);
                    }
                    values[i] = value;
                }
                map.put(key, values);
            }
        }
        return map;
    }

    @Override
    public String getHeader(String name){
        String value = super.getHeader(name);
        if(!StrUtil.hasEmpty(value)){
            value = HtmlUtil.filter(value);
        }
        return value;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        InputStream inputStream = super.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer body = new StringBuffer();
        String line = bufferedReader.readLine();
        while(line != null){
            body.append(line);
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        reader.close();
        inputStream.close();

        Map<String, Object> map = JSONUtil.parseObj(body.toString());
        Map<String, Object> result = new LinkedHashMap<>();
        for(String key:map.keySet()){
            Object val = map.get(key);
            if(val instanceof String){
                if(StrUtil.hasEmpty()){
                    result.put(key,HtmlUtil.filter(val.toString()));
                }
            }else {
                result.put(key,val);
            }
        }
        String json = JSONUtil.toJsonStr(result);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(json.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

}
