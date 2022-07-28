package com.zslin.basic.repository;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/6 17:24.
 */
public class SimpleSpecification<T> implements Specification<T> {

    public static final String GRATE_EQUAL = "ge"; //大于等于
    public static final String GRATE_THEN = "gt"; //大于
    public static final String LESS_EQUAL = "le"; //小于等于
    public static final String LESS_THEN = "lt"; //小于
    public static final String LIKE_BEGIN = "likeb"; // like '%?'
    public static final String LIKE_END = "likee"; //like '?%'
    public static final String LIKE = "like"; //like '%?%'
    public static final String LIKE_BEGIN_END = "likebe"; //like '%?%'
    public static final String NOT_LIKE_BEGIN = "nlikeb"; //not like '%?'
    public static final String NOT_LIKE_END = "nlikee"; //not like '?%'
    public static final String NOT_LIKE = "nlike"; //not like '%?%'
    public static final String NOT_LIKE_BEGIN_END = "nlikebe"; // not like '%?%'
    public static final String EQUAL = "eq"; //equal =
    public static final String NOT_EQUAL = "ne"; // not equal   !=
    public static final String IS_NULL = "isnull"; //is null
    public static final String NOT_NULL = "notnull";
    public static final String IN = "in"; //如果是IN，对应的Value应该是'1','3'，多个值用逗号隔开

    /**
     * 查询的条件列表，是一组列表
     * */
    private List<SpecificationOperator> opers;

    public SimpleSpecification(List<SpecificationOperator> opers) {
        this.opers = opers;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        int index = 0;
        //通过resultPre来组合多个条件
        Predicate resultPre = null;
        for(SpecificationOperator op:opers) {
            if(index++==0) {
                resultPre = generatePredicate(root,criteriaBuilder,op);
                continue;
            }
            Predicate pre = generatePredicate(root,criteriaBuilder,op);
            if(pre==null) continue;
            if("and".equalsIgnoreCase(op.getJoin())) {
                resultPre = criteriaBuilder.and(resultPre,pre);
            } else if("or".equalsIgnoreCase(op.getJoin())) {
                resultPre = criteriaBuilder.or(resultPre,pre);
            }
        }
        return resultPre;
    }

    private Expression<T>[] buildExpression(Root<T> root, CriteriaBuilder criteriaBuilder, String [] params) {
        Expression<T>[] res = new Expression[params.length];
        for(int i=0;i<params.length;i++) {
            res[i] = buildExpression(root, criteriaBuilder, params[i]);
        }
        return res;
    }

    private Expression buildExpression(Root<T> root, CriteriaBuilder criteriaBuilder, String param) {
        if(param.startsWith(SpecificationOperator.FUNCTION_FIELD_PRE)) {
            param = param.replace(SpecificationOperator.FUNCTION_FIELD_PRE, "");
            return root.get(param);
        } else {
            param = param.replace(SpecificationOperator.FUNCTION_VALUE_PRE, "");
            return criteriaBuilder.literal(param);
        }
    }

    private Predicate buildFunction(Root<T> root,CriteriaBuilder criteriaBuilder, SpecificationOperator op) {
        if(!isFunction(op)) {return null;}
        String oper = op.getOper();
        if(EQUAL.equalsIgnoreCase(oper)) {
            return criteriaBuilder.equal(criteriaBuilder.function(op.getFunction(), op.getFunReturnClass(), buildExpression(root, criteriaBuilder, op.getFunParams())), op.getValue());
        } if(GRATE_EQUAL.equalsIgnoreCase(oper)) {
            return criteriaBuilder.ge(criteriaBuilder.function(op.getFunction(), op.getFunReturnClass(), buildExpression(root, criteriaBuilder, op.getFunParams())), (Number)op.getValue());
        } else if(LESS_EQUAL.equalsIgnoreCase(oper)) {
            return criteriaBuilder.le(criteriaBuilder.function(op.getFunction(), op.getFunReturnClass(), buildExpression(root, criteriaBuilder, op.getFunParams())), (Number)op.getValue());
        } else if(GRATE_THEN.equalsIgnoreCase(oper)) {
            return criteriaBuilder.gt(criteriaBuilder.function(op.getFunction(), op.getFunReturnClass(), buildExpression(root, criteriaBuilder, op.getFunParams())), (Number)op.getValue());
        } else if(LESS_THEN.equalsIgnoreCase(oper)) {
            return criteriaBuilder.lt(criteriaBuilder.function(op.getFunction(), op.getFunReturnClass(), buildExpression(root, criteriaBuilder, op.getFunParams())), (Number)op.getValue());
        } else {
            return null;
        }
    }

    private boolean isFunction(SpecificationOperator op) {
        return op.getFunction()!=null && !"".equals(op.getFunction().trim());
    }

    private Predicate generatePredicate(Root<T> root,CriteriaBuilder criteriaBuilder, SpecificationOperator op) {
        Predicate res = null;
        if(isFunction(op)) {res = buildFunction(root, criteriaBuilder, op);}
        if(res!=null) {return res;}
        /*
        * 根据不同的操作符返回特定的查询*/
        if(EQUAL.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.equal(root.get(op.getKey()),op.getValue());
        } else if(GRATE_EQUAL.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.ge(root.get(op.getKey()), (Number)op.getValue());
        } else if(LESS_EQUAL.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.le(root.get(op.getKey()),(Number)op.getValue());
        } else if(GRATE_THEN.equalsIgnoreCase(op.getOper())) {
//            return criteriaBuilder.gt(criteriaBuilder.function("", Integer.class, criteriaBuilder.literal(""), root.get(op.getKey())), (Number)op.getValue());
            return criteriaBuilder.gt(root.get(op.getKey()),(Number)op.getValue());
        } else if(LESS_THEN.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.lt(root.get(op.getKey()),(Number)op.getValue());
        } else if(LIKE.equalsIgnoreCase(op.getOper()) || LIKE_BEGIN_END.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.like(root.get(op.getKey()),"%"+op.getValue()+"%");
        } else if(LIKE_BEGIN.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.like(root.get(op.getKey()),op.getValue()+"%");
        } else if(LIKE_END.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.like(root.get(op.getKey()),"%"+op.getValue());
        } else if(IS_NULL.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.isNull(root.get(op.getKey()));
        } else if(NOT_NULL.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.isNotNull(root.get(op.getKey()));
        } else if(NOT_EQUAL.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.notEqual(root.get(op.getKey()),op.getValue());
        } else if(NOT_LIKE.equalsIgnoreCase(op.getOper()) || NOT_LIKE_BEGIN_END.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.notLike(root.get(op.getKey()), "%"+op.getValue()+"%");
        } else if(NOT_LIKE_BEGIN.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.notLike(root.get(op.getKey()), op.getValue()+"%");
        } else if(NOT_LIKE_END.equalsIgnoreCase(op.getOper())) {
            return criteriaBuilder.notLike(root.get(op.getKey()), "%"+op.getValue());
        } else if(IN.equalsIgnoreCase(op.getOper())) {
//            System.out.println("--------------");
            //如果是IN，则遍历value
            CriteriaBuilder.In in = criteriaBuilder.in(root.get(op.getKey()));
            String [] array = op.getValue().toString().split(",");
            for(String str:array) {
                in = in.value(str.trim());
            }
            return in;
//            return criteriaBuilder.in(root.get(op.getKey())).value(op.getValue());
        }
        return null;
    }
}
