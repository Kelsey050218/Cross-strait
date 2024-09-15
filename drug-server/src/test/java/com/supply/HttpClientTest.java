package com.supply;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HttpClientTest {

    @Test
    public void testGet() throws Exception {
        //创建httpclient对象
        CloseableHttpClient aDefault = HttpClients.createDefault();
        //创建请求对象
        HttpGet httpGet = new HttpGet("https://restapi.amap.com/v3/ip?key=e445559685bbf4b0faf3768910dba2ed");
        //发送请求
        CloseableHttpResponse execute = aDefault.execute(httpGet);
        //获取返回状态码
        int statusCode = execute.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        //获取返回数据
        HttpEntity entity = execute.getEntity();
        String string = EntityUtils.toString(entity);
        System.out.println(string);

        // 解析 JSON 字符串为 JSONObject 对象
        JSONObject jsonObject = JSONObject.parseObject(string);

        // 获取 province 和 city 字段
        String province = jsonObject.getString("province");
        String city = jsonObject.getString("city");

        // 输出结果
        System.out.println("Province: " + province);
        System.out.println("City: " + city);

        //关闭资源
        execute.close();
        aDefault.close();
    }

    @Test
    public void testPost() throws Exception {
        //创建httpclient对象
        CloseableHttpClient aDefault = HttpClients.createDefault();
        //创建请求对象
        HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "admin");
        jsonObject.put("password", "123456");
        StringEntity entity = new StringEntity(jsonObject.toString());
        //指定编码方式
        entity.setContentEncoding("utf-8");
        //指定数据格式
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        //发送请求
        CloseableHttpResponse execute = aDefault.execute(httpPost);
        //获取返回状态码
        int statusCode = execute.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        //获取返回数据
        HttpEntity entitys = execute.getEntity();
        String string = EntityUtils.toString(entitys);
        System.out.println(string);

        //关闭资源
        execute.close();
        aDefault.close();
    }

    @Test
    public void testLocal() throws Exception {

        // 创建 HttpClient 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建 HttpGet 对象，并指定请求 URL
        HttpGet httpGet = new HttpGet("https://api.map.baidu.com/location/ip?coor=bd09ll&ak=6f5EX5fK2vhyKuJtWhUthlasyCie2ecu");

        // 发送 GET 请求，并获取 HttpResponse
        HttpResponse httpResponse = httpClient.execute(httpGet);

        // 获取响应状态码和响应实体
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        HttpEntity httpEntity = httpResponse.getEntity();

        // 处理响应内容
        if (httpEntity != null) {
            String responseString = EntityUtils.toString(httpEntity);
            System.out.println("Response Code: " + statusCode);
            System.out.println("Response Content: " + responseString);
        }

        // 释放连接资源
        httpClient.close();

    }


}
