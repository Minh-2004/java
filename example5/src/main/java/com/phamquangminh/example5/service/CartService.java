package com.phamquangminh.example5.service;

import java.util.List;
import com.phamquangminh.example5.payloads.CartDTO;
import com.phamquangminh.example5.payloads.CartItemDTO;

public interface CartService {

    CartDTO addProductToCart(Long cartId, Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long cartId);

    CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity);

    void updateProductInCarts(Long cartId, Long productId);

    String deleteProductFromCart(Long cartId, Long productId);

    List<CartItemDTO> getCartItemsByCartId(Long cartId);

}
