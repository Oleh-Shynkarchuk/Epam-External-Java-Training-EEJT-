package com.epam.esm.entity;

import java.time.Duration;
import java.time.LocalDateTime;

public record GiftCertificate(Long ID, String name, String description, Double price, Duration duration,
                              LocalDateTime create_date, LocalDateTime last_update_date){

}