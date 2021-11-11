package org.tretton63.wstest.interfaces.rest;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.tretton63.wstest.dto.OrderDto;
import org.tretton63.wstest.entity.OrderItem;
import org.tretton63.wstest.repository.OrderItemRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class OrderItemController {
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;


    private static final Logger logger = LoggerFactory.getLogger(OrderItemController.class);

    public OrderItemController(OrderItemRepository orderItemRepository, ModelMapper modelMapper) {
        this.orderItemRepository = orderItemRepository;
        this.modelMapper = modelMapper;
    }


    @PostMapping(value = "/orders/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String somethingElse(@RequestBody String reqst) {
        logger.info("saved called {}", reqst);
        OrderItem order = new OrderItem();
        order.setOrderId(UUID.randomUUID());
        order.setOrderNumber("order-" + UUID.randomUUID().toString().substring(0, 10));
        orderItemRepository.save(order);
        return order.getOrderId().toString();
    }

    @GetMapping("/orders/")
    public List<OrderDto> allOrders() {
        logger.info("allOrders called");
        TypeMap<OrderItem, OrderDto> typeMap = modelMapper.typeMap(OrderItem.class, OrderDto.class);
        typeMap.validate();
        typeMap.addMappings(mappings -> {
            mappings.map(
                    OrderItem::getCreatedAt,
                    OrderDto::setOrderCreatedAt);
        });
        return orderItemRepository.findAll()
                .stream()
                .map(orderItem -> modelMapper
                        .map(
                                orderItem,
                                OrderDto.class
                        )
                )
                .collect(Collectors.toList());
    }
}
