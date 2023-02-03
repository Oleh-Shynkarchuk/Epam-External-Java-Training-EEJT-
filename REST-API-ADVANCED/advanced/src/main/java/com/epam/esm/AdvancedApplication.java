package com.epam.esm;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AdvancedApplication
        implements CommandLineRunner {

//    private final CertificateRepository certificateRepository;
//    private final TagRepository tagRepository;
//    private final UserRepository userRepository;
//    private final OrderRepository orderRepository;
//
//    public AdvancedApplication(CertificateRepository certificateRepository,
//                               TagRepository tagRepository,
//                               UserRepository userRepository,
//                               OrderRepository orderRepository) {
//        this.certificateRepository = certificateRepository;
//        this.tagRepository = tagRepository;
//        this.userRepository = userRepository;
//        this.orderRepository = orderRepository;
//    }

    public static void main(String[] args) {
        SpringApplication.run(AdvancedApplication.class, args);
    }

    @Override
    public void run(String... args) {
//        List<Certificate> certificates = new ArrayList<>(10000);
//        List<User> users = new ArrayList<>(1000);
//        List<Tag> tags = new ArrayList<>(1000);
//        List<Order> orders = new ArrayList<>(1000);
//        Faker faker = new Faker();
//        Random random = new Random();
//        for (int i = 0; i < 750; i++) {
//            User newUser = User.builder().email(faker.name().firstName() + "@gmail.com").build();
//            while (userRepository.existsByEmail(newUser.getEmail())) {
//                newUser = User.builder().email(faker.name().firstName() + "@gmail.com").build();
//            }
//            Tag newTag = Tag.builder().name(faker.funnyName().name()).build();
//            while (tagRepository.existsByName(newTag.getName())) {
//                newTag = Tag.builder().name(faker.funnyName().name()).build();
//            }
//            users.add(userRepository.saveAndFlush(newUser));
//            tags.add(tagRepository.saveAndFlush(newTag));
//        }
//        for (int i = 0; i < 10000; i++) {
//            Certificate newCertificate = Certificate.builder()
//                    .name(faker.name().title())
//                    .description(faker.ancient().god())
//                    .price(new BigDecimal(BigInteger.valueOf(random.nextInt(100001)), 2))
//                    .durationOfDays(String.valueOf(random.nextInt(365) + 1))
//                    .createDate(LocalDateTime.now())
//                    .lastUpdateDate(LocalDateTime.now())
//                    .tags(generateTags(tags, random))
//                    .build();
//            while (certificateRepository.existsByName(newCertificate.getName())) {
//                newCertificate = Certificate.builder()
//                        .name(faker.name().title())
//                        .description(faker.ancient().god())
//                        .price(new BigDecimal(BigInteger.valueOf(random.nextInt(100001)), 2))
//                        .durationOfDays(String.valueOf(random.nextInt(365) + 1))
//                        .createDate(LocalDateTime.now())
//                        .lastUpdateDate(LocalDateTime.now())
//                        .tags(generateTags(tags, random))
//                        .build();
//            }
//            certificates.add(certificateRepository.saveAndFlush(newCertificate));
//        }
//        for (int i = 0; i < 100000; i++) {
//            Set<Certificate> tempList = new HashSet<>();
//            for (int j = 0; j < random.nextInt(10) + 1; j++) {
//                tempList.add(certificates.get(random.nextInt(certificates.size())));
//            }
//            Order order = Order.builder()
//                    .certificates(tempList.stream().toList())
//                    .user(users.get(random.nextInt(users.size())))
//                    .totalPrice(getTotalPrice(tempList.stream().toList()))
//                    .purchaseDate(LocalDateTime.now()).build();
//            orders.add(order);
//        }
//        orderRepository.saveAllAndFlush(orders);
//    }
//
//    private List<Tag> generateTags(List<Tag> tags, Random random) {
//        Set<Integer> tempList = new HashSet<>();
//        List<Tag> list = new ArrayList<>();
//        int size = random.nextInt(10);
//        while (tempList.size() <= size) {
//            tempList.add(random.nextInt(tags.size()));
//        }
//        for (Integer id : tempList) {
//            list.add(tags.get(id));
//        }
//        return list;
//    }
//
//    private BigDecimal getTotalPrice(List<Certificate> tempList) {
//        BigDecimal totalPrice = new BigDecimal(0);
//        for (Certificate certificate : tempList) {
//            totalPrice = totalPrice.add(certificate.getPrice());
//        }
//        return totalPrice;
    }
}