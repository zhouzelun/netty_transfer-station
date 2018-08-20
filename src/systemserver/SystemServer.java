/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemserver;

import HttpManagement.PostManager;
import SensorClass.AshData;
import SensorClass.UdpData;
import SqlManagement.SqlManagement;
import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import static java.lang.Thread.sleep;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zzl
 */
public class SystemServer {

    public static void main(String[] args) throws Exception {
        int udp_port = 1114;  
        UdpDataRecvServer drs = new UdpDataRecvServer(udp_port);     
        DealBufferManagement dbm = DealBufferManagement.getInstance();
        drs.run();
        //PostManager.PostMessage("http://45.40.197.99:8080/warning/report", "{\n\"content\":\"asd\",\n\"id\":\"401518591490\"\n}\n");
    }
}