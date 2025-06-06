package com.phamquangminh.example5.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.phamquangminh.example5.entity.Cart;
import com.phamquangminh.example5.entity.CartItem;
import com.phamquangminh.example5.entity.Order;
import com.phamquangminh.example5.entity.OrderItem;
import com.phamquangminh.example5.entity.Payment;
import com.phamquangminh.example5.entity.PaymentStatus;
import com.phamquangminh.example5.entity.Product;
import com.phamquangminh.example5.exceptions.APIException;
import com.phamquangminh.example5.exceptions.ResourceNotFoundException;
import com.phamquangminh.example5.payloads.OrderDTO;
import com.phamquangminh.example5.payloads.OrderItemDTO;
import com.phamquangminh.example5.payloads.OrderResponse;
import com.phamquangminh.example5.repository.CartItemRepo;
import com.phamquangminh.example5.repository.CartRepo;
import com.phamquangminh.example5.repository.OrderItemRepo;
import com.phamquangminh.example5.repository.OrderRepo;
import com.phamquangminh.example5.repository.PaymentRepository;
import com.phamquangminh.example5.repository.UserRepo;
import com.phamquangminh.example5.service.CartService;
import com.phamquangminh.example5.service.OrderService;
import com.phamquangminh.example5.service.UserService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrderDTO placeOrder(String emailId, Long cartId, String paymentMethod) {
        Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted!");
        order.setPaymentStatus(PaymentStatus.PENDING_PAYMENT); // Set trạng thái thanh toán
        order.setCreatedAt(LocalDateTime.now()); // Set thời gian tạo đơn hàng

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);
        paymentRepo.save(payment);

        order.setPayment(payment);
        Order savedOrder = orderRepo.save(order);

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new APIException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItemRepo.saveAll(orderItems);

        cartItems.forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());
            product.setQuantity(product.getQuantity() - quantity);
        });

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

        return orderDTO;
    }

    @Override
    public List<OrderDTO> getOrdersByUser(String emailId) {
        List<Order> orders = orderRepo.findAllByEmail(emailId);
        List<OrderDTO> orderDTOs = orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());

        if (orderDTOs.isEmpty()) {
            throw new APIException("No orders placed yet by the user with email: " + emailId);
        }

        return orderDTOs;
    }

    @Override
    public OrderDTO getOrder(String emailId, Long orderId) {
        Order order = orderRepo.findOrderByEmailAndOrderId(emailId, orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order", "orderId", orderId);
        }

        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Order> pageOrders = orderRepo.findAll(pageDetails);
        List<Order> orders = pageOrders.getContent();

        List<OrderDTO> orderDTOs = orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());

        if (orderDTOs.isEmpty()) {
            throw new APIException("No orders placed yet by the users");
        }

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(orderDTOs);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setLastPage(pageOrders.isLast());

        return orderResponse;
    }

    @Override
    public OrderDTO updateOrder(String emailId, Long orderId, String orderStatus) {
        Order order = orderRepo.findOrderByEmailAndOrderId(emailId, orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order", "orderId", orderId);
        }

        order.setOrderStatus(orderStatus);
        return modelMapper.map(order, OrderDTO.class);
    }
}
