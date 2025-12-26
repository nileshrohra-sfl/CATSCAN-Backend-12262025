package com.sfl.core.web.rest;

import com.google.firebase.database.annotations.NotNull;
import com.sfl.core.config.cache.MultipleCacheResolver;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

@Transactional
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "javabaseproject")
public class CacheManagerResource {

    private final CacheManager cacheManager;
    private final MultipleCacheResolver cacheResolver;

    private static final String PREFIX="com.sfl.core.domain.";

    private final Logger log = LoggerFactory.getLogger(CacheManagerResource.class);

    public CacheManagerResource(CacheManager cacheManager, MultipleCacheResolver cacheResolver) {
        this.cacheManager = cacheManager;
        this.cacheResolver = cacheResolver;
    }

    /**
     * api to flush cache
     */
    @DeleteMapping("/cache-manager/all")
    public ResponseEntity<Void> clearCache(){
        log.info("Request to clear all caches");
        Collection<String> names=cacheManager.getCacheNames();
        log.info("Caches {}",names);
        names.forEach(name-> Objects.requireNonNull(cacheManager.getCache(name)).clear());
        return ResponseEntity.ok().build();
    }

    /**
     * api to flush cache for particular entity and entity will be the name of the domain (e.g. SflUser)
     */
    @DeleteMapping("/entity/{entity}")
    public ResponseEntity<Void> clearCacheForEntity(@PathVariable @NotNull String entity){
        log.info("Request to clear cache for entity {}",entity);
        String name=entityToCacheName(entity);
        cacheManager.getCache(name).clear();
        log.info("Cleared cache for {}",name);
        return ResponseEntity.ok().build();
    }

    private String entityToCacheName(String entity){
        return PREFIX+entity;
    }

    /**
     * api to enable disable cache
     */
    @PostMapping("/global-enable-disable-cache/{enabled}")
    public ResponseEntity<Void>enableDisableCache(@PathVariable Boolean enabled){
        log.info("Request to enable/disable caching {}",enabled);
        clearCache();
        cacheResolver.setCacheEnabled(enabled);
        return ResponseEntity.ok().build();
    }
}
