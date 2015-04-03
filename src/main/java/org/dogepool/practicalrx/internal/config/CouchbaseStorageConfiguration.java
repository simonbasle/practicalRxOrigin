package org.dogepool.practicalrx.internal.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchbaseStorageConfiguration {

    @Value("${store.enableFindAll:false}")
    private boolean useCouchbaseForFindAll;

    @Bean(destroyMethod = "disconnect")
    public Cluster couchbaseCluster( @Value("#{'${store.nodes:127.0.0.1}'.split(',')}")  String... nodes) {
        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                .queryEnabled(useCouchbaseForFindAll)
                .build();
        return CouchbaseCluster.create(env, nodes);
    }

    @Bean
    @Autowired
    public Bucket couchbaseBucket( Cluster cluster,
            @Value("${store.bucket:default}") String bucket,
            @Value("${store.bucket.password:}") String password) {

        return cluster.openBucket(bucket, password);
    }
}
