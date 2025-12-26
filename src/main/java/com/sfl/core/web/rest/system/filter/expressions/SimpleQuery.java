package com.sfl.core.web.rest.system.filter.expressions;
import com.sfl.core.web.rest.system.filter.CriteriaKeyValuePair;

public class SimpleQuery {
    private ExpressionTreeNode.Operator treeOperator = ExpressionTreeNode.Operator.NOOP;
    private  CriteriaKeyValuePair[] values = new CriteriaKeyValuePair[]{};

    private OrderBy[] orderBy = new OrderBy[]{};

    public SimpleQuery() {
    }

    public SimpleQuery(ExpressionTreeNode.Operator treeOperator, CriteriaKeyValuePair[] values) {
        this.treeOperator = treeOperator;
        this.values = values;
    }

    public ExpressionTreeNode.Operator getTreeOperator() {
        return treeOperator;
    }

    public void setTreeOperator(ExpressionTreeNode.Operator treeOperator) {
        this.treeOperator = treeOperator;
    }

    public CriteriaKeyValuePair[] getValues() {
        return values;
    }

    public void setValues(CriteriaKeyValuePair[] values) {
        this.values = values;
    }

    public OrderBy[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderBy[] orderBy) {
        this.orderBy = orderBy;
    }
}
