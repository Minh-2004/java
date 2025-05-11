package com.phamquangminh.example5.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.phamquangminh.example5.config.AppConstants;
import com.phamquangminh.example5.entity.Address;
import com.phamquangminh.example5.entity.Cart;
import com.phamquangminh.example5.entity.Role;
import com.phamquangminh.example5.entity.User;
import com.phamquangminh.example5.exceptions.APIException;
import com.phamquangminh.example5.exceptions.ResourceNotFoundException;
import com.phamquangminh.example5.payloads.AddressDTO;
import com.phamquangminh.example5.payloads.CartDTO;
import com.phamquangminh.example5.payloads.ProductDTO;
import com.phamquangminh.example5.payloads.UserDTO;
import com.phamquangminh.example5.payloads.UserResponse;
import com.phamquangminh.example5.repository.AddressRepo;
import com.phamquangminh.example5.repository.RoleRepo;
import com.phamquangminh.example5.repository.UserRepo;
import com.phamquangminh.example5.service.CartService;
import com.phamquangminh.example5.service.UserService;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private CartService cartService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        try {
            // Map UserDTO to User entity
            User user = modelMapper.map(userDTO, User.class);

            // Initialize and link cart to user
            Cart cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);

            // Assign role to user
            Role role = roleRepo.findById(AppConstants.USER_ID)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.getRoles().add(role);

            // Handle address
            AddressDTO addressDTO = userDTO.getAddress();
            String country = addressDTO.getCountry();
            String state = addressDTO.getState();
            String city = addressDTO.getCity();
            String pincode = addressDTO.getPincode();
            String street = addressDTO.getStreet();
            String buildingName = addressDTO.getBuildingName();

            Address address = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(
                    country, state, city, pincode, street, buildingName);

            if (address == null) {
                address = new Address(country, state, city, pincode, street, buildingName);
                address = addressRepo.save(address);
            }

            user.setAddresses(List.of(address));

            // Save user and return response
            User registeredUser = userRepo.save(user);
            cart.setUser(registeredUser);

            // Map registered user back to UserDTO
            UserDTO responseDTO = modelMapper.map(registeredUser, UserDTO.class);
            responseDTO.setAddress(modelMapper.map(
                    registeredUser.getAddresses().stream().findFirst().orElse(null),
                    AddressDTO.class));

            return responseDTO;

        } catch (DataIntegrityViolationException e) {
            throw new APIException("User already exists with emailId: " + userDTO.getEmail());
        }
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        // Map address if exists
        if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
            userDTO.setAddress(modelMapper.map(
                    user.getAddresses().stream().findFirst().orElse(null),
                    AddressDTO.class));
        }

        // Map cart and products if exists
        if (user.getCart() != null) {
            CartDTO cartDTO = modelMapper.map(user.getCart(), CartDTO.class);
            List<ProductDTO> products = user.getCart().getCartItems().stream()
                    .map(item -> modelMapper.map(item.getProduct(), ProductDTO.class))
                    .collect(Collectors.toList());
            cartDTO.setProducts(products);
            userDTO.setCart(cartDTO);
        }

        return userDTO;
    }

    @Override
    public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        // Khởi tạo Pageable với các thông số phân trang
        Pageable pageable = PageRequest.of(pageNumber, pageSize,
                Sort.by(sortOrder.equalsIgnoreCase("asc") ? Sort.Order.asc(sortBy) : Sort.Order.desc(sortBy)));

        // Lấy danh sách người dùng từ repository
        Page<User> users = userRepo.findAll(pageable);

        // Chuyển đổi danh sách User thành UserDTO
        List<UserDTO> userDTOs = users.getContent().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        // Trả về đối tượng UserResponse với các tham số
        return new UserResponse(
                userDTOs, // content
                pageNumber, // pageNumber
                pageSize, // pageSize
                users.getTotalElements(), // totalElements
                users.getTotalPages(), // totalPages
                users.isLast() // lastPage
        );
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        // Update the user's details here
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setMobileNumber(userDTO.getMobileNumber());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // If you want to update password

        // Update address if needed
        if (userDTO.getAddress() != null) {
            AddressDTO addressDTO = userDTO.getAddress();
            Address address = modelMapper.map(addressDTO, Address.class);
            user.getAddresses().clear();
            user.getAddresses().add(address);
        }

        User updatedUser = userRepo.save(user);

        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public String deleteUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        userRepo.delete(user);
        return "User deleted successfully";
    }
}
