/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemserver;

import SensorClass.UdpData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zzl
 */
public class DataSendServerHandler extends ChannelInboundHandlerAdapter {
    List<UdpData> dataBuffer;
    private Thread t;
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        dataBuffer = new ArrayList<>();
        ByteBuf in = (ByteBuf)msg;
        InetSocketAddress insocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientIP = insocket.getAddress().getHostAddress();
        try{
            while(in.isReadable()){
                System.out.print(in.readByte());
                System.out.flush();
            }
            System.out.println("  "+clientIP);
            System.out.print("\n");
            
            /*ByteBuf time = ctx.alloc().buffer(4); //为ByteBuf分配四个字节
            time.writeBytes("abc".getBytes());
            ctx.writeAndFlush(time); // (3)*/
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client quit:"+ctx.channel().remoteAddress());
        
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
    
}
