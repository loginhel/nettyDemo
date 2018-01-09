package com.hhp.netty.msgPack;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.util.ArrayList;
import java.util.List;

public class demo {
    public static void main(String... args) throws Exception{
        List<String> src = new ArrayList<>();
        src.add("msgpack");
        src.add("kumofs");
        src.add("viver");

        MessagePack msgPack = new MessagePack();
        //Serialize
        byte[] raw = msgPack.write(src);
        //Deserialize directly using a template
        List<String> dst1 = msgPack.read(raw, Templates.tList(Templates.TString));
        System.out.println(dst1.get(0));
        System.out.println(dst1.get(1));
        System.out.println(dst1.get(2));
    }
}
