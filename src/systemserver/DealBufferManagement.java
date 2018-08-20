/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemserver;

import HttpManagement.PostManager;
import SensorClass.TempData;
import SensorClass.UdpData;
import SqlManagement.SqlManagement;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.SocketChannel;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author zzl
 */
public class DealBufferManagement {

    private Queue<List<Byte>> origin_buffer;
    //private SqlManagement sm;
    private DataSendServer dss;
    private int send_port = 1115;

    private DealBufferManagement() throws Exception {

        origin_buffer = new LinkedBlockingQueue<>();
        //sm = new SqlManagement("jdbc:mysql://45.40.197.99:3306/sanrenxing", "root", "16899199");
        SendThread sendThread = new SendThread();
        //dss = new DataSendServer(send_port);
        //dss.run();
        sendThread.start();
    }

    private static DealBufferManagement single = null;

    public static DealBufferManagement getInstance() throws Exception {
        if (single == null) {
            single = new DealBufferManagement();
        }
        return single;
    }

    private UdpData origin_to_udpdata(List<Byte> tmp) {
        UdpData ud = null;
        String e_id = "";
        String content = "";
        for (int i = 0; i < 12; i++) {
            e_id = e_id + (char) tmp.get(i).byteValue();
        }
        for (int i = 13; i <= 16; i++) {
            content = content + (char) tmp.get(i).byteValue();
        }
        ud = new UdpData(e_id, content);
        return ud;
    }

    private void sendDataToSqlAndTcp() {
        if (queue_is_null()) {
            return;
        }
        List<Byte> tmp = this.pop_queue();
        SocketChannel sc = dss.getClientChannel();
        if (sc == null) {
            return;
        }
        //System.out.println("send one data!");
        ByteBuf time = sc.alloc().buffer(20);
        UdpData ud = origin_to_udpdata(tmp);
        //UdpData ud = new TempData("123","wodetest",8);
        String send_buff = JSON.toJSONString(ud);
        time.writeBytes(send_buff.getBytes());
        if (ud.getIsFault()) {
            sc.writeAndFlush(time);
        }
        //sm.inser_dataList(ud);
    }

    private void sendDataToSqlAndPost() {
        if (queue_is_null()) {
            return;
        }
        List<Byte> tmp = this.pop_queue();
        //SocketChannel sc = dss.getClientChannel();
        /*if (sc == null) {
            return;
        }*/
        //System.out.println("send one data!");
        //ByteBuf time = sc.alloc().buffer(20);
        UdpData ud = origin_to_udpdata(tmp);
        //UdpData ud = new TempData("123","wodetest",8);
        String send_buff = JSON.toJSONString(ud);
        try {
            if (ud.getIsFault()) {
                PostManager.PostMessage("http://45.40.197.99:8080/warning/report", send_buff);
                                         
                System.out.println("send a warning message!!!");
            } else {
                PostManager.PostMessage("http://45.40.197.99:8080/data/report", send_buff);
                System.out.println("send a common message!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* ********************/
        //sm.inser_dataList(ud);
    }

    public void push_queue(List<Byte> origin) {
        if (single.origin_buffer.size() == Integer.MAX_VALUE - 1) {
            single.origin_buffer.clear();
        }
        single.origin_buffer.add(origin);

    }

    public List<Byte> pop_queue() {
        return single.origin_buffer.poll();
    }

    public boolean queue_is_null() {
        return single.origin_buffer.isEmpty();
    }

    class SendThread implements Runnable {

        private Thread t;

        public void run() {
            while (true) {
                sendDataToSqlAndPost();
                //sendDataToSqlAndTcp();
            }
        }

        public void start() {
            System.out.println("send data to website and db thread running...");
            t = new Thread(this);
            t.start();
        }
    }
}
