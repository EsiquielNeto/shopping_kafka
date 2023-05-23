package br.com.studies.eventshop;

import br.com.studies.eventshop.admin.KafkaAdmin;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        AdminClient adminClient = AdminClient.create(properties);
        KafkaAdmin.createTopic("topico-1", 2, (short) 1, adminClient);
        KafkaAdmin.createTopic("topico-2", 2, (short) 1, adminClient);
        KafkaAdmin.listTopics(adminClient);
        KafkaAdmin.describeTopic("topico-1", adminClient);
        KafkaAdmin.deleteTopic("topico-1", adminClient);
        KafkaAdmin.deleteTopic("topico-2", adminClient);
        KafkaAdmin.listConsumerGroup(adminClient);
        KafkaAdmin.deleteConsumerGroup("group", adminClient);
        KafkaAdmin.describeCluster(adminClient);

    }
}