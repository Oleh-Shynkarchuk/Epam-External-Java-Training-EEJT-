package com.epam.esm.tag.hateoas;

import com.epam.esm.tag.controller.TagController;
import com.epam.esm.tag.entity.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagHateoasSupport {
    public Tag addHateoasSupportToSingleTag(Tag t) {
        return t.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .getTagById(String.valueOf(t.getId()))).withRel("getTag"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .getMostWidelyUsedTag()).withRel("getMostWidelyUsedTag"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .getAllTag(Pageable.unpaged())).withRel("getAllTag"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .createTag(Tag.builder().build())).withRel("createNewTag"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .deleteTag(String.valueOf(t.getId()))).withRel("deleteTag")
        );
    }

    public CollectionModel<Tag> addHateoasSupportToAllTag(List<Tag> allTag, @ParameterObject Pageable paginationCriteria) {
        allTag.forEach(this::addHateoasSupportToTag);
        return CollectionModel.of(allTag).add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .getAllTag(paginationCriteria)).withRel("getAllTag"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .getMostWidelyUsedTag()).withRel("getMostWidelyUsedTag"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .createTag(Tag.builder().build())).withRel("createNewTag")
        );
    }

    public void addHateoasSupportToTag(Tag t) {
        if (t.getLinks().isEmpty()) {
            t.add(
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                            .getTagById(String.valueOf(t.getId()))).withRel("getTag"),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                            .deleteTag(String.valueOf(t.getId()))).withRel("deleteTag")
            );
        }
    }
}
