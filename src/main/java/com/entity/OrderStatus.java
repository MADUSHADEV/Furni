package com.entity;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "order_status")
public class OrderStatus implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "order_status", length = 45, nullable = false)
    private String order_status;

    public OrderStatus() {
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
