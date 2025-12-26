package com.sfl.core.service.impl;

import com.sfl.core.domain.PersistentAuditEvent;
import com.sfl.core.repository.PersistenceAuditEventRepository;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Service for managing audit events.
 * <p>
 * This is the default implementation to support SpringBoot Actuator {@code AuditEventRepository}.
 */
@Service
@Transactional
public class AuditEventService {

    private final Logger log = LoggerFactory.getLogger(AuditEventService.class);

    private final JHipsterProperties jHipsterProperties;

    private final PersistenceAuditEventRepository persistenceAuditEventRepository;

    public AuditEventService(
        PersistenceAuditEventRepository persistenceAuditEventRepository, JHipsterProperties jhipsterProperties) {

        this.persistenceAuditEventRepository = persistenceAuditEventRepository;
        this.jHipsterProperties = jhipsterProperties;
    }

    /**
     * Old audit events should be automatically deleted after 30 days.
     *
     * This is scheduled to get fired at 12:00 (am).
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void removeOldAuditEvents() {
        persistenceAuditEventRepository
            .findByAuditEventDateBefore(Instant.now().minus(jHipsterProperties.getAuditEvents().getRetentionPeriod(), ChronoUnit.DAYS))
            .forEach(auditEvent -> {
                log.debug("Deleting audit data {}", auditEvent);
                persistenceAuditEventRepository.delete(auditEvent);
            });
    }

    @Transactional(readOnly = true)
    public Page<PersistentAuditEvent> findAll(Pageable pageable, String eventType) {
        return persistenceAuditEventRepository.findAllByAuditEventTypeLike(eventType, pageable);
    }


    @Transactional(readOnly = true)
    public Page<PersistentAuditEvent> findByDates(Instant fromDate, Instant toDate, Pageable pageable) {
        return persistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<PersistentAuditEvent> find(Long id) {
        return persistenceAuditEventRepository.findById(id);
    }
}
