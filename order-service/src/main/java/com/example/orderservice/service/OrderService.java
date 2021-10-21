package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Iterable<OrderEntity> getAllOrders(){
        return orderRepository.findAll();
    }

    public OrderDto getOrdersByOrderId(String orderId){
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        OrderDto orderDto = new ModelMapper().map(orderEntity,OrderDto.class);
        return orderDto;
    }

    public Iterable<OrderEntity> getOrdersByUserId(String userId){
        return  orderRepository.findByUserId(userId);
    }

    public OrderDto createOrder(OrderDto orderDto){
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderEntity orderEntity = mapper.map(orderDto,OrderEntity.class);
        orderRepository.save(orderEntity);

        return mapper.map(orderEntity,OrderDto.class);
    }
}
