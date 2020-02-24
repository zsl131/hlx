package com.zslin.test;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="t_test_obj")
@Data
public class TestObj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
}
