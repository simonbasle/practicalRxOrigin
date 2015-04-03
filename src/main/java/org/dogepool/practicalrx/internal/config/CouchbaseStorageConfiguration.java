package org.dogepool.practicalrx.internal.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchbaseStorageConfiguration {

    @Bean(destroyMethod = "disconnect")
    public Cluster couchbaseCluster( @Value("#{'${store.nodes:127.0.0.1}'.split(',')}")  String... nodes) {
        System.setProperty("com.couchbase.queryEnabled", "true");
        return CouchbaseCluster.create(nodes);
    }

    @Bean
    @Autowired
    public Bucket couchbaseBucket( Cluster cluster,
            @Value("${store.bucket:default}") String bucket,
            @Value("${store.bucket.password:}") String password) {

        return cluster.openBucket(bucket, password);
    }
}
