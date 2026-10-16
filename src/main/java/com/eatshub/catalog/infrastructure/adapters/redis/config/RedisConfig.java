package com.eatshub.catalog.infrastructure.adapters.redis.config;

import com.eatshub.catalog.infrastructure.adapters.redis.dto.RestaurantCacheDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.*;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, RestaurantCacheDto> restaurantRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory) {

        ObjectMapper objectMapper = createObjectMapper();

        JacksonJsonRedisSerializer<RestaurantCacheDto> serializer =
                new JacksonJsonRedisSerializer<>(objectMapper, RestaurantCacheDto.class);

        RedisSerializationContext<String, RestaurantCacheDto> context =
                RedisSerializationContext.<String, RestaurantCacheDto>newSerializationContext(StringRedisSerializer.UTF_8)
                        .value(serializer)
                        .hashKey(StringRedisSerializer.UTF_8)
                        .hashValue(serializer)
                        .build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, List<RestaurantCacheDto>> restaurantListRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory) {

        ObjectMapper objectMapper = createObjectMapper();

        JavaType listType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, RestaurantCacheDto.class);

        JacksonJsonRedisSerializer<List<RestaurantCacheDto>> serializer =
                new JacksonJsonRedisSerializer<>(objectMapper, listType);

        RedisSerializationContext<String, List<RestaurantCacheDto>> context =
                RedisSerializationContext.<String, List<RestaurantCacheDto>>newSerializationContext(StringRedisSerializer.UTF_8)
                        .value(serializer)
                        .hashKey(StringRedisSerializer.UTF_8)
                        .hashValue(serializer)
                        .build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }

    private ObjectMapper createObjectMapper() {
        return JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .configure(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
    }

}
