package ru.edu.qamid.ui.utils;

import java.util.UUID;

public class TestDataGenerator {

    /**
     * Генерирует уникальное название для новости: "News_<UUID>"
     */
    public static String generateNewsTitle() {
        return "News_" + UUID.randomUUID().toString();
    }

    /**
     * Если нужно короткое название с префиксом и числом (для читаемости в логах)
     */
    public static String generateShortTitle(String prefix) {
        return prefix + "_" + System.currentTimeMillis();
    }
}