package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.utils.GiftCertificatesRequestParamHandle;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.repo.GiftCertificatesRepository;
import com.epam.esm.integration.errorhandle.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftCertificatesServiceImpl implements GiftCertificatesService {

    private final GiftCertificatesRepository giftCertificatesRepository;

    @Autowired
    public GiftCertificatesServiceImpl(GiftCertificatesRepository giftCertificatesRepository) {
        this.giftCertificatesRepository = giftCertificatesRepository;
    }

    @Override
    public List<GiftCertificate> read(String tagName, String name, String description, String sortByDate, String sortByName) {
        Validate.findGiftByFieldsValidation(sortByName, sortByDate, tagName, name, description, sortByDate, sortByName);
        return giftCertificatesRepository.getGiftCertificateByParam(
                GiftCertificatesRequestParamHandle.createSQLQueryFromReqParam(tagName, name, description, sortByDate, sortByName),
                GiftCertificatesRequestParamHandle.getParamList(tagName, name, description)).orElseThrow();
    }

    @Override
    public List<GiftCertificate> read() {
        return giftCertificatesRepository.getAllGiftCertificates();
    }

    @Override
    public GiftCertificate create(GiftCertificate createGiftCertificate) {
        Validate.FieldNameOfCertificateMustBeUnique(createGiftCertificate.getName(), giftCertificatesRepository.getAllGiftCertificates());
        return giftCertificatesRepository.createNewGiftCertificate(createGiftCertificate).orElseThrow();
    }

    @Override
    public boolean delete(Long id) {
        read(id);
        return giftCertificatesRepository.deleteGiftCertificateById(id);
    }


    @Override
    public GiftCertificate read(Long id) {
        Validate.positiveRequestedId(id);
        return giftCertificatesRepository.getGiftCertificateById(id).orElseThrow();
    }

    @Override
    public GiftCertificate update(Long id, GiftCertificate updateGiftCertificate) {
        List<GiftCertificate> allGiftCertificates = giftCertificatesRepository.getAllGiftCertificates();
        Validate.GiftCertificateOnUpdate(id, updateGiftCertificate.getName(), allGiftCertificates);
        return giftCertificatesRepository.updateGiftCertificateById(id,
                GiftCertificatesRequestParamHandle.complementCertificateOnUpdateByCertificateFromDB(updateGiftCertificate,
                        allGiftCertificates.stream().filter(g -> g.getId().equals(id)).findFirst().orElseThrow())).orElseThrow();
    }
}
