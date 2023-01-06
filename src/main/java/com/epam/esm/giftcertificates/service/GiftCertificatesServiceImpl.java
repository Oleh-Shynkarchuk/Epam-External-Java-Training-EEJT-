package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.filter.Chain;
import com.epam.esm.giftcertificates.utils.GiftCertificatesComplement;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.repo.GiftCertificatesRepository;
import com.epam.esm.giftcertificates.exception.CertificateNotRepresent;
import com.epam.esm.giftcertificates.exception.CertificateInvalidRequest;
import com.epam.esm.giftcertificates.exception.CertificateNotFound;
import com.epam.esm.giftcertificates.validation.GiftValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class GiftCertificatesServiceImpl implements GiftCertificatesService {

    private final GiftCertificatesRepository giftCertificatesRepository;

    @Autowired
    public GiftCertificatesServiceImpl(GiftCertificatesRepository giftCertificatesRepository) {
        this.giftCertificatesRepository = giftCertificatesRepository;
    }

    @Override
    public List<GiftCertificate> readGiftCertificate(Map<String, String> requestmap) {
        GiftValidate.findGiftByFieldsValidation(
                requestmap.get("sort_date"), requestmap.get("sort_name"),
                requestmap.get("tag_name"), requestmap.get("gift_name"),
                requestmap.get("description"));
        Chain chain = new Chain(requestmap);
        return giftCertificatesRepository.getGiftCertificateByParam(chain.buildQuery(), chain.buildParamList())
                .orElseThrow(getCertificateNotFoundException("No item was found for specified parameters."));
    }

    @Override
    public List<GiftCertificate> readAllGiftCertificates() {
        return giftCertificatesRepository.getAllGiftCertificates()
                .orElseThrow(getCertificateNotFoundException("The certificate table is empty."));
    }

    @Override
    public GiftCertificate createGiftCertificate(GiftCertificate createGiftCertificate) {
        if (giftCertificatesRepository.isGiftCertificateByNameExist(createGiftCertificate.getName())) {
            throw new CertificateInvalidRequest("Certificate with that name already exist." +
                    " But that field must be unique, change it and try again.");
        }
        return giftCertificatesRepository.createNewGiftCertificate(createGiftCertificate)
                .orElseThrow(getCertificateNotRepresentException("Can not represent created certificate."));
    }

    @Override
    public void deleteGiftCertificate(Long id) {
        readGiftCertificate(id);
        if (!giftCertificatesRepository.deleteGiftCertificateById(id)) {
            throw new CertificateNotRepresent("Certificate has not been deleted.");
        }
    }


    @Override
    public GiftCertificate readGiftCertificate(Long id) {
        GiftValidate.positiveRequestedId(id);
        return giftCertificatesRepository.getGiftCertificateById(id)
                .orElseThrow(getCertificateNotFoundException("Certificate not found where ( id = "+id+" )."
                        +" Try again with different request param"));
    }

    @Override
    public GiftCertificate updateGiftCertificate(Long id, GiftCertificate updateGiftCertificate) {
        GiftValidate.GiftCertificateOnUpdate(id, updateGiftCertificate);
        GiftCertificate previousCertificate = readGiftCertificate(id);
        return giftCertificatesRepository.updateGiftCertificateById(id,
                        GiftCertificatesComplement.mergingCertificate(updateGiftCertificate, previousCertificate))
                .orElseThrow(getCertificateNotRepresentException("Can not represent updated item."));
    }

    private static Supplier<RuntimeException> getCertificateNotFoundException(String message) {
        return () -> new CertificateNotFound(message);
    }
    private static Supplier<RuntimeException> getCertificateNotRepresentException(String message) {
        return () -> new CertificateNotRepresent(message);
    }
}
