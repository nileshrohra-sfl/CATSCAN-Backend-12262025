package com.sfl.core.web.rest.system.filter.expressions;

import com.sfl.core.service.mapper.EntityMapper;
import com.sfl.core.web.rest.errors.GenericException;
import com.sfl.core.web.rest.errors.GenericFilterException;
import com.sfl.core.web.rest.system.filter.*;
import liquibase.pro.packaged.D;
import liquibase.pro.packaged.E;
import liquibase.pro.packaged.T;
import liquibase.pro.packaged.X;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
@Component
public class ExpressionTreeEvaluatorImpl implements ExpressionTreeEvaluator {

    private final Logger logger = LoggerFactory.getLogger(ExpressionTreeEvaluatorImpl.class);

    private static final String PACKAGE_NAME_ENTITY = "com.sfl.core.domain.";
    private static final String PACKAGE_NAME_MAPPER = "com.sfl.core.service.mapper.";

    private static final String TOTAL_ROWS_LOG = "Total Rows {}";

    private final ApplicationContext appContext;

    private final Map<String, EntityMapper<D, E>> mappers = new HashMap<>();

    private CriteriaBuilder criteriaBuilder;

    private final PredicateHandler<T> predicateHandler = new PredicateHandler<>();

    private EntityManager entityManager = null;

    private CriteriaQuery query;

    private Class clz;

    private Root<X> root;

    public ExpressionTreeEvaluatorImpl(ApplicationContext appContext) {
        this.appContext = appContext;}

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    private Class loadClass(String packageName, String entity) {
        try {
            String fullyQualifiedEntityName = packageName + entity;
            logger.info("Loading entity class {}", fullyQualifiedEntityName);
            return Class.forName(fullyQualifiedEntityName);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
            throw new EntityClassNotFound(entity);
        }
    }

    private EntityMapper<D, E> loadMapper(String entity) {

        String mapperClass = entity + "Mapper";
        logger.info("Loading Mapper Class {}", entity);
        if (!mappers.containsKey(entity)) {
            try {
                Class c = loadClass(PACKAGE_NAME_MAPPER, mapperClass);
                EntityMapper mapper = (EntityMapper) appContext.getBean(c);
                mappers.put(entity, mapper);
            } catch (Exception e) {
                throw new GenericFilterException("EntityMapper " + clz.getSimpleName() + " Not found", 500, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return mappers.get(entity);
    }

    private Predicate evaluate(CriteriaKeyValuePair[] criteriaPairs, ExpressionTreeNode.Operator operator) {
        if (criteriaPairs == null) {
            criteriaPairs = new CriteriaKeyValuePair[]{};
        }
        Predicate p = null;
        try {
            switch (operator) {
                case NOOP:
                case AND:
                    p = predicateHandler.processCriteria(criteriaPairs, criteriaBuilder, root, false);
                    break;
                case OR:
                    p = predicateHandler.processCriteria(criteriaPairs, criteriaBuilder, root, true);
                    break;
                case NOT:
                    p = criteriaBuilder.not(predicateHandler.processCriteria(criteriaPairs, criteriaBuilder, root, false));
                    break;
            }
        } catch (OperatorInvalidForDataTypeException | InvalidFilterStateException | IllegalAccessException |
                 ClassNotFoundException | NoSuchFieldException | IllegalArgumentException e) {
            throw new GenericFilterException(e);
        }
        return p;
    }


    /**
     * Get filter data based on root node and page.
     *
     * @param rootNode       for the predict.
     * @param page           page details.
     * @param withOutMapping set value true if don't want to map and direct return entity.
     */
    public Page execute(ExpressionTreeNode rootNode, Pageable page, Boolean withOutMapping, Boolean isDistinct) {
        logger.debug("Request to get filter data based on root node and page details ");
        initialize(rootNode);
        EntityMapper<D, E> mapper = loadMapper(clz.getSimpleName());
        Predicate predicate = evaluateInner(rootNode);
        long totalRows = count(predicate);
        logger.info(TOTAL_ROWS_LOG, totalRows);
        initialize(rootNode);
        predicate = evaluateInner(rootNode);
        query = query.where(predicate);
        OrderBy[] orderBy = rootNode.orderBy();
        if (orderBy == null || orderBy.length == 0) {
            query = query.orderBy(criteriaBuilder.asc(root.get("id")));
        } else {
            List<Order> orders = new ArrayList<>();
            for (OrderBy field : orderBy) {
                if (field != null) {
                    Expression<T> e = predicateHandler.buildPath(field.field, root);
                    if (field.ascending) {
                        orders.add(criteriaBuilder.asc(e));
                    } else {
                        orders.add(criteriaBuilder.desc(e));
                    }
                }
            }
            query.orderBy(orders);
        }

        if (Boolean.TRUE.equals(isDistinct)) {
            query.distinct(true);
        }
        TypedQuery<E> q = entityManager.createQuery(query);
        int pageNumber = page.getPageNumber();
        int pageSize = page.getPageSize();
        int firstResult = pageNumber * pageSize;
        q.setFirstResult(firstResult);
        q.setMaxResults(pageSize);
        List<E> list = q.getResultList();
        if (Boolean.TRUE.equals(withOutMapping)) {
            return new PageImpl<>(list, page, totalRows);
        }
        return new PageImpl<T>((List) list.stream().map(mapper::toDto).collect(Collectors.toList()), page, totalRows);
    }


    public Page getIdsWithFilter(ExpressionTreeNode rootNode, Pageable page) {
        initializeForId(rootNode);
        Predicate predicate = evaluateInner(rootNode);
        long totalRows = count(predicate);
        logger.info(TOTAL_ROWS_LOG, totalRows);
        query = query.where(predicate);
        OrderBy[] orderBy = rootNode.orderBy();
        if (orderBy == null || orderBy.length == 0) {
            query = query.orderBy(criteriaBuilder.asc(root.get("id")));
        } else {
            for (OrderBy field : orderBy) {
                if (field != null) {
                    Expression<T> e = predicateHandler.buildPath(field.field, root);
                    if (field.ascending) {
                        query = query.orderBy(criteriaBuilder.asc(e));
                    } else {
                        query = query.orderBy(criteriaBuilder.desc(e));
                    }
                }
            }
        }
        query.select(root.get("id"));
        TypedQuery<X> q = entityManager.createQuery(query);
        int pageNumber = page.getPageNumber();
        int pageSize = page.getPageSize();
        int firstResult = pageNumber * pageSize;
        q.setFirstResult(firstResult);
        q.setMaxResults(pageSize);
        List<?> list = q.getResultList();
        return new PageImpl<>((List<T>) list, page, totalRows);
    }


    public List getIdsWithFilter(ExpressionTreeNode rootNode) {
        initializeForId(rootNode);
        Predicate predicate = evaluateInner(rootNode);
        OrderBy[] orderBy = rootNode.orderBy();
        if (orderBy == null || orderBy.length == 0) {
            query = query.orderBy(criteriaBuilder.asc(root.get("id")));
        } else {
            for (OrderBy field : orderBy) {
                if (field != null) {
                    Expression<T> e = predicateHandler.buildPath(field.field, root);
                    if (field.ascending) {
                        query = query.orderBy(criteriaBuilder.asc(e));
                    } else {
                        query = query.orderBy(criteriaBuilder.desc(e));
                    }
                }
            }
        }
        query.select(root.get("id"));
        return entityManager.createQuery(query.where(predicate)).getResultList();
    }


    public Long count(Predicate predicate) {
        query.select(criteriaBuilder.count(root));
        if (predicate != null) {
            query.where(predicate);
        }
        try {
            return (Long) entityManager.createQuery(query).getSingleResult();
        } catch (Exception e) {
            throw new GenericException(e);
        }
    }

    private Predicate evaluateInner(ExpressionTreeNode rootNode) {
        if (rootNode == null) {
            return null;
        }
        rootNode.assertIsValid();

        if (rootNode.isNodeEvaluable()) {
            return evaluate(rootNode.value(), rootNode.operator());
        } else {
            Predicate leftValue = evaluateInner(rootNode.left());

            Predicate rightValue = evaluateInner(rootNode.right());

            if (leftValue == null && rightValue == null) {
                return criteriaBuilder.and();
            }

            if (leftValue != null && rightValue == null) {
                return checkLeftNotNullAndRightNull(rootNode, criteriaBuilder, leftValue);
            }

            if (leftValue == null) {
                return checkLeftNull(rootNode, criteriaBuilder, rightValue);
            }
            if (Boolean.TRUE.equals(isBinaryOperator(rootNode))) {
                return processOperator(rootNode, leftValue, rightValue);
            }
        }
        return criteriaBuilder.and();
    }

    private Predicate checkLeftNull(ExpressionTreeNode rootNode, CriteriaBuilder criteriaBuilder, Predicate rightValue) {
        if (rootNode.isOperatorNot()) {
            return criteriaBuilder.not(rightValue);
        }
        //operator is NOOP
        assert (rootNode.isOperatorNoop());
        return rightValue;
    }

    private Predicate checkLeftNotNullAndRightNull(ExpressionTreeNode rootNode, CriteriaBuilder criteriaBuilder, Predicate leftValue) {
        if (rootNode.isOperatorNot()) {
            return criteriaBuilder.not(leftValue);
        }
        //operator is NOOP
        assert (rootNode.isOperatorNoop());
        return leftValue;
    }


    private Boolean isBinaryOperator(ExpressionTreeNode rootNode) {
        return (rootNode.isOperatorOr() || rootNode.isOperatorAnd());
    }

    private Predicate processOperator(ExpressionTreeNode rootNode, Predicate leftValue, Predicate rightValue) {
        // Check which operator to apply
        if (rootNode.isOperatorAnd()) {
            return criteriaBuilder.and(leftValue, rightValue);
        } else {
            return criteriaBuilder.or(leftValue, rightValue);
        }
    }

    private void initialize(ExpressionTreeNode rootNode) {
        rootNode.assertEntitySpecified();
        rootNode.assertIsValid();
        clz = loadClass(PACKAGE_NAME_ENTITY, rootNode.entity());
        query = criteriaBuilder.createQuery(clz);
        root = query.from(clz);
    }

    private void initializeForId(ExpressionTreeNode rootNode) {
        clz = loadClass(PACKAGE_NAME_ENTITY, rootNode.entity());
        query = criteriaBuilder.createQuery(Long.class);
        root = query.from(clz);
    }
}
