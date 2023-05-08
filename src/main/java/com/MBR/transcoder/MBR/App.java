package com.MBR.transcoder.MBR;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableCassandraRepositories(basePackages = "com.MBR.transcoder.MBR")
public class App
{
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
        System.out.println( "Hello World!" );
    }
}
