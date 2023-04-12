package com.epam.esm.user.repository;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.customer_group.CustomerGroup;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.utils.EcommerceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EcommerceUserRepo {

    private final ProjectApiRoot projectApiRoot;
    private final EcommerceUtil ecommerceUtil;

    public User getUserById(String id) {
        List<CustomerGroup> customerGroup = projectApiRoot.customerGroups().get().executeBlocking().getBody().getResults();
        Customer customer = projectApiRoot.customers().withId(id).get().executeBlocking().getBody();
        return ecommerceUtil.transformCustomerToUser(customer, customerGroup);
    }

    public List<User> getAllUsers(Pageable pageable) {
        List<CustomerGroup> customerGroup = projectApiRoot.customerGroups().get().executeBlocking().getBody().getResults();
        return projectApiRoot.customers()
                .get()
                .withLimit(pageable.getPageSize())
                .withOffset(pageable.getOffset())
                .executeBlocking()
                .getBody()
                .getResults()
                .stream()
                .map(customer -> ecommerceUtil.transformCustomerToUser(customer, customerGroup))
                .toList();
    }

    public User createUser(User user) {
        List<CustomerGroup> customerGroup = projectApiRoot.customerGroups().get().executeBlocking().getBody().getResults();
        Customer customer = projectApiRoot.customers().post(ecommerceUtil.transformUserToCustomer(user)).executeBlocking().getBody().getCustomer();
        return ecommerceUtil.transformCustomerToUser(customer, customerGroup);
    }
}
