package com.adfecomm.adfecomm.service;

import com.adfecomm.adfecomm.exceptions.APIException;
import com.adfecomm.adfecomm.exceptions.ResourceNotFoundException;
import com.adfecomm.adfecomm.model.*;
import com.adfecomm.adfecomm.payload.OrderDTO;
import com.adfecomm.adfecomm.payload.OrderRequest;
import com.adfecomm.adfecomm.payload.PaymentDTO;
import com.adfecomm.adfecomm.repository.*;
import com.adfecomm.adfecomm.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductService productService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    CartService cartService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthUtil authUtil;

    @Transactional
    @Override
    public OrderDTO placeOrder(String paymentMethod, OrderRequest orderRequest) {
        Long addressId = orderRequest.getAddressId();
        User user = authUtil.loggedInUser();
        Cart cart  = cartRepository.findCartByEmail(user.getEmail());

        if (Objects.isNull(cart)) throw new ResourceNotFoundException("User cart is empty");

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(addressId, "Address", "addressId"));

        //Create Order
        Order order = new Order();
        order.setEmail(user.getEmail());
        order.setTotalPrice(cart.getTotalPrice());
        order.setOrderStatus(orderRequest.getPgStatus());
        order.setOrderDate(LocalDate.now());
        order.setAddress(address);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem: cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            Product product = productRepository.findById(cartItem.getProduct().getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            cartItem.getProduct().getProductId(),
                            "Product is out of stock (deleted)",
                            "productId"
                    ));

            if (product.getQuantity() < cartItem.getQuantity())
                throw  new APIException("Asked quantity (" + cartItem.getQuantity() + ") for product: "
                        + product.getProductName() + " exceeds available quantity ("
                        + product.getQuantity() + ")");

            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrderedProductPrice(cartItem.getProduct().getPrice());
            orderItem.setDiscount(cartItem.getProduct().getDiscount());
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setOrder(order);

            productService.updateProductQuantity(product.getProductId(), product.getQuantity() - cartItem.getQuantity());
            orderItems.add(orderItem);
        }

        PaymentDTO paymentDTO = new PaymentDTO(
                paymentMethod,
                orderRequest.getPgPaymentId(),
                orderRequest.getPgStatus(),
                orderRequest.getPgResponseMessage());

        Order newOrder = orderRepository.save(order);
        Payment payment = paymentService.createPayment(paymentMethod, order, paymentDTO);

        newOrder.setPayment(payment);
        payment.setOrder(newOrder);

        List<OrderItem> orderItemList = orderItemRepository.saveAll(orderItems);
        newOrder.setOrderItems(orderItemList);
        orderRepository.save(newOrder);
        paymentRepository.save(payment);

        cartService.clearUserCart();

        return modelMapper.map(newOrder, OrderDTO.class);
    }

    @Override
    public OrderDTO confirmOrderPayment(String pgPaymentId, String pgResponseMessage) {
        Order order = orderRepository.findByPaymentPGPaymentId(pgPaymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        Payment payment = paymentRepository.findById(order.getPayment().getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not registered"));

        payment.setPgStatus(OrderStatus.PAYED);
        payment.setPgResponseMessage(pgResponseMessage);
        order.setOrderStatus(OrderStatus.PAYED);

        paymentRepository.save(payment);
        orderRepository.save(order);

        return modelMapper.map(order, OrderDTO.class);
    }
}
