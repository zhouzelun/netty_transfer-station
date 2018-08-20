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
public class TcpDataRecvServer {

    private int port;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private ServerBootstrap b;
    private SocketChannel clientChannel;

    public TcpDataRecvServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        try {
            System.out.println("DataRecvServer begin...");
            b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    System.out.println("remote sensor connect establish:" + ch.remoteAddress());
                    ch.pipeline().addLast(new TcpRecvServerHandler());
                    //ch.close();
                }
            });
            
            b.bind(port).sync().channel().closeFuture().sync();
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            System.out.println("close connect thread...");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
