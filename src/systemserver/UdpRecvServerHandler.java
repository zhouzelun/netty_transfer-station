/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemserver;

import SensorClass.UdpData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author zzl
 */
public class UdpRecvServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    List<UdpData> dataBuffer;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        ByteBuf in = msg.content();
        dataBuffer = new ArrayList<>();
        
        //InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        //String clientIP = insocket.getAddress().getHostAddress();
        List<Byte> origin_list = new ArrayList<>();
        try {
            while (in.isReadable()) {
                Byte tmp = in.readByte();
                origin_list.add(tmp);
                //System.out.print(tmp);
            }
            //去除异常数据　仅限长度不对的
            System.out.print("recv "+origin_list.size()+"length message");
            if(origin_list.size()!=UdpData.data_length+UdpData.equipment_id_length+1+2) ;
            
            DealBufferManagement.getInstance().push_queue(origin_list);
            byte[] testb = listTobyte(origin_list);
            String recv_str  = "";
            for(int i = 0;i<testb.length;i++){
                recv_str = recv_str+(char)testb[i];
            }
            
            System.out.println(":"+recv_str);
            System.out.flush();
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
