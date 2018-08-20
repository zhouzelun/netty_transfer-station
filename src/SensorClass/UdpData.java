/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SensorClass;

import com.alibaba.fastjson.annotation.JSONField;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author zzl
 */
public class UdpData {
    
    @JSONField(ordinal=2,name = "id")
    private String eid;
    
    @JSONField(ordinal=1,name = "content")
    private String data;
   
    @JSONField(serialize=false)
    private String data_time;
    
    @JSONField(serialize=false)
    public static int data_length = 4;
    
    public static enum SensorCategory{temp,ash,ch4,co1};
    
    @JSONField(serialize=false)
    private Boolean  isFault = true;
    
    @JSONField(serialize=false)
    public static int equipment_id_length = 12;

    public UdpData(String eid, String data) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.eid = eid;
        this.data = data;
        this.data_time = df.format(new Date());
        for(int i = 0;i<data.length();i++){
            if(data.charAt(i) == '0'){
                this.isFault = false;
            }
        }
    }
    
    public Boolean getIsFault() {
        return isFault;
    }

    public String getEid() {
        return eid;
    }

    public String getData() {
        return data;
    }

    public String getData_time() {
        return data_time;
    }
}
