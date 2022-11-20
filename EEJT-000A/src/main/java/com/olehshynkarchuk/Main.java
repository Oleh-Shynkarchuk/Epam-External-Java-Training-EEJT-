package com.olehshynkarchuk;

import com.olehshynkarchuk.task1.TxtProcessing;
import com.olehshynkarchuk.task2.*;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        /* ------- Task1 ---------*/

        TxtProcessing txtProcessing = new TxtProcessing("C:\\data\\Epam-External-Java-Training-EEJT-\\EEJT-000A\\Bigtext.txt");
        for (String s : txtProcessing.getLinesFromArray()) {
            System.out.println(s);
        }

        /* ------- Task2 ---------*/


        boolean work = true;
        while (work) {
            String fileName = null;
            String filenameExtensions = null;
            long sizeStartRange = 0L;
            long sizeEndRange = 0L;
            long dateStartRange = 0L;
            long dateEndRange = 0L;
            int add;
            Scanner scanner = new Scanner(System.in);
            System.out.println("Шукати по імені файлу?(0/1) ");
            add = scanner.nextInt();
            scanner.nextLine();
            switch (add) {
                case 0 -> System.out.println("Пошук за іменем файлу не відбуватиметься.");
                case 1 -> {
                    System.out.println("Назва файлу : ");
                    fileName = scanner.nextLine();
                }
                default -> throw new IllegalStateException("Неочікуване введення даних: " + add);
            }
            System.out.println("Шукати за форматом файла?(0/1) ");
            add = scanner.nextInt();
            scanner.nextLine();
            switch (add) {
                case 0 -> System.out.println("Пошук за форматом не відбуватиметься.");
                case 1 -> {
                    System.out.println("Формат (.docx) крапка обовязкова : ");
                    filenameExtensions = (scanner.nextLine());
                    while (!filenameExtensions.matches("^[^a-zA-Z0-9,]*\\.[a-zA-Z0-9,]*$")) {
                        System.out.println("Невірний формат, необхідно вказати формат з допомогою крапки : ");
                        filenameExtensions = (scanner.nextLine());
                    }
                }
                default -> throw new IllegalStateException("Неочікуване введення даних: " + add);
            }
            System.out.println("Шукати за розміром файла?(0/1) ");
            add = scanner.nextInt();
            scanner.nextLine();
            switch (add) {
                case 0 -> System.out.println("Пошук за розіром файлу не відбуватиметься.");
                case 1 -> {
                    while (sizeStartRange >= sizeEndRange) {
                        System.out.println("Зверніть увагу кінцева межа пошуку за розміром не може бути меньшою за початкову.");
                        System.out.println("Початкова межа пошуку (у байтах) : ");
                        sizeStartRange = (scanner.nextLong());
                        System.out.println("Кінцева межа пошуку (у байтах) : ");
                        sizeEndRange = (scanner.nextLong());
                    }
                }
            }
            System.out.println("Шукати за датою модифікації файла?(0/1) ");
            add = scanner.nextInt();
            scanner.nextLine();
            switch (add) {
                case 0 -> System.out.println("Пошук за датою модифікації файлу не відбуватиметься.");
                case 1 -> {
                    while (dateStartRange >= dateEndRange) {
                        System.out.println("Початкова межа дати повинна бути старшою за кінцеву");
                        System.out.println("Введіть початкову межу дати модифікації (формат yyyy-MM-dd HH:mm:ss) ");
                        dateStartRange = Timestamp.valueOf(scanner.nextLine()).getTime();
                        System.out.println("Введіть кінцеву межу дати модифікації (формат yyyy-MM-dd HH:mm:ss) ");
                        dateEndRange = Timestamp.valueOf(scanner.nextLine()).getTime();
                    }
                }
            }
            Chain chain = new Chain(fileName, filenameExtensions, sizeStartRange, sizeEndRange, dateStartRange, dateEndRange);
            ArrayList<File> fileArrayList = new ArrayList<>();
            FileParametrs fileParametrs = new FileParametrs();
            fileParametrs.setFileList(fileArrayList);
            chain.searchFiles(new File("D:\\steam"), fileParametrs);
            for (File f : fileArrayList) {
                Timestamp t = new Timestamp(f.lastModified());
                System.out.println("\n" + f.getAbsolutePath());
                System.out.println("Останій раз модифікований : " + t);
                System.out.println("Розмір :" + f.length() + " байт");
            }
            System.out.println("Продовжити пошук?(0/1) ");
            add = scanner.nextInt();
            if (add == 0) {
                work = false;
            }
            scanner.nextLine();
        }
    }
}

