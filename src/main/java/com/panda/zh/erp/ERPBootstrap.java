package com.panda.zh.erp;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Panda.Z
 */
@SuppressWarnings("ALL")
@Configuration
@ComponentScan("cc.changic.platform.web.cdkey")
public class ERPBootstrap {

    @Bean
    public Gson gson(){
        return new Gson();
    }

    public static void main(String[] args) {

        MongoClient client = new MongoClient(new ServerAddress("10.10.7.103", 17017), new MongoClientOptions.Builder().connectionsPerHost(1000).build());

        System.out.println(client.getMongoClientOptions().getConnectionsPerHost());
        MongoDatabase panda = client.getDatabase("panda");
        final MongoCollection<Document> collection = panda.getCollection("act_keys_coll");
        ExecutorService service = Executors.newFixedThreadPool(5000);
        for (int i = 0; i < 100000; i++) {
            final int index = i;
            service.submit(new Runnable() {
                @Override
                public void run() {
                    Document update = collection.findOneAndUpdate(Filters.eq("_id", new ObjectId("56f38c92634a4a3fe062e50f")), Updates.combine(Updates.inc("usedCount", 1)));
                    System.out.println(Thread.currentThread().getName() + " " + update.get("usedCount"));
                }
            });
        }

//        for (int i = 0; i < 1000; i++) {
//            final int index = i;
//            service.submit(new Runnable() {
//                @Override
//                public void run() {
//                   collection.insertOne(new Document().append("index", index));
//                }
//            });
//        }

    }
}
