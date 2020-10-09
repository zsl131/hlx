package com.zslin.multi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "m_base_qrcode")
@Data
public class BaseQrcode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String url;
}
