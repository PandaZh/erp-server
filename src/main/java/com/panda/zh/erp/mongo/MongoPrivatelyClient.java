package com.panda.zh.erp.mongo;

import com.mongodb.MongoClient;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Panda.Z
 */
public class MongoPrivatelyClient {

    private final static AtomicBoolean IS_INIT = new AtomicBoolean(false);

    private String host;
    private int port;

    private MongoClient client;

    public MongoPrivatelyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init() {
        if (IS_INIT.compareAndSet(false, true))
            client = new MongoClient(host, port);
    }

    public void destroy() {
        client.close();
    }

}
