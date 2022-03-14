package jpabook.jpashop.domain;

import org.apache.tomcat.jni.Address;

public class Delivery {

    private Long id;

    private Order order;

    private Address address;

    private DeliveryStatus status;
}
