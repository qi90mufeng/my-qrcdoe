/**
 * sinafenqi.com
 * Copyright (c) 2017 All Rights Reserved.
 */
package com.snfq.module.qrcode.util;

import java.util.HashMap;

/**
 * 火车票 查询问题  针对12306设计一个快速的查询系统
 * 思想就是将火车票区间的每个站按位映射，然后通过位操作法来查询
 * 注：本代码只是演示了查询的流程  关于同步方面并未考虑
 * @author fujin
 * @version $Id: TrainTickets.java, v 0.1 2017-11-24 17:20 boyuan Exp $$
 */
public class TrainTickets {
    /**
     * 一个宁波到上海的高铁为例
     * 每一个元素都是表示停靠站点
     */
    private final HashMap<String,Integer> STATION_MAP;

    /**
     * 和STATION_MAP一样的信息
     */
    private final String[] STATION_ARRAY={"宁波","余姚北","绍兴北","杭州东","桐乡","嘉善南","上海虹桥"};

    public TrainTickets(){
        //初始化一下站点信息
        STATION_MAP=new HashMap<String,Integer>(){
            {
                for(int i=0;i<STATION_ARRAY.length;i++)
                {
                    put(STATION_ARRAY[i],i);
                }
            }
        };
    }

    /**
     * 01111111  低位表示起始  高位表示终止  一共7站
     */
    private int rTicket=0x7F;

    public enum State{
        /**
         * 成功标志 有票 或者购买成功
         */
        SUCCESS,
        /**
         * 失败标志 无票 或者购买失败
         */
        FAILED,
        /**
         * 错误标志 可能使两个站点不存在
         */
        ERROR;
    }

    /**
     * 查询是否有票
     * @param start
     * @param end
     * @return
     */
    public State query(String start,String end) {
        return this.query(this.buildTicket(start, end));
    }

    private State query(int qTicket) {
        if(qTicket==-1){
            return State.ERROR;
        }
        return (rTicket&qTicket)==qTicket?State.SUCCESS:State.FAILED;//如果进行与操作之后值还是查询票的数据 就表示有票
    }

    /**
     * 买票操作
     * @param start
     * @param end
     * @return
     */
    public State buy(String start,String end)
    {
        int qTicket=this.buildTicket(start, end);
        State state=this.query(qTicket);

        if(state==State.SUCCESS)
        {
            this.rTicket^=qTicket;//进行异或操作 将票买了
        }

        return state;
    }

    /**
     * 退票操作
     * @param start
     * @param end
     * @return
     */
    public State refund(String start,String end)
    {
        int qTicket=this.buildTicket(start, end);

        /**
         * 1100010
         * 0011100
         * 这里是为了判断退票的区间在余票中是否被正好买掉
         */
        if((this.rTicket|qTicket)-this.rTicket==qTicket)
        {
            this.rTicket|=qTicket;//将票归还
            return State.SUCCESS;
        }else{
            return State.ERROR;//发生意外错误了
        }
    }

    /**
     * 根据出发和达到站构建票
     * @param start
     * @param end
     * @return -1表示没有这些站点
     */
    private int buildTicket(String start,String end)
    {
        if(!STATION_MAP.containsKey(start) || !STATION_MAP.containsKey(end))
        {
            return -1;//表示没有这些站点  或者站点名字出错
        }

        int si=STATION_MAP.get(start),ei=STATION_MAP.get(end);
        int qTicket=0;

        while(si<ei)
        {
            qTicket|=0x1<<si;//在相应的位置上置1  注意  达到站是不置1
            si++;
        }

        return qTicket;
    }

    /**
     * 获取余票状态
     * @return
     */
    public String remaining()
    {
        StringBuilder sb=new StringBuilder();
        boolean cc=false;//false 表示前一站已经被买了
        sb.append("余票状态：");
        for(int i=0;i<STATION_ARRAY.length;i++)
        {
            int mark=0x1<<i;
            if((this.rTicket&mark)==mark)
            {
                if(cc)
                {
                    sb.append("->");
                }else{
                    sb.append("  ");
                }

                sb.append(STATION_ARRAY[i]);
                cc=true;
            }
            else{
                cc=false;
            }

        }

        return sb.toString().trim();
    }

    public static void main(String[] args) {
        TrainTickets tt=new TrainTickets();
        System.out.println(tt.remaining());
        System.out.println("查询 宁波->杭州南:"+tt.query("宁波","杭州南"));
        System.out.println("查询 宁波->杭州东:"+tt.query("宁波","杭州东"));
        System.out.println("购买 宁波->杭州东:"+tt.buy("宁波","杭州东"));
        System.out.println(tt.remaining());
        System.out.println("购买 宁波->杭州东:"+tt.buy("宁波","杭州东"));
        System.out.println("购买 宁波->绍兴北:"+tt.buy("宁波","绍兴北"));
        System.out.println("购买 绍兴北->桐乡:"+tt.buy("绍兴北","桐乡"));
        System.out.println("购买 杭州东->桐乡:"+tt.buy("杭州东","桐乡"));
        System.out.println(tt.remaining());
        System.out.println("退票 宁波->杭州东:"+tt.refund("宁波","杭州东"));
        System.out.println(tt.remaining());
        System.out.println("购买 宁波->杭州东:"+tt.buy("宁波","杭州东"));
    }
}
