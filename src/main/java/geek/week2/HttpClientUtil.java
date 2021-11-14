package geek.week2;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

public class HttpClientUtil {

    public static String doGet(String url){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpGet get = new HttpGet(url);
        RequestConfig config = RequestConfig.custom().
                //配置文件
                setSocketTimeout(10000).
                setConnectTimeout(10000).
                build();
        get.setConfig(config);
        try {
            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            String message = null;
            if(null != entity && response.getStatusLine() != null){
                // do something
//                int statusCode = response.getStatusLine().getStatusCode();
                message = EntityUtils.toString(entity);
            }
            return message;
        } catch (IOException e) {
//            e.printStackTrace();
//            log.error();
            //or return something.
            throw new RuntimeException("系统异常，message" + e.getMessage());
        } finally {
            try{
                if(httpClient != null){
                    httpClient.close();
                }
                if(response != null){
                    response.close();
                }
            }catch (Exception e){
//                throw new RuntimeException("IO流施放异常");
                //do something
            }
        }

    }

    public static String doPost(String url, Map<String, Object> params){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        RequestConfig config = RequestConfig.custom().
                //配置文件
                setSocketTimeout(10000).
                setConnectTimeout(10000).
                build();
        httpPost.setConfig(config);

        StringEntity entity = new StringEntity(JSONObject.toJSONString(params), "urf-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        try {
            response = httpClient.execute(httpPost);
            //log.info("响应状态code为->", response.getStatusLine().getStatus())
            String message = null;
            HttpEntity resEntity = response.getEntity();
            if(null != resEntity){
                message = EntityUtils.toString(resEntity);
            }
            return message;
        } catch (IOException e) {
//            e.printStackTrace();
//            log.error();
            //or return something.
            throw new RuntimeException("系统异常，message" + e.getMessage());
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
//                e.printStackTrace();
                //do something
            }
        }

    }
}
