package com.sfl.core.domain.audit;

import com.sfl.core.security.SecurityUtils;
import com.sfl.core.service.audit.EntityAuditService;
import com.sfl.core.service.util.audit.AuditEntityIdentifier;
import com.sfl.core.service.util.audit.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.util.HashMap;
import java.util.Map;

public class EntityAuditListener {

    private final Logger log = LoggerFactory.getLogger(EntityAuditListener.class);
    private final BeanUtil beanUtil;

    public EntityAuditListener(BeanUtil beanUtil) { this.beanUtil = beanUtil; }

    ThreadLocal<Map<String, String>> preObjectThreadLocal = ThreadLocal.withInitial(HashMap::new);
    HibernateAwareObjectMapper mapper = new HibernateAwareObjectMapper();
    public static final String DEFAULT_PRINCIPAL = "UNKNOWN";

    @PostLoad
    public synchronized void postLoad(Object o) {
        try {
            preObjectThreadLocal.get().put(String.valueOf(o.hashCode()), mapper.writeValueAsString(o));
        } catch (Exception e) {
            log.error("Error while post load entity lifecycle {}", e.getMessage());
        }
    }

    @PostUpdate
    public synchronized void postUpdate(Object o) {
        try {
            EntityAuditService entityAuditService = beanUtil.getBean(EntityAuditService.class);
            entityAuditService.entityUpdateAuditLogs(preObjectThreadLocal.get().get(String.valueOf(o.hashCode())),
                mapper.writeValueAsString(o), o.getClass().toString(),
                SecurityUtils.getCurrentUserLogin().orElse(DEFAULT_PRINCIPAL),
                o.getClass().getAnnotation(AuditEntityIdentifier.class) != null ?
                    o.getClass().getAnnotation(AuditEntityIdentifier.class).value() : "id");
            preObjectThreadLocal.remove();
        } catch (Exception e) {
            log.error("Error while post update entity lifecycle {}", e.getMessage());
        }
    }

    @PostPersist
    public synchronized void postPersist(Object o) {
        try {
            EntityAuditService entityAuditService = beanUtil.getBean(EntityAuditService.class);
            entityAuditService.entityAddAuditLogs(mapper.writeValueAsString(o), o.getClass().toString(),
                SecurityUtils.getCurrentUserLogin().orElse(DEFAULT_PRINCIPAL),
                o.getClass().getAnnotation(AuditEntityIdentifier.class) != null ?
                    o.getClass().getAnnotation(AuditEntityIdentifier.class).value() : "id");
        } catch (Exception e) {
            log.error("Error while post persist entity lifecycle {}", e.getMessage());
        }
    }

    @PostRemove
    public synchronized void postRemove(Object o) {
        try {
            EntityAuditService entityAuditService = beanUtil.getBean(EntityAuditService.class);
            entityAuditService.entityRemoveAuditLogs(mapper.writeValueAsString(o), o.getClass().toString(),
                SecurityUtils.getCurrentUserLogin().orElse(DEFAULT_PRINCIPAL),
                o.getClass().getAnnotation(AuditEntityIdentifier.class) != null ?
                    o.getClass().getAnnotation(AuditEntityIdentifier.class).value() : "id");
        } catch (Exception e) {
            log.error("Error while remove entity lifecycle {}", e.getMessage());
        }
    }
}
