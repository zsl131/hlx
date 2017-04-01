package com.zslin.basic.repository;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/8 9:16.
 */
public class SimpleSpecificationBuilder<T> {

    private List<SpecificationOperator> opers;

    public SimpleSpecificationBuilder(String key, String oper, Object value) {
        SpecificationOperator so = new SpecificationOperator(key, oper, value, "and");
        opers = new ArrayList<>();
        opers.add(so);
    }

    public SimpleSpecificationBuilder() {
        opers = new ArrayList<>();
    }

    public SimpleSpecificationBuilder add(String key, String oper, Object value, String join) {
        SpecificationOperator so = new SpecificationOperator(key, oper, value, join);
        opers.add(so);
        return this;
    }

    public SimpleSpecificationBuilder add(SpecificationOperator... ops) {
        if(ops==null || ops.length<=0) {return this;}
        for(SpecificationOperator so : ops) {
            opers.add(so);
        }
        return this;
    }

    public SimpleSpecificationBuilder addOr(String key, String oper, Object value) {
        return this.add(key, oper, value, "or");
    }

    public SimpleSpecificationBuilder add(String key, String oper, Object value) {
        return this.add(key, oper, value, "and");
    }

    public Specification generate() {
        Specification<T> specification = new SimpleSpecification<>(opers);
        return specification;
    }
}
