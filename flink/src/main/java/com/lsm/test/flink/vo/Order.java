package com.lsm.test.flink.vo;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Table;
import lombok.Data;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/20 22:00
 **/


@Data
@Table(keyspace = "my_keyspace", name = "order_table")
public class Order {
    @Column(name = "order_id")
    private long orderId;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "user_name")
    private String userName;

    private Long userId;

    @Column(name = "amount")
    private double amount;

}
