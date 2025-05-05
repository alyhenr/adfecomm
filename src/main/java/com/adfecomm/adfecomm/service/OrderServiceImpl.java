package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.exceptions.APIException;
import com.adfecomm.adfecomm.exceptions.ResourceNotFoundException;
import com.adfecomm.adfecomm.model.Order;
import com.adfecomm.adfecomm.model.Payment;
import com.adfecomm.adfecomm.model.Product;
import com.adfecomm.adfecomm.payload.OrderDTO;
import com.adfecomm.adfecomm.payload.OrderItemDTO;
import com.adfecomm.adfecomm.repository.OrderRepository;
import com.adfecomm.adfecomm.repository.PaymentRepository;
import com.adfecomm.adfecomm.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderDTO placeOrder(OrderDTO orderDTO, String paymentMethod) {
        for (OrderItemDTO orderItemDTO: orderDTO.getOrderItems()) {
            Product product = productRepository.findById(orderItemDTO.getProduct().getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            orderItemDTO.getProduct().getProductId(),
                            "Product",
                            "productId"
                    ));

            if (product.getQuantity() < orderItemDTO.getQuantity())
                throw  new APIException("Asked quantity (" + orderItemDTO.getQuantity() + ") for product: "
                + product.getProductName() + " exceeds available quantity (" + product.getQuantity() + ")");

            productService.updateProductQuantity(product.getProductId(), product.getQuantity() - orderItemDTO.getQuantity());
        }

        Order newOrder = orderRepository.save(
                modelMapper.map(orderDTO, Order.class)
        );

        paymentService.managePayment(paymentMethod, newOrder.getOrderId());
        return modelMapper.map(newOrder, OrderDTO.class);
    }
}
