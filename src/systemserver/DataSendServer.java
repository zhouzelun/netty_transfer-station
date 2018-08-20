/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 * @author zzl
 */
public class DataSendServer {

    private int port;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private ServerBootstrap b;
    private SocketChannel clientChannel;

    public SocketChannel getClientChannel() {
        //System.out.println(DataSendServer.this.clientChannel.toString());
        return clientChannel;
    }

    public DataSendServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        System.out.println("DataSendServer begin...");
        try {
            b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new DataSendServerHandler());
                    System.out.println("remote website connect establish:" + ch.remoteAddress());
                    DataSendServer.this.clientChannel = ch;
                }
            });
            b.bind(port).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //bossGroup.shutdownGracefully();
            //workerGroup.shutdownGracefully();
        }
    }

}
