package com.phamquangminh.example5.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.phamquangminh.example5.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    /**
     * Tìm danh sách người dùng dựa trên địa chỉ ID.
     *
     * @param addressId ID của địa chỉ cần tìm
     * @return Danh sách người dùng liên quan tới địa chỉ
     */
    @Query("SELECT u FROM User u JOIN FETCH u.addresses a WHERE a.addressId = ?1")
    List<User> findByAddress(Long addressId);

    /**
     * Tìm người dùng dựa trên email.
     *
     * @param email Email của người dùng
     * @return Optional chứa đối tượng User nếu tồn tại
     */
    Optional<User> findByEmail(String email);
}
