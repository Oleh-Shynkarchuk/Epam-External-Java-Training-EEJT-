package com.epam.esm.certificate.graphqlutils;

import com.epam.esm.tag.entity.Tag;

import java.util.List;

public record GraphqlTagResponse(Categories categories) {
    public record Categories(List<Tag> results) {
    }
}
