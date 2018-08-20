/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import SensorClass.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import java.util.Iterator;

/**
 *
 * @author zzl
 */
public class TcpRecvServerHandler extends ChannelInboundHandlerAdapter {
    
    List<UdpData> dataBuffer;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        dataBuffer = new ArrayList<>();
        ByteBuf in = (ByteBuf) msg;
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIP = insocket.getAddress().getHostAddress();
        List<Byte> origin_list = new ArrayList<>();
        try {
            while (in.isReadable()) {
                Byte tmp = in.readByte();
                origin_list.add(tmp);
                //System.out.print(tmp);
            }
            
            DealBufferManagement.getInstance().push_queue(origin_list);
            byte[] testb = listTobyte(origin_list);
            
            System.out.println(testb.toString() + "  " + clientIP);
            System.out.flush();
            //ctx.disconnect();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] listTobyte(List<Byte> list) {
        if (list == null || list.size() < 0) {
            return null;
        }
        byte[] bytes = new byte[list.size()];
        int i = 0;
        Iterator<Byte> iterator = list.iterator();
        while (iterator.hasNext()) {
            bytes[i] = iterator.next();
            i++;
        }
        return bytes;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client quit:" + ctx.channel().remoteAddress());

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
