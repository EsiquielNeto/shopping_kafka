package br.com.studies.eventshop.admin;

import org.apache.kafka.clients.admin.*;

import java.util.Collections;

public class KafkaAdmin {

    public static void listTopics(AdminClient adminClient) {
        try {
            adminClient.listTopics()
                    .names()
                    .get()
                    .forEach(System.out::println);
        } catch (final Exception e) {
            throw new RuntimeException(String.format("Failed to list topic, exception: %s", e));
        }

    }

    public static void createTopic(String topicName, int partitions, short replications, AdminClient adminClient) {
        try {
            final NewTopic newTopic = new NewTopic(topicName, partitions, replications);
            final CreateTopicsResult createTopics = adminClient.createTopics(Collections.singletonList(newTopic));

            createTopics.all().get();

            System.out.println("Topic created with sucessfully!");
        } catch (final Exception e) {
            throw new RuntimeException(String.format("Failed to create topic: %s \n exception: %s", topicName, e));
        }
    }

    public static void describeTopic(String topicName, AdminClient adminClient) {
        try {
            adminClient.describeTopics(Collections.singletonList(topicName))
                    .allTopicNames()
                    .get()
                    .forEach((obj, topic) -> System.out.println(obj + topic.topicId() + "\n" + topic.partitions()));

        } catch (final Exception e) {
            throw new RuntimeException(String.format("Failed to describe topic: %s \n exception: %s", topicName, e));
        }
    }

    public static void deleteTopic(String topicName, AdminClient adminCLient) {
        try {
            DeleteTopicsResult result = adminCLient.deleteTopics(Collections.singletonList(topicName));
            result.all().get();

            System.out.println("Topic deleted with sucessfully!");
        } catch (final Exception e) {
            throw new RuntimeException(String.format("Failed to delete topic: %s \n exception: %s", topicName, e));
        }
    }

    public static void listConsumerGroup(AdminClient adminClient) {
        try {
            adminClient.listConsumerGroups()
                    .all()
                    .get()
                    .forEach(cg -> System.out.println(cg.groupId()));
        } catch (final Exception e) {
            throw new RuntimeException(String.format("Failed to list consumer group, exception: %s", e));
        }
    }

    public static void deleteConsumerGroup(String groupId, AdminClient adminClient) {
        try {
            DeleteConsumerGroupsResult result = adminClient.deleteConsumerGroups(Collections.singletonList(groupId));
            result.all().get();

            System.out.println("Consumer group deleted with sucessfully");
        } catch (final Exception e) {
            throw new RuntimeException(String.format("Failed to delete consumer group: %s \n exception: %s", groupId, e));
        }
    }

    public static void describeCluster(AdminClient adminClient) {
        try {
            DescribeClusterResult cluster = adminClient.describeCluster();
            System.out.println(cluster.clusterId().get());
        } catch (final Exception e) {
            throw new RuntimeException(String.format("Failed to describe consumer group,exception: %s", e));
        }
    }


}
