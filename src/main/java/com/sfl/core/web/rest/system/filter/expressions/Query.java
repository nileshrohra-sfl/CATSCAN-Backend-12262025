package com.sfl.core.web.rest.system.filter.expressions;
import com.sfl.core.web.rest.system.filter.CriteriaKeyValuePair;

public class Query {

    protected ExpressionTreeNode.Operator treeOperator = ExpressionTreeNode.Operator.NOOP;

    protected  CriteriaKeyValuePair[] values;
    protected SimpleQuery left;

    protected SimpleQuery right;

    protected String entity;

    private OrderBy[] orderBy = new OrderBy[]{};

    public Query(String entity, ExpressionTreeNode.Operator treeOperator,
                 CriteriaKeyValuePair[] values, SimpleQuery left, SimpleQuery right) {
        this.entity = entity;
        this.treeOperator = treeOperator;
        this.values = values;
        this.left = left;
        this.right = right;
    }

    public Query(String entity) {
        this.entity = entity;
    }

    public Query() {
    }

    public ExpressionTreeNode.Operator getTreeOperator() {
        return treeOperator;
    }

    public void setTreeOperator(ExpressionTreeNode.Operator treeOperator) {
        this.treeOperator = treeOperator;
    }


    public SimpleQuery getLeft() {
        return left;
    }

    public void setLeft(SimpleQuery left) {
        this.left = left;
    }

    public SimpleQuery getRight() {
        return right;
    }

    public void setRight(SimpleQuery right) {
        this.right = right;
    }

    public CriteriaKeyValuePair[] getValues() {
        return values;
    }

    public void setValues(CriteriaKeyValuePair[] values) {
        this.values = values;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public OrderBy[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderBy[] orderBy) {
        this.orderBy = orderBy;
    }
}
