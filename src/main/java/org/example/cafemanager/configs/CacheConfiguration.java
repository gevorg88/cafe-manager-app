package org.example.cafemanager.configs;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfiguration implements CachingConfigurer {

    @Override
    @Nullable
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager() {

            @Override
            @Nonnull
            protected Cache createConcurrentMapCache(final String name) {
                return new ConcurrentMapCache(name, CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES)
                        .maximumSize(100).build().asMap(), false);
            }
        };
    }

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Override
    @Bean("customKeyGenerator")
    public KeyGenerator keyGenerator() {
        return (Object target, Method method, Object... params) -> target.getClass().getSimpleName() + "_"
                + method.getName() + "_" + StringUtils.arrayToDelimitedString(params, "_");
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }

}
