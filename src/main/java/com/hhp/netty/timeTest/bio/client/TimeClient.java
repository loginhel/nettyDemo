package com.hhp.netty.timeTest.bio.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author hhp
 */
public class TimeClient {

    public static void main(String... args){
        int port = 8080;
        if(args!=null && args.length>0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                //默认值
            }
        }

        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket("127.0.0.1",port);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()
            ));
            out = new PrintWriter(socket.getOutputStream(),true);
            out.println("query time order");
            System.out.println("send order to server succeed.");
            String resp = in.readLine();
            System.out.println("Now is: "+resp);
        }catch (Exception e){
            //do nothing
        }finally {
            if(out !=null){
                out.close();
                out = null;
            }
            if(in!=null){
                try {
                    in.close();
                }catch (IOException e1){
                    e1.printStackTrace();
                }
            }
            if(socket!=null){
                try {
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
