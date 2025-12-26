package com.sfl.core.web.rest.system.filter;
import com.sfl.core.config.Constants;
import liquibase.pro.packaged.X;
import liquibase.pro.packaged.Z;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.sfl.core.config.Constants.ANY_WILDCARD;
import static com.sfl.core.config.Constants.PATH_SEPARATOR;

public class PredicateHandler<T> {
    private static final String ENUM_PACKAGE_NAME = "com.sfl.sfl.core.domain.enumeration.";

    private Class<?> loadEnumClass(String enumName) throws ClassNotFoundException {
        return Class.forName(ENUM_PACKAGE_NAME + enumName);
    }

    private Predicate getEnumPredicate(CriteriaKeyValuePair c, CriteriaBuilder cb, Root<X> root) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Predicate p;
        String enumName = c.getEnumName();
        Class<?> clazz = loadEnumClass(enumName);
        Expression<T> path = buildPath(c.getKey(), root);
        Object value = clazz.getField(c.getValue().toString()).get(clazz);
        if (c.getOperator() == CriteriaKeyValuePair.Operator.EQUAL) {
            p = cb.equal(path, value);
        } else {
            p = cb.notEqual(path, value);
        }
        return p;
    }

    private Predicate getStringPredicate(CriteriaKeyValuePair c, CriteriaBuilder cb, Root<X> root) {
        Predicate p = null;
        String value = c.getValue().toString();
        Expression<String> path = buildPath(c.getKey(), root);
        if (c.getOperator() == CriteriaKeyValuePair.Operator.LIKE) {
            p = cb.like(path, ANY_WILDCARD + value + ANY_WILDCARD);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.EQUAL) {
            p = cb.equal(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.NOT_EQUAL) {
            p = cb.notEqual(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.GREATER_THAN) {
            p = cb.greaterThan(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.LESS_THAN) {
            p = cb.lessThan(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.GREATER_THAN_OR_EQUAL) {
            p = cb.greaterThanOrEqualTo(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.LESS_THAN_OR_EQUAL) {
            p = cb.lessThanOrEqualTo(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.IN) {
            List<Predicate> inQueryPredicates = new ArrayList<>();
            for (String searchIn : value.split(Constants.SEPARATOR)) {
                inQueryPredicates.add(cb.like(path, ANY_WILDCARD + searchIn + ANY_WILDCARD));
            }
            p = cb.or(inQueryPredicates.toArray(new Predicate[0]));
        }
        return p;
    }

    private Predicate getDatePredicate(CriteriaKeyValuePair c, CriteriaBuilder cb, Root<X> root) {
        Predicate p = null;
        ZonedDateTime value;
        try {
            value = ZonedDateTime.parse(c.getValue().toString());
        } catch (Exception e) {
            throw new InvalidFilterStateException(String.format("Unknown date class %s for value %s", c.getValue().getClass().getSimpleName(), c.getValue()));
        }
        Expression<ZonedDateTime> path = buildPath(c.getKey(), root);
        if (c.getOperator() == CriteriaKeyValuePair.Operator.LIKE) {
            throw new OperatorInvalidForDataTypeException(c.getDataType(), c.getOperator());
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.EQUAL) {
            p = cb.equal(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.NOT_EQUAL) {
            p = cb.notEqual(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.GREATER_THAN) {
            p = cb.greaterThan(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.LESS_THAN) {
            p = cb.lessThan(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.GREATER_THAN_OR_EQUAL) {
            p = cb.greaterThanOrEqualTo(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.LESS_THAN_OR_EQUAL) {
            p = cb.lessThanOrEqualTo(path, value);
        }
        return p;
    }

    private Predicate getBooleanPredicate(CriteriaKeyValuePair c, CriteriaBuilder cb, Root<X> root) {
        Predicate p;
        Expression<Boolean> path = buildPath(c.getKey(), root);
        if (c.getValue() == null) {
            return cb.isNull(path);
        }
        Boolean value = Boolean.parseBoolean(c.getValue().toString());
        if (c.getOperator() == CriteriaKeyValuePair.Operator.LIKE) {
            throw new OperatorInvalidForDataTypeException(c.getDataType(), c.getOperator());
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.EQUAL) {
            p = cb.equal(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.NOT_EQUAL) {
            p = cb.notEqual(path, value);
        } else {
            throw new OperatorInvalidForDataTypeException(c.getDataType(), c.getOperator());
        }
        return p;
    }

    private Predicate getLongPredicate(CriteriaKeyValuePair c, CriteriaBuilder cb, Root<X> root) {
        Predicate p = null;
        Long value = Long.parseLong(c.getValue().toString());
        Expression<Long> path = buildPath(c.getKey(), root);
        if (c.getOperator() == CriteriaKeyValuePair.Operator.LIKE) {
            throw new OperatorInvalidForDataTypeException(c.getDataType(), c.getOperator());
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.EQUAL) {
            p = cb.equal(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.NOT_EQUAL) {
            p = cb.notEqual(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.GREATER_THAN) {
            p = cb.greaterThan(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.LESS_THAN) {
            p = cb.lessThan(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.GREATER_THAN_OR_EQUAL) {
            p = cb.greaterThanOrEqualTo(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.LESS_THAN_OR_EQUAL) {
            p = cb.lessThanOrEqualTo(path, value);
        }
        return p;
    }

    private Predicate getIntPredicate(CriteriaKeyValuePair c, CriteriaBuilder cb, Root<X> root) {
        Predicate p = null;
        Integer value = Integer.parseInt(c.getValue().toString());
        Expression<Integer> path = buildPath(c.getKey(), root);
        if (c.getOperator() == CriteriaKeyValuePair.Operator.LIKE) {
            throw new OperatorInvalidForDataTypeException(c.getDataType(), c.getOperator());
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.EQUAL) {
            p = cb.equal(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.NOT_EQUAL) {
            p = cb.notEqual(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.GREATER_THAN) {
            p = cb.greaterThan(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.LESS_THAN) {
            p = cb.lessThan(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.GREATER_THAN_OR_EQUAL) {
            p = cb.greaterThanOrEqualTo(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.LESS_THAN_OR_EQUAL) {
            p = cb.lessThanOrEqualTo(path, value);
        }
        return p;
    }

    private Predicate getDoublePredicate(CriteriaKeyValuePair c, CriteriaBuilder cb, Root<X> root) {
        Predicate p = null;
        Double value = Double.parseDouble(c.getValue().toString());
        Expression<Double> path = buildPath(c.getKey(), root);
        if (c.getOperator() == CriteriaKeyValuePair.Operator.LIKE) {
            throw new OperatorInvalidForDataTypeException(c.getDataType(), c.getOperator());
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.EQUAL) {
            p = cb.equal(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.NOT_EQUAL) {
            p = cb.notEqual(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.GREATER_THAN) {
            p = cb.greaterThan(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.LESS_THAN) {
            p = cb.lessThan(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.GREATER_THAN_OR_EQUAL) {
            p = cb.greaterThanOrEqualTo(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.LESS_THAN_OR_EQUAL) {
            p = cb.lessThanOrEqualTo(path, value);
        }
        return p;
    }

    private Predicate getFloatPredicate(CriteriaKeyValuePair c, CriteriaBuilder cb, Root<X> root) {
        Predicate p = null;
        Float value = Float.parseFloat(c.getValue().toString());
        Expression<Float> path = buildPath(c.getKey(), root);
        if (c.getOperator() == CriteriaKeyValuePair.Operator.LIKE) {
            throw new OperatorInvalidForDataTypeException(c.getDataType(), c.getOperator());
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.EQUAL) {
            p = cb.equal(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.NOT_EQUAL) {
            p = cb.notEqual(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.GREATER_THAN) {
            p = cb.greaterThan(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.LESS_THAN) {
            p = cb.lessThan(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.GREATER_THAN_OR_EQUAL) {
            p = cb.greaterThanOrEqualTo(path, value);
        } else if (c.getOperator() == CriteriaKeyValuePair.Operator.LESS_THAN_OR_EQUAL) {
            p = cb.lessThanOrEqualTo(path, value);
        }
        return p;
    }

    private Predicate getNumberPredicate(CriteriaKeyValuePair c, CriteriaBuilder cb, Root<X> root) {
        Predicate p = null;
        final BigDecimal bigDecimal = new BigDecimal(c.getValue().toString());

        if (c.getOperator() == CriteriaKeyValuePair.Operator.LIKE) {
            throw new OperatorInvalidForDataTypeException(c.getDataType(), c.getOperator());
        } else {
            Expression<Number> numberPath = buildPath(c.getKey(), root);
            if (c.getOperator() == CriteriaKeyValuePair.Operator.EQUAL) {
                p = cb.equal(numberPath, bigDecimal);
            } else if (c.getOperator() == CriteriaKeyValuePair.Operator.NOT_EQUAL) {
                p = cb.notEqual(numberPath, bigDecimal);
            } else if (c.getOperator() == CriteriaKeyValuePair.Operator.GREATER_THAN) {
                p = cb.gt(numberPath, bigDecimal);
            } else if (c.getOperator() == CriteriaKeyValuePair.Operator.LESS_THAN) {
                p = cb.lt(numberPath, bigDecimal);
            } else if (c.getOperator() == CriteriaKeyValuePair.Operator.GREATER_THAN_OR_EQUAL) {
                p = cb.ge(numberPath, bigDecimal);
            } else if (c.getOperator() == CriteriaKeyValuePair.Operator.LESS_THAN_OR_EQUAL) {
                p = cb.le(numberPath, bigDecimal);
            }
        }
        return p;
    }


    private Predicate processCriteriaNull(CriteriaKeyValuePair c, CriteriaBuilder cb, Root<X> root) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        Predicate p;
        if (c.getDataType() == DataType.STRING) {
            p = getStringPredicate(c, cb, root);
        } else if (c.getDataType() == DataType.ENUM) {
            p = getEnumPredicate(c, cb, root);
        } else if (c.getDataType() == DataType.NUMBER) {
            p = getNumberPredicate(c, cb, root);
        } else if (c.getDataType() == DataType.INTEGER) {
            p = getIntPredicate(c, cb, root);
        } else if (c.getDataType() == DataType.LONG) {
            p = getLongPredicate(c, cb, root);
        } else if (c.getDataType() == DataType.FLOAT) {
            p = getFloatPredicate(c, cb, root);
        } else if (c.getDataType() == DataType.DOUBLE) {
            p = getDoublePredicate(c, cb, root);
        } else if (c.getDataType() == DataType.DATE) {
            p = getDatePredicate(c, cb, root);
        } else if (c.getDataType() == DataType.BOOLEAN) {
            p = getBooleanPredicate(c, cb, root);
        } else {
            //Should not come here
            throw new InvalidFilterStateException();
        }
        return p;
    }

    private Predicate processCriteriaOr(Predicate p, CriteriaKeyValuePair c, CriteriaBuilder cb, Root<X> root) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {

        if (c.getDataType() == DataType.STRING) {
            p = cb.or(p, getStringPredicate(c, cb, root));
        } else if (c.getDataType() == DataType.ENUM) {
            p = cb.or(p, getEnumPredicate(c, cb, root));
        } else if (c.getDataType() == DataType.NUMBER) {
            p = cb.or(p, getNumberPredicate(c, cb, root));
        } else if (c.getDataType() == DataType.INTEGER) {
            p = cb.or(p, getIntPredicate(c, cb, root));
        } else if (c.getDataType() == DataType.LONG) {
            p = cb.or(p, getLongPredicate(c, cb, root));
        } else if (c.getDataType() == DataType.DOUBLE) {
            p = cb.or(p, getDoublePredicate(c, cb, root));
        } else if (c.getDataType() == DataType.FLOAT) {
            p = cb.or(p, getFloatPredicate(c, cb, root));
        } else if (c.getDataType() == DataType.BOOLEAN) {
            p = cb.or(p, getBooleanPredicate(c, cb, root));
        } else if (c.getDataType() == DataType.DATE) {
            p = cb.or(p, getDatePredicate(c, cb, root));
        } else {
            //Should not come here
            throw new InvalidFilterStateException();
        }
        return p;
    }

    public Predicate processCriteriaAnd(Predicate p, CriteriaKeyValuePair c, CriteriaBuilder cb, Root<X> root) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {

        if (c.getDataType() == DataType.STRING) {
            p = cb.and(p, getStringPredicate(c, cb, root));
        } else if (c.getDataType() == DataType.ENUM) {
            p = cb.and(p, getEnumPredicate(c, cb, root));
        } else if (c.getDataType() == DataType.NUMBER) {
            p = cb.and(p, getNumberPredicate(c, cb, root));
        } else if (c.getDataType() == DataType.INTEGER) {
            p = cb.and(p, getIntPredicate(c, cb, root));
        } else if (c.getDataType() == DataType.LONG) {
            p = cb.and(p, getLongPredicate(c, cb, root));
        } else if (c.getDataType() == DataType.DOUBLE) {
            p = cb.and(p, getDoublePredicate(c, cb, root));
        } else if (c.getDataType() == DataType.FLOAT) {
            p = cb.and(p, getFloatPredicate(c, cb, root));
        } else if (c.getDataType() == DataType.BOOLEAN) {
            p = cb.and(p, getBooleanPredicate(c, cb, root));
        } else if (c.getDataType() == DataType.DATE) {
            p = cb.and(p, getDatePredicate(c, cb, root));
        } else {
            //Should not come here
            throw new InvalidFilterStateException();
        }
        return p;
    }

    public Predicate processCriteria(CriteriaKeyValuePair[] criteriaPairs, CriteriaBuilder cb, Root<X> root, boolean or) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        Predicate p = null;
        for (CriteriaKeyValuePair c : criteriaPairs) {
            if (p == null) {
                p = processCriteriaNull(c, cb, root);
            } else if (or) {
                p = processCriteriaOr(p, c, cb, root);
            } else {
                p = processCriteriaAnd(p, c, cb, root);
            }
        }
        if (p == null) {
            p = cb.and();
        }
        return p;
    }

    public <V> Expression<V> buildPath(String key, Root<X> root) {

        Expression<V> path = null;
        if (key.contains(".")) {
            //get the name of the final field in the full multi-component path
            String attribute = key.substring(key.lastIndexOf('.') + 1);
            //drop the part after the last '.' - It is the name of a field and is not a type
            key = key.substring(0, key.lastIndexOf('.'));

            String[] types = key.split(PATH_SEPARATOR);
            Join<Z, X> join = null;
            for (String type : types) {
                if (join == null) {
                    join = root.join(type);
                } else {
                    join = join.join(type);
                }
            }
            if (join != null) {
                path = join.get(attribute);
            }
        } else {
            path = root.get(key);
        }
        return path;
    }
}
