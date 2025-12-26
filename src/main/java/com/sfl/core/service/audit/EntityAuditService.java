package com.sfl.core.service.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sfl.core.domain.PersistentAuditEvent;
import com.sfl.core.domain.audit.AuditData;
import com.sfl.core.repository.PersistenceAuditEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class EntityAuditService {

    private final Logger log = LoggerFactory.getLogger(EntityAuditService.class);

    ObjectMapper mapper = new ObjectMapper();

    private final PersistenceAuditEventRepository persistenceAuditEventRepository;

    public EntityAuditService(PersistenceAuditEventRepository persistenceAuditEventRepository) {
        this.persistenceAuditEventRepository = persistenceAuditEventRepository;
    }

    @Async
    public void entityAddAuditLogs(String newObj, String entityType, String principal, String entityIdentifier) {
        try {
            ObjectNode node = mapper.readValue(newObj, ObjectNode.class);
            savingAuditData(null, newObj, "save-" + entityType, principal,
                node.get(entityIdentifier).asText());
        } catch (Exception e) {
            log.error("Error while add entity auditing {}", e.getMessage());
        }
    }

    @Async
    public void entityUpdateAuditLogs(String oldObj, String newObj, String entityType, String principal,
                                      String entityIdentifier) {
        try {
            ObjectNode node = mapper.readValue(newObj, ObjectNode.class);
            savingAuditData(oldObj, newObj, "update-" + entityType, principal,
                node.get(entityIdentifier).asText());
        } catch (Exception e) {
            log.error("Error while update entity auditing {}", e.getMessage());
        }
    }

    @Async
    public void entityRemoveAuditLogs(String oldObj, String entityType, String principal, String entityIdentifier) {
        try {
            ObjectNode node = mapper.readValue(oldObj, ObjectNode.class);
            savingAuditData(oldObj, null, "delete-" + entityType, principal,
                node.get(entityIdentifier).asText());
        } catch (Exception e) {
            log.error("Error while remove entity auditing {}", e.getMessage());
        }
    }

    public void savingAuditData(String oldAuditObj, String newAuditObj, String eventType, String principal,
                                String entityIdentifier) {
        try {
            PersistentAuditEvent persistentAuditEvent = new PersistentAuditEvent();
            persistentAuditEvent.setPrincipal(principal);
            persistentAuditEvent.setAuditEventType(eventType);
            persistentAuditEvent.setAuditEventDate(Instant.now());
            persistentAuditEvent.setEntityIdentifier(entityIdentifier);
            Map<String, String> newAuditDate = new HashMap<>();
            newAuditDate.put("audit-data", mapper.writeValueAsString(new AuditData(oldAuditObj, newAuditObj)));
            persistentAuditEvent.setData(newAuditDate);
            persistenceAuditEventRepository.save(persistentAuditEvent);
        } catch (Exception e) {
            log.error("Error while saving audit date entity {}", e.getMessage());
        }
    }
}
