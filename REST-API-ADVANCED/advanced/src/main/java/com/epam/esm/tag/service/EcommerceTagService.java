package com.epam.esm.tag.service;

import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.repo.EcommerceTagRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EcommerceTagService implements TagService {

    private final EcommerceTagRepo ecommerceTagRepo;

    @Override
    public List<Tag> getAllTags(Pageable paginationCriteria) {
        return ecommerceTagRepo.getAllTags(paginationCriteria);
    }

    @Override
    public Tag getTagById(String id) {
        return ecommerceTagRepo.getTagById(id);
    }

    @Override
    public Optional<Tag> getTagByName(String tagName) {
        return Optional.empty();
    }

    @Override
    public Tag createTag(Tag newTag) {
        return ecommerceTagRepo.createTag(newTag);
    }

    @Override
    public void deleteTagById(String id) {
        ecommerceTagRepo.deleteTag(id);
    }

    @Override
    public Tag getMostWidelyUsedTag() {
        return ecommerceTagRepo.getMost();
    }
}
