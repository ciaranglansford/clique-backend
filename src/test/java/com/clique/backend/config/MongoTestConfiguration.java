package com.clique.backend.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

@TestConfiguration
@ActiveProfiles("test")
public class MongoTestConfiguration {

    private static final String HOST = "localhost";
    private static final int PORT = 27017;

    @Bean(destroyMethod = "stop")
    public MongodExecutable mongodExecutable() throws Exception {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        MongodConfig mongodConfig = MongodConfig.builder()
                .version(Version.Main.V4_4)
                .net(new Net(HOST, PORT, Network.localhostIsIPv6()))
                .build();

        MongodExecutable mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        return mongodExecutable;
    }
}