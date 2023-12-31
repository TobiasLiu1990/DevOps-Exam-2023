package com.example.s3rekognition.config;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

import java.time.Duration;
import java.util.Map;

@Configuration
public class MetricsConfig {

    @Value("${cloudwatch.namespace}")
    private String cloudwatchNamespace;

    @Bean
    public CloudWatchAsyncClient cloudWatchAsyncClient() {
        return CloudWatchAsyncClient
                .builder()
                .region(Region.EU_WEST_1)
                .build();
    }

    @Bean
    public MeterRegistry getMeterRegistry() {
        CloudWatchConfig cloudWatchConfig = setupCloudWatchConfig();

        return new CloudWatchMeterRegistry(
                cloudWatchConfig,
                Clock.SYSTEM,
                cloudWatchAsyncClient());
    }

    // Gets the name for the cloudwatch namespace from application.properties.
    // The name of cloudwatch.namespace must be the same as in Terraform.
    private CloudWatchConfig setupCloudWatchConfig() {
        return new CloudWatchConfig() {
            private Map<String, String> configuration = Map.of(
                    "cloudwatch.namespace", cloudwatchNamespace,
                    "cloudwatch.step", Duration.ofSeconds(5).toString());
            @Override
            public String get(String key) {
                return configuration.get(key);
            }
        };
    }
}
