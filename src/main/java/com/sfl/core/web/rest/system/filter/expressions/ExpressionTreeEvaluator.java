package com.sfl.core.web.rest.system.filter.expressions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.Predicate;
import java.util.List;


public interface ExpressionTreeEvaluator<T> {

    Page<T> execute(ExpressionTreeNode rootNode, Pageable page, Boolean withOutMapping, Boolean isDistinct);

    Page<T> getIdsWithFilter(ExpressionTreeNode rootNode, Pageable page);

    List<T> getIdsWithFilter(ExpressionTreeNode rootNode);

    Long count(Predicate predicate);

}

