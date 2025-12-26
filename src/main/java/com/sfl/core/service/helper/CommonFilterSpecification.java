package com.sfl.core.service.helper;

import com.sfl.core.service.dto.FilterRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;

import static com.sfl.core.repository.constants.CacheConstants.COLUMN_CREATED_DATE;
import static com.sfl.core.repository.constants.CacheConstants.COLUMN_LAST_MODIFIED_DATE;

/***
 * @author Henil Mistry
 *
 * This component use for common query data structure prediction.
 * @param <T>
 */
@Component
public class CommonFilterSpecification<T> {

    private final Logger log = LoggerFactory.getLogger(CommonFilterSpecification.class);

    /**
     * Based on provided Entity type and filter criteria prepare the predicates.
     * @param filterRequestDTO
     * @return
     * @param <T>
     */
    public  <T> Specification<T> applyFilters(FilterRequestDTO filterRequestDTO) {
        log.info("Apply filter specification.");
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            String entityName = root.getModel().getName();
            log.info("Apply filter specification on entity: {}",entityName);
            // Example of applying date filters for "createdDate" from AbstractAuditingEntity
            String dateRangeField = filterRequestDTO.isRangeBasedOnUpdatedDate()? COLUMN_LAST_MODIFIED_DATE:COLUMN_CREATED_DATE;
            if (filterRequestDTO.getStartDate() != null) {
                Instant startInstant = filterRequestDTO.getStartDate().toInstant(ZoneOffset.UTC);
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.greaterThanOrEqualTo(root.get(dateRangeField), startInstant));
            }
            if (filterRequestDTO.getEndDate() != null) {
                Instant endInstant = filterRequestDTO.getEndDate().toInstant(ZoneOffset.UTC);
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.lessThanOrEqualTo(root.get(dateRangeField), endInstant));
            }

            // Apply dynamic filters
            for (Map.Entry<String, Object> entry : filterRequestDTO.getFilter().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value != null) {
                    Path<String> path = getPath(root, key);
                    predicate = setPredicateByType(path, key, value,criteriaBuilder,predicate);
                }
            }

            if (filterRequestDTO.getSearchText() != null && !filterRequestDTO.getSearchText().isEmpty()) {
                String searchText = "%" + filterRequestDTO.getSearchText().toLowerCase() + "%";
                Predicate searchPredicate = criteriaBuilder.disjunction();
                log.info("Apply filter search text: {}",searchText);
                if (filterRequestDTO.getSearchText() != null) {
                    for (String field : filterRequestDTO.getSearchFields()) {
                        Path<String> path = getPath(root, field);
                        searchPredicate = criteriaBuilder.or(searchPredicate,
                            criteriaBuilder.like(criteriaBuilder.lower(path.as(String.class)), searchText));
                    }
                }
                predicate = criteriaBuilder.and(predicate, searchPredicate);
            }

            // Apply sorting
            if (filterRequestDTO.getSortBy() != null && !filterRequestDTO.getSortBy().isEmpty()) {
                String sortBy = filterRequestDTO.getSortBy();
                String sortDirection = filterRequestDTO.getSortDirection() != null ? filterRequestDTO.getSortDirection() : "ASC";
                // Determine the sort path based on whether it involves the parent entity
                Path<?> sortPath = getPath(root, sortBy);
                // Apply sorting to the query
                if ("DESC".equalsIgnoreCase(sortDirection)) {
                    query.orderBy(criteriaBuilder.desc(sortPath));
                } else {
                    query.orderBy(criteriaBuilder.asc(sortPath));
                }
            }

            return predicate;
        };
    }

    /**
     * It's method to check Entity filed type
     * Reason of checking is while predictor predicts the query that time
     * for enum and other type syntax is different
     * @param path
     * @param value
     * @param criteriaBuilder
     * @param predicate
     * TODO: this is method take change in future if coming any error related to casting
     */
    private Predicate setPredicateByType(Path<?> path, String key, Object value, CriteriaBuilder criteriaBuilder, Predicate predicate) {
        Class<?> fieldType = path.getJavaType();
        if (fieldType.isEnum()) {
            try {
                @SuppressWarnings("unchecked")
                Class<Enum> enumType = (Class<Enum>) fieldType;
                Enum<?> enumValue = Enum.valueOf(enumType, (String) value);
                return criteriaBuilder.and(predicate, criteriaBuilder.equal(path, enumValue));
            } catch (IllegalArgumentException e) {
                log.error("Failed to cast the value to the appropriate enum type for key: {}", key, e);
            }
        } else {
            return criteriaBuilder.and(predicate, criteriaBuilder.equal(path, value));
        }
        return predicate;
    }

    /**
     *Helper method to handle nested paths
     */
    private <T> Path<String> getPath(Root<T> root, String field) {
        if (field.contains(".")) {
            String[] parts = field.split("\\.");
            Path<?> path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            return (Path<String>) path;
        } else {
            return root.get(field);
        }
    }
}
