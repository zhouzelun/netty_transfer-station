/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HttpManagement;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 *
 * @author zzl
 */
public class PostManager {

    public static void PostMessage(String dest_url, String message) throws MalformedURLException, ProtocolException, IOException {

        //建立连接
        String result = "";
        URL url = new URL(dest_url);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        //设置参数
        httpConn.setDoOutput(true);     //需要输出
        httpConn.setDoInput(true);      //需要输入
        httpConn.setUseCaches(true);   //不允许缓存
        httpConn.setRequestMethod("POST");      //设置POST方式连接
        //设置请求属性
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Charset", "UTF-8");

        //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
        //httpConn.connect();
        //建立输入流，向指向的URL传入参数
        //DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
        OutputStream outStream = httpConn.getOutputStream();
        outStream.write(message.getBytes());

        outStream.flush();
        outStream.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        System.out.println(result);

    }
}
