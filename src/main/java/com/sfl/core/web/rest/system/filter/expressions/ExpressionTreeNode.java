package com.sfl.core.web.rest.system.filter.expressions;

import com.sfl.core.service.util.ObjectSerializer;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.DomainFilterException;
import com.sfl.core.web.rest.system.filter.CriteriaKeyValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExpressionTreeNode {

    private static final Logger logger = LoggerFactory.getLogger(ExpressionTreeNode.class);

    public ExpressionTreeNode(Operator operator, CriteriaKeyValuePair[] values) {
        this.operator = operator;
        this.value = values;
    }

    public enum Operator {AND, OR, NOT, NOOP}

    private static final String ERR_MSG_NO_ENTITY_SPECIFIED = "Entity must be specified";
    private static final String ERR_MSG_NO_CHILDREN_AND_NOT_NOOP = "if there is no criteria and no child nodes, the node must have the null/NOOP operator :";
    private static final String ERR_MSG_HAS_BOTH_CHILDREN_BUT_NO_OPERATOR = "if there is no criteria but there are child nodes, the node must have either AND/OR operator :";
    private static final String ERR_MSG_SINGLE_CHILD_OPERATOR = "if there is a single child, the operator must be NOT or NOOP";
    private static final String ERR_MSG_SINGLE_CRITERION_AND_HAS_OPERATOR = "if there is exactly one criterion, the node must have either a null/NOOP operator or a NOT operator :";
    private static final String ERR_MSG_MULTIPLE_CRITERIA_NO_OPERATOR = "if there are more than one criteria, the node must have either AND/OR operator :";

    private Operator operator;
    private String entity;
    private CriteriaKeyValuePair[] value;
    private ExpressionTreeNode left;
    private ExpressionTreeNode right;
    private OrderBy[] orderBy = new OrderBy[]{};


    public ExpressionTreeNode() {
    }

    public ExpressionTreeNode(Query query){
        this.entity=query.getEntity();
        if(Objects.isNull(query.getTreeOperator())){
            throw new RuntimeException("Invalid Query Object,Operator is null");
        }
        this.operator=query.getTreeOperator();
        this.value = query.getValues();
        this.orderBy = query.getOrderBy();
        if(Objects.nonNull(query.getLeft()) && Objects.nonNull(query.getLeft().getTreeOperator())) {
            this.left = new ExpressionTreeNode(query.getLeft().getTreeOperator(), query.getLeft().getValues());
        }
        if(Objects.nonNull(query.getRight()) && Objects.nonNull(query.getRight().getTreeOperator())) {
            this.right = new ExpressionTreeNode(query.getRight().getTreeOperator(), query.getRight().getValues());
        }
    }

    public ExpressionTreeNode(String entity, CriteriaKeyValuePair[] criteriaPairs) {
        this.entity = entity;
        value = criteriaPairs;
        left = right = null;
    }

    public ExpressionTreeNode(Operator operator, ExpressionTreeNode left, ExpressionTreeNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public ExpressionTreeNode(String entity, Operator operator, ExpressionTreeNode left, ExpressionTreeNode right, OrderBy[] orderBy) {
        this.entity = entity;
        this.operator = operator;
        this.left = left;
        this.right = right;
        this.orderBy = orderBy;
    }



    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String entity() {
        return this.entity;
    }

    public OrderBy[] orderBy() {
        return this.orderBy;
    }

    public Operator operator() {
        return this.operator;
    }

    public ExpressionTreeNode left() {
        return this.left;
    }

    public ExpressionTreeNode right() {
        return this.right;
    }

    public CriteriaKeyValuePair[] value() {
        return this.value;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public CriteriaKeyValuePair[] getValue() {
        return value;
    }

    public void setValue(CriteriaKeyValuePair[] value) {
        this.value = value;
    }

    public ExpressionTreeNode getLeft() {
        return left;
    }

    public void setLeft(ExpressionTreeNode left) {
        this.left = left;
    }

    public ExpressionTreeNode getRight() {
        return right;
    }

    public void setRight(ExpressionTreeNode right) {
        this.right = right;
    }

    public OrderBy[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderBy[] orderBy) {
        this.orderBy = orderBy;
    }

    public void setOrderBy(String orderBy, boolean asc) {
        if (orderBy != null && orderBy.trim().length() > 0) {
            String[] components = orderBy.split(",");
            OrderBy[] array = Arrays.stream(components).filter(c -> c.trim().length() > 0).map(c -> new OrderBy(c.trim(), asc)).collect(Collectors.toList()).toArray(new OrderBy[]{});
            setOrderBy(array);
        }
    }

    public void assertEntitySpecified() {
        if (entity() == null || entity().length() == 0) {
            throw new DomainFilterException(ERR_MSG_NO_ENTITY_SPECIFIED, Constants.BAD_REQUEST_EXCEPTION, HttpStatus.BAD_REQUEST);
        }
    }


    private void checkLengthZero(String json) {
        if (hasNoChildren() && !isOperatorNoop())/* Default no condition search */ {
            throw new DomainFilterException(ERR_MSG_NO_CHILDREN_AND_NOT_NOOP + json, Constants.BAD_REQUEST_EXCEPTION, HttpStatus.BAD_REQUEST);
        }
        if (hasBothChildren() && !isBinaryOperatorNode()) {
            throw new DomainFilterException(ERR_MSG_HAS_BOTH_CHILDREN_BUT_NO_OPERATOR + json, Constants.BAD_REQUEST_EXCEPTION, HttpStatus.BAD_REQUEST);
        }
        //To allow NOT on an entire child node. The child node could be left or right, and could itself contain a CriteriaKeyValuePair array with an AND/OR operator
        if (hasChildren() && !hasBothChildren() && isBinaryOperatorNode()) {
            throw new DomainFilterException(ERR_MSG_SINGLE_CHILD_OPERATOR + json, Constants.BAD_REQUEST_EXCEPTION, HttpStatus.BAD_REQUEST);
        }

    }

    private void checkLengthOne(String json) {
        if (isBinaryOperatorNode() || !(isOperatorNot() || isOperatorNoop())) {
            throw new DomainFilterException(ERR_MSG_SINGLE_CRITERION_AND_HAS_OPERATOR + json, Constants.BAD_REQUEST_EXCEPTION, HttpStatus.BAD_REQUEST);
        }
    }

    private void checkLengthGreaterThanOne(String json) {
        if (value.length > 1 && !isBinaryOperatorNode()) {
            throw new DomainFilterException(ERR_MSG_MULTIPLE_CRITERIA_NO_OPERATOR + json, Constants.BAD_REQUEST_EXCEPTION, HttpStatus.BAD_REQUEST);
        }
    }

    public void assertIsValid() {
        String json = ObjectSerializer.serialize(this);

        if (operator() == null) {
            setOperator(Operator.NOOP);
        }
        if (value() == null) {
            setValue(new CriteriaKeyValuePair[]{});
        }
        logger.info("Evaluating RootNode {}", json);

        if (value.length == 0) {

            checkLengthZero(json);
        } else if (value.length == 1) {
            checkLengthOne(json);
        } else {
            checkLengthGreaterThanOne(json);
        }
    }


    @JsonIgnore
    private boolean hasNoChildren() {
        return !hasLeftChild() && !hasRightChild();
    }

    @JsonIgnore
    public boolean isBinaryOperatorNode() {
        return isOperatorAnd() || isOperatorOr();
    }

    @JsonIgnore
    public boolean isCriteriaNode() {
        return left() == null && right() == null && value().length > 0;
    }

    @JsonIgnore
    public boolean isValueNode() {
        return isCriteriaNode() && isOperatorNoop();
    }

    @JsonIgnore
    public boolean isNodeEvaluable() {
        return isBlankNode() || isCriteriaNode() || isValueNode();
    }

    @JsonIgnore
    public boolean isBlankNode() {
        return operator().equals(Operator.NOOP) && value.length == 0 && left == null && right == null;
    }

    @JsonIgnore
    public boolean isOperatorAnd() {
        return operator().equals(Operator.AND);
    }

    @JsonIgnore
    public boolean isOperatorOr() {
        return operator().equals(Operator.OR);
    }

    @JsonIgnore
    public boolean isOperatorNot() {
        return operator().equals(Operator.NOT);
    }

    @JsonIgnore
    public boolean isOperatorNoop() {
        return operator() == null || operator().equals(Operator.NOOP);
    }

    @JsonIgnore
    public boolean hasLeftChild() {
        return left() != null;
    }

    @JsonIgnore
    public boolean hasRightChild() {
        return right() != null;
    }

    @JsonIgnore
    public boolean hasChildren() {
        return hasLeftChild() || hasRightChild();
    }

    @JsonIgnore
    public boolean hasBothChildren() {
        return hasLeftChild() && hasRightChild();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpressionTreeNode that = (ExpressionTreeNode) o;
        return Objects.equals(operator, that.operator) &&
            Objects.equals(entity, that.entity) &&
            Objects.equals(left, that.left)
            && Objects.equals(right, that.right) &&
            Arrays.equals(value, that.value) &&
            Arrays.equals(orderBy, that.orderBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, operator, Arrays.hashCode(value), Arrays.hashCode(orderBy), left, right);
    }

    @Override
    public String toString() {
        return ObjectSerializer.serialize(this);
    }
}
