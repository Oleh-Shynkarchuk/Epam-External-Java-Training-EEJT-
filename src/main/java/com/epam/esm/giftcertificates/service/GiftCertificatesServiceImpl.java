package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.filter.Chain;
import com.epam.esm.giftcertificates.utils.GiftCertificatesComplement;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.repo.GiftCertificatesRepository;
import com.epam.esm.integration.errorhandle.ItemNotFound;
import com.epam.esm.integration.errorhandle.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class GiftCertificatesServiceImpl implements GiftCertificatesService {

    private final GiftCertificatesRepository giftCertificatesRepository;

    @Autowired
    public GiftCertificatesServiceImpl(GiftCertificatesRepository giftCertificatesRepository) {
        this.giftCertificatesRepository = giftCertificatesRepository;
    }

    @Override
    public List<GiftCertificate> readGiftCertificate(Map<String, String> requestmap) {
        Validate.findGiftByFieldsValidation(
                requestmap.get("sort_date"),requestmap.get("sort_name"),
                requestmap.get("tag_name"),requestmap.get("gift_name"),
                requestmap.get("description"));
        Chain chain=new Chain(requestmap);
       return giftCertificatesRepository.getGiftCertificateByParam(chain.buildQuery(),chain.buildParamList())
               .orElseThrow(()->{throw new ItemNotFound("No results found for your request." +
                       " Try again with different request param",40402,HttpStatus.NOT_FOUND);
       });
    }

    @Override
    public List<GiftCertificate> readAllGiftCertificates() {
        return giftCertificatesRepository.getAllGiftCertificates().orElseThrow();
    }

    @Override
    public GiftCertificate createGiftCertificate(GiftCertificate createGiftCertificate) {
        Validate.FieldNameOfCertificateMustBeUnique(createGiftCertificate.getName(), giftCertificatesRepository.getAllGiftCertificates().orElseThrow());
        return giftCertificatesRepository.createNewGiftCertificate(createGiftCertificate).orElseThrow();
    }

    @Override
    public void deleteGiftCertificate(Long id) {
        readGiftCertificate(id);
        if (!giftCertificatesRepository.deleteGiftCertificateById(id)) {
          throw new NoSuchElementException();
        }
    }


    @Override
    public GiftCertificate readGiftCertificate(Long id) {
        Validate.positiveRequestedId(id);
        return giftCertificatesRepository.getGiftCertificateById(id).orElseThrow();
    }

    @Override
    public GiftCertificate updateGiftCertificate(Long id, GiftCertificate updateGiftCertificate) {
        Validate.positiveRequestedId(id);
        List<GiftCertificate> allGiftCertificates = giftCertificatesRepository.getAllGiftCertificates().orElseThrow();
        Validate.GiftCertificateOnUpdate(id, updateGiftCertificate.getName(), allGiftCertificates);
        return giftCertificatesRepository.updateGiftCertificateById(id,
                GiftCertificatesComplement.complementCertificateOnUpdateByCertificateFromDB(updateGiftCertificate,
                        allGiftCertificates.stream().filter(g -> g.getId().equals(id)).findFirst().orElseThrow())).orElseThrow();
    }
}
