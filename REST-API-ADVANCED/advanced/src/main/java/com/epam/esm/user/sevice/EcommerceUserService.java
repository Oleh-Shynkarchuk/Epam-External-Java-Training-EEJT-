package com.epam.esm.user.sevice;

import com.epam.esm.user.entity.User;
import com.epam.esm.user.repository.EcommerceUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EcommerceUserService {
    private final EcommerceUserRepo ecommerceUserRepo;

    public User getUserById(String id) {
        return ecommerceUserRepo.getUserById(id);
    }


    public List<User> getAllUsers(Pageable pageable) {
        return ecommerceUserRepo.getAllUsers(pageable);
    }

    public User createUser(User user) {
        return ecommerceUserRepo.createUser(user);
    }
}
