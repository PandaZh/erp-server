package com.panda.zh.erp.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * @author Panda.Z
 */
public class PropertyDecipher extends PropertyPlaceholderConfigurer {

    public final static String CONFUSABLE_CHARACTERS = "character.confusable";
    public final static String SIGN_KEY_GM = "sign.key.gm";
    public final static String SIGN_KEY_GAME = "sign.key.game";


    public final static String MONGO_DB = "mongo.db";
    public final static String MONGO_COLL_ACT = "mongo.coll.act";
    public final static String MONGO_COLL_KEYS = "mongo.coll.keys";
    public final static String MONGO_COLL_KEYS_INDEX = "mongo.coll.keys.index";

    private static Properties PROPS;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        PROPS = props;
    }

    public static Object getProperty(String key) {
        return PROPS.getProperty(key);
    }
}
