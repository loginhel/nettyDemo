package com.hhp.netty;

import com.anysoft.util.DefaultProperties;
import com.anysoft.util.resource.ResourceFactory;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * proxy配置类
 * @author hhp
 * @date 2018.01.03
 */
public class OcsProxySettings {

    protected Map<String, Object> content = new ConcurrentHashMap<>();
    private static OcsProxySettings instance = null;
    private static final Object LOCK = new Object();

    private OcsProxySettings(){}

    public static OcsProxySettings get(){
        if (null == instance) {
            synchronized(LOCK) {
                if (null == instance) {
                    instance = new OcsProxySettings();
                }
            }
        }
        return instance;
    }

    /**
     * 将配置类中的数据都加入到当前的配置单例中
     * @param prop 配置类对象
     */
    public void addSettings(Properties prop){
        if(prop==null){
            return;
        }
        prop.forEach((k,v)-> {
            String key = (k instanceof String) ? (String)k : null;
            content.put(key,v);
        });
    }

    /**
     * 从xml文件加载配置项
     * @param xmlUrl xml配置文件的路径
     */
    public void addSettings(String xmlUrl){
        DefaultProperties dp = new DefaultProperties();
        dp.addSettings(xmlUrl,null,new ResourceFactory());
        Map<String, String> xmlContent = dp.getContent();
        addSettings(xmlContent);
    }

    /**
     * 私有配置加载函数
     * @param prop 配置项map
     */
    private void addSettings(Map<String,String> prop){
        if(prop==null){
            return;
        }
        prop.forEach((k,v)-> {
            content.put(k,v);
        });
    }

    /**
     * 获取配置项
     * @param name 配置项名称
     * @param defaultValue 默认值
     * @return 配置项的值
     */
    public String getValue(String name, String defaultValue) {
        String value = (String)this.content.get(name);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 增加配置项
     * @param name 配置项名称
     * @param value 配置项值
     */
    public void setValue(String name, String value) {
        if (value == null || value.length() <= 0) {
            this.content.remove(name);
        }
        this.content.put(name, value);
    }
}
