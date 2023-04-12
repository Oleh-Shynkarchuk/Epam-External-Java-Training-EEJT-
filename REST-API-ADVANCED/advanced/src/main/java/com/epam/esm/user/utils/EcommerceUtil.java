package com.epam.esm.user.utils;

import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.customer.CustomerDraft;
import com.commercetools.api.models.customer_group.CustomerGroup;
import com.commercetools.api.models.customer_group.CustomerGroupResourceIdentifier;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.entity.provider.Provider;
import com.epam.esm.user.entity.roles.Role;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EcommerceUtil {
    public User transformCustomerToUser(Customer customer, List<CustomerGroup> customerGroup) {
        return User.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .role(Role.valueOf(customerGroup.stream().filter(c -> c.getId().equals(customer.getCustomerGroup().getId())).findFirst().get().getName()))
                .provider(Provider.ECOMMERCE)
                .build();
    }

    public CustomerDraft transformUserToCustomer(User user) {
        return CustomerDraft.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .customerGroup(CustomerGroupResourceIdentifier.builder()
                        .key("user1key")
                        .build())
                .isEmailVerified(true)
                .build();
    }
}
