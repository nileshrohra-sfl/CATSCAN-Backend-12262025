package com.sfl.core.web.rest.system.filter.domain;

import com.sfl.core.domain.SflUser;
import com.sfl.core.web.rest.system.filter.CriteriaKeyValuePair;
import com.sfl.core.web.rest.system.filter.expressions.ExpressionTreeNode;
import com.sfl.core.web.rest.system.filter.expressions.Query;
import com.sfl.core.web.rest.system.filter.expressions.SimpleQuery;

public class SflUserQuery extends Query
{
    protected Boolean status = false;

    private static final String ENTITY_NAME = SflUser.class.getSimpleName();

    public SflUserQuery() {super(ENTITY_NAME);}

    public SflUserQuery(ExpressionTreeNode.Operator operator, CriteriaKeyValuePair[] values,
                        SimpleQuery left, SimpleQuery right, Boolean status)
    {
        super(ENTITY_NAME, operator, values, left, right);
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
