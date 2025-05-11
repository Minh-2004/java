package com.phamquangminh.example5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.phamquangminh.example5.entity.CartItem;
import com.phamquangminh.example5.entity.Product;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci.product FROM CartItem ci WHERE ci.product.productId = ?1")
    Product findProductById(Long productId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartId = ?1 AND ci.product.productId = ?2")
    CartItem findCartItemByProductIdAndCartId(Long cartId, Long productId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.cartId = ?1 AND ci.product.productId = ?2")
    void deleteCartItemByProductIdAndCartId(Long cartId, Long productId);

}
