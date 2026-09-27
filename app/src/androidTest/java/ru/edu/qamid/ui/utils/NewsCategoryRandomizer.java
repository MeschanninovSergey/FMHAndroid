package ru.edu.qamid.ui.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NewsCategoryRandomizer {

    private static final List<String> CATEGORIES = Arrays.asList(
            "Нужна помощь",
            "Праздник",
            "Массаж",
            "Объявление",
            "Зарплата",
            "День рождения",
            "Профсоюз",
            "Благодарность"
            // Добавь сюда все категории, которые есть в приложении
    );

    private static final Random RANDOM = new Random();

    /**
     * Возвращает случайную категорию из списка.
     */
    public static String getRandomCategory() {
        int index = RANDOM.nextInt(CATEGORIES.size());
        return CATEGORIES.get(index);
    }

    /**
     * (Опционально) Возвращает случайную категорию, отличную от указанной.
     * Полезно, если нужно проверить смену категории.
     */
    public static String getRandomCategoryExcept(String exclude) {
        List<String> filtered = CATEGORIES.stream()
                .filter(c -> !c.equals(exclude))
                .toList();

        if (filtered.isEmpty()) {
            return getRandomCategory(); // fallback, если исключили всё
        }

        int index = RANDOM.nextInt(filtered.size());
        return filtered.get(index);
    }
}