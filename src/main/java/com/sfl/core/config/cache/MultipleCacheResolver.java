package com.sfl.core.config.cache;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MultipleCacheResolver {

    private final Logger log = LoggerFactory.getLogger(MultipleCacheResolver.class);

    private boolean cacheEnabled;


    public boolean isCacheEnabled() {
        log.info("is Caching enabled/disabled {}", cacheEnabled);
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        log.info("Set Caching enabled/disabled {}", cacheEnabled);
        this.cacheEnabled = cacheEnabled;
    }
}
