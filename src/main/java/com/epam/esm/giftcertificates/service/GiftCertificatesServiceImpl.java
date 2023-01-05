package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.filter.Chain;
import com.epam.esm.giftcertificates.utils.GiftCertificatesComplement;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.repo.GiftCertificatesRepository;
import com.epam.esm.integration.errorhandle.InternalDatabaseException;
import com.epam.esm.integration.errorhandle.InvalidRequest;
import com.epam.esm.integration.errorhandle.ItemNotFound;
import com.epam.esm.integration.errorhandle.Validate;
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
        Validate.findGiftByFieldsValidation(
                requestmap.get("sort_date"), requestmap.get("sort_name"),
                requestmap.get("tag_name"), requestmap.get("gift_name"),
                requestmap.get("description"));
        Chain chain = new Chain(requestmap);
        return giftCertificatesRepository.getGiftCertificateByParam(chain.buildQuery(), chain.buildParamList())
                .orElseThrow(getItemNotFoundException("No item was found for the specified parameters."));
    }

    @Override
    public List<GiftCertificate> readAllGiftCertificates() {
        return giftCertificatesRepository.getAllGiftCertificates()
                .orElseThrow(getItemNotFoundException("The certificate table is empty."));
    }

    @Override
    public GiftCertificate createGiftCertificate(GiftCertificate createGiftCertificate) {
        if (giftCertificatesRepository.isGiftCertificateByNameExist(createGiftCertificate.getName())) {
            throw new InvalidRequest("Certificate with that name already exist." +
                    " But that field must be unique, change it and try again.");
        }
        return giftCertificatesRepository.createNewGiftCertificate(createGiftCertificate)
                .orElseThrow(getInternalDbException("Certificate not found after creation," +
                        " see if it was created among all certificates."));
    }

    @Override
    public void deleteGiftCertificate(Long id) {
        readGiftCertificate(id);
        if (!giftCertificatesRepository.deleteGiftCertificateById(id)) {
            throw new InternalDatabaseException("Certificate has not been deleted. Internal database error.");
        }
    }


    @Override
    public GiftCertificate readGiftCertificate(Long id) {
        Validate.positiveRequestedId(id);
        return giftCertificatesRepository.getGiftCertificateById(id)
                .orElseThrow(getItemNotFoundException("Certificate does not exist where ( id = "+id+" )."
                        +" Try again with different request param"));
    }

    @Override
    public GiftCertificate updateGiftCertificate(Long id, GiftCertificate updateGiftCertificate) {
        Validate.GiftCertificateOnUpdate(id, updateGiftCertificate);
        GiftCertificate previousCertificate = readGiftCertificate(id);
        return giftCertificatesRepository.updateGiftCertificateById(id,
                        GiftCertificatesComplement.mergingCertificate(updateGiftCertificate, previousCertificate))
                .orElseThrow(getInternalDbException("Update was completed successfully. " +
                        "But the item was not found after the update"));
    }

    private static Supplier<RuntimeException> getItemNotFoundException(String message) {
        return () -> {
            throw new ItemNotFound(message);
        };
    }
    private static Supplier<RuntimeException> getInternalDbException(String message) {
        return () -> {
            throw new InternalDatabaseException(message);
        };
    }
}
