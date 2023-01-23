package com.epam.esm.user.sevice;

import com.epam.esm.user.entity.User;
import com.epam.esm.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
//    private final OrderService orderService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository
//                           ,OrderService orderService
    ) {
        this.userRepository = userRepository;
//        this.orderService = orderService;
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public List<User> getAllUser(String page, String size) {
        if (page != null && size != null) {
            return userRepository.findAll(PageRequest.of(Integer.parseInt(page), Integer.parseInt(size))).toList();
        } else return userRepository.findAll();
    }

//    @Override
//    public List<Order> getUserOrders(Long id) {
//        return orderService.getAllOrderByUserId(id);
//    }

    @Override
    public User createUser(User user) {
        return userRepository.saveAndFlush(user);
    }
}
