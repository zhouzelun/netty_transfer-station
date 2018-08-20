/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
/**
 *
 * @author zzl
 */
public class UdpDataRecvServer {
    private int port;
    //private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup group = new NioEventLoopGroup();
    public UdpDataRecvServer(int port) {
        this.port = port;
    }
    public void run() throws Exception {
        try {
            System.out.println("DataRecvServer begin...");
            Bootstrap serverBootstrap = new Bootstrap();
            serverBootstrap.group(group);
            serverBootstrap.channel(NioDatagramChannel.class);
            serverBootstrap.option(ChannelOption.SO_BROADCAST, true);
            serverBootstrap.handler(new UdpRecvServerHandler());
            
            serverBootstrap.bind(port).sync().channel().closeFuture().await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("close connect thread...");
            //workerGroup.shutdownGracefully();
            //bossGroup.shutdownGracefully();
            group.shutdownGracefully();
        }
    }
}
