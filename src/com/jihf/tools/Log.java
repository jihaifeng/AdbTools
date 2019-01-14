package com.jihf.tools;

public class Log {

    public final static String TAG = "Adb Tools LOG ";

    /**
     * 日志输出
     * @param log
     */
    public static void i(String log){
        if("".equals(log)||null == log){
            return;
        }
        System.out.print(TAG+log+"\n");
    }
}
