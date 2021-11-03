package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.OrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
@Slf4j
public class OrderController {

    private final OrderService service;
    private final Environment env;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service Port : {%s}",env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder orderDetails){

        log.info("Before add order data");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderdto = mapper.map(orderDetails,OrderDto.class);
        orderdto.setUserId(userId);

          /* jpa */
        OrderDto createdDto = service.createOrder(orderdto);
        ResponseOrder responseOrder = mapper.map(createdDto, ResponseOrder.class);

        /*jpa 대신 kafka*/
        orderdto.setOrderId(UUID.randomUUID().toString());
        orderdto.setTotalPrice(orderDetails.getQty()*orderDetails.getUnitPrice());

        /* send this order to the kafka */
//        kafkaProducer.send("example-catalog-topic",mapper.map(orderDetails,OrderDto.class));
//        orderProducer.send("orders",orderdto);


//        ResponseOrder responseOrder = mapper.map(orderdto,ResponseOrder.class);

        log.info("After add order data");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);

    }

    @GetMapping ("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> createOrder(@PathVariable("userId") String userId) throws Exception{
        log.info("Before retrieve order data");
        Iterable<OrderEntity> orderList = service.getOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();

        try{
            Thread.sleep(1000);
            throw new Exception("장애발생");

        }catch (InterruptedException exception){
            log.error(exception.getMessage());
        }

        orderList.forEach( o -> {
            result.add(new ModelMapper().map(o,ResponseOrder.class));
        });
        log.info("Add retrieved order data");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
