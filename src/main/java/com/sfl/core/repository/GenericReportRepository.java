package com.sfl.core.repository;

import com.sfl.core.domain.MissingProduct;
import com.sfl.core.domain.SflUserExtend;
import com.sfl.core.domain.TryAgainProduct;
import com.sfl.core.domain.UserDeviceDetail;
import com.sfl.core.service.dto.RequestReportDTO;
import com.sfl.core.service.mapper.CommonAfterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sfl.core.repository.constants.CacheConstants.*;

@Repository
public class GenericReportRepository {

    private final Logger log = LoggerFactory.getLogger(GenericReportRepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    private static final String TABLE_MISSING_PRODUCTS = "missing-products";
    private static final String FIELD_BARCODE_IMAGE = "barcodeImage";
    private static final String FIELD_PRODUCT_FRONT_IMAGE = "productFrontImage";
    private static final String FIELD_CATSCAN_ERROR_IMAGE = "catscanErrorImage";
    private static final String FIELD_INGREDIENT_IMAGE = "ingredientImage";
    private static final String FIELD_OTHER_IMAGES = "otherImages";
    private final CommonAfterMapper commonAfterMapper;

    public GenericReportRepository(CommonAfterMapper commonAfterMapper) {
        this.commonAfterMapper = commonAfterMapper;
    }

    /**
     *  Based on request filter prepare dynamic query to fetch data.
      * @param requestReportDTO
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Object[]> fetchReportData(RequestReportDTO requestReportDTO, Pageable pageable) {
        log.info("fetching report for table : {}",requestReportDTO.getTableName());
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<?> root = query.from(entityManager.getMetamodel().entity(resolveEntityClass(requestReportDTO.getTableName())));
        log.info("Root entity selected based on table name.");
        // Select fields
        List<Selection<?>> selections = requestReportDTO.getFields().stream()
            .map(field -> resolvePath(root, field))
            .collect(Collectors.toList());
        query.multiselect(selections);
        log.info("Select clause is prepared.");

        // Apply filters
        List<Predicate> predicates = new ArrayList<>();
        if(requestReportDTO.getFilter() != null && !requestReportDTO.getFilter().isEmpty()) {
            for (Map.Entry<String, Object> entry : requestReportDTO.getFilter().entrySet()) {
                var path = resolvePath(root, entry.getKey());
                var key =   (path.getJavaType().isEnum())?path.as(String.class):path;
                var value =  (path.getJavaType().isEnum())?entry.getValue().toString():entry.getValue();
                predicates.add(cb.equal(key, entry.getValue()));
            }
            log.info("Applied filter criteria on predicates.");
        }

        if (requestReportDTO.getStartDate() != null && requestReportDTO.getEndDate() != null) {
            Instant startInstant = requestReportDTO.getStartDate().toInstant(ZoneOffset.UTC);
            Instant endInstant = requestReportDTO.getEndDate().toInstant(ZoneOffset.UTC);
            predicates.add(cb.between(root.get(COLUMN_LAST_MODIFIED_DATE), startInstant, endInstant));

        }

        if(!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }
        // Apply sorting
        if ("desc".equalsIgnoreCase(requestReportDTO.getSortOrder())) {
            query.orderBy(cb.desc(root.get(requestReportDTO.getSortBy())));
        } else {
            query.orderBy(cb.asc(root.get(requestReportDTO.getSortBy())));
        }

        log.info("Executing record fetching query.");
        // Execute query with pagination
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        log.info("Record fetched.");

        List<Object[]> results = typedQuery.getResultList();
        // Process image fields for missing-products table
        if (TABLE_MISSING_PRODUCTS.equalsIgnoreCase(requestReportDTO.getTableName())) {
            log.info("Processing image fields for full URL conversion.");

            for (Object[] row : results) {
                int otherImagesCount = 0; // track how many times we’ve seen “otherImages”

                for (int i = 0; i < requestReportDTO.getFields().size(); i++) {
                    String fieldName = requestReportDTO.getFields().get(i);
                    Object value = row[i];

                    if (value == null) continue;

                    // Handle normal single image fields
                    if (isImageField(fieldName) && !FIELD_OTHER_IMAGES.equalsIgnoreCase(fieldName)) {
                        row[i] = commonAfterMapper.getFullUrl(value.toString());
                    }

                    // Handle otherImages field (appears twice in request)
                    else if (FIELD_OTHER_IMAGES.equalsIgnoreCase(fieldName)) {
                        otherImagesCount++;
                        try {
                            List<String> images = (List<String>) value;
                            String imageUrl = null;

                            // First occurrence → first image
                            if (otherImagesCount == 1 && !images.isEmpty() && images.get(0) != null) {
                                imageUrl = commonAfterMapper.getFullUrl(images.get(0));
                            }
                            // Second occurrence → second image
                            else if (otherImagesCount == 2 && images.size() > 1 && images.get(1) != null) {
                                imageUrl = commonAfterMapper.getFullUrl(images.get(1));
                            }

                            row[i] = imageUrl; // set only one image URL
                        } catch (Exception e) {
                            log.error("Error processing otherImages field: {}", e.getMessage());
                        }
                    }
                }
            }
        }

        // Get total count
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(resolveEntityClass(requestReportDTO.getTableName()))));
        countQuery.where(predicates.toArray(new Predicate[0]));
        long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    /***
     * According to select clause traverse root-sub entity member
     * @param root
     * @param field
     * @return
     */
    private Path<?> resolvePath(Root<?> root, String field) {
        if (field.contains(".")) {
            String[] parts = field.split("\\.");
            Path<?> path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            return path;
        }
        return root.get(field);
    }

    /**
     * Based on tableName pool-out the bean
     * @param tableName
     * @return
     */
    private Class<?> resolveEntityClass(String tableName) {
        return switch (tableName) {
            case TABLE_MISSING_PRODUCTS -> MissingProduct.class;
            case TABLE_TRY_AGAIN_PRODUCTS -> TryAgainProduct.class;
            case TABLE_EXTENDED_USER -> SflUserExtend.class;
            case TABLE_USER_DEVICE_DETAILS -> UserDeviceDetail.class;
            default ->{
                log.error("Registered entity for export data not found {}", tableName);
                throw new IllegalStateException("Unexpected value of table: " + tableName);
            }
        };
    }

    /**
     *  Based on request filter prepare dynamic query to fetch data.
     * @param requestReportDTO
     * @param pageable
     * @return
     */
    public Page<TryAgainProduct> fetchTryAgainProducts(RequestReportDTO requestReportDTO, Pageable pageable) {
        log.info("fetching report for table : {}",requestReportDTO.getTableName());
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TryAgainProduct> query = cb.createQuery(TryAgainProduct.class);
        Root<TryAgainProduct> root = query.from(TryAgainProduct.class);
        log.info("Root entity selected based on table name.");
        // Select fields

        log.info("Select clause is prepared.");

        // Apply filters
        List<Predicate> predicates = new ArrayList<>();
        if(requestReportDTO.getFilter() != null && !requestReportDTO.getFilter().isEmpty()) {
            for (Map.Entry<String, Object> entry : requestReportDTO.getFilter().entrySet()) {
                var path = resolvePath(root, entry.getKey());
                var key =   (path.getJavaType().isEnum())?path.as(String.class):path;
                predicates.add(cb.equal(key, entry.getValue()));
            }
            log.info("Applied filter criteria on predicates.");
        }

        if (requestReportDTO.getStartDate() != null && requestReportDTO.getEndDate() != null) {
            Instant startInstant = requestReportDTO.getStartDate().toInstant(ZoneOffset.UTC);
            Instant endInstant = requestReportDTO.getEndDate().toInstant(ZoneOffset.UTC);
            predicates.add(cb.between(root.get(COLUMN_LAST_MODIFIED_DATE), startInstant, endInstant));

        }

        if(!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }
        // Apply sorting
        if ("desc".equalsIgnoreCase(requestReportDTO.getSortOrder())) {
            query.orderBy(cb.desc(root.get(requestReportDTO.getSortBy())));
        } else {
            query.orderBy(cb.asc(root.get(requestReportDTO.getSortBy())));
        }

        log.info("Executing record fetching query.");
        // Execute query with pagination
        TypedQuery<TryAgainProduct> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        log.info("Record fetched.");
        // Get total count
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(TryAgainProduct.class)));
        countQuery.where(predicates.toArray(new Predicate[0]));
        long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(typedQuery.getResultList(), pageable, total);
    }

    /**
     * Check if the field is image field
     * @param fieldName Field name
     * @return boolean
     */
    private boolean isImageField(String fieldName) {
        log.info("Checking if field is image field: {}", fieldName);
        return List.of(FIELD_BARCODE_IMAGE, FIELD_PRODUCT_FRONT_IMAGE, FIELD_CATSCAN_ERROR_IMAGE, FIELD_INGREDIENT_IMAGE).contains(fieldName);
    }

}
