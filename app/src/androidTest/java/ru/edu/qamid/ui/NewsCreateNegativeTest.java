package ru.edu.qamid.ui;

import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Feature;
import io.qameta.allure.kotlin.Owner;
import io.qameta.allure.kotlin.Severity;
import io.qameta.allure.kotlin.SeverityLevel;
import io.qameta.allure.kotlin.Step;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.edu.qamid.ui.base.BaseUiTest;
import ru.edu.qamid.ui.screens.NewsEditScreen;
import ru.edu.qamid.ui.screens.NewsScreen;
import ru.edu.qamid.ui.utils.AllureStepHelper;
import ru.edu.qamid.ui.utils.NewsCategoryRandomizer;
import ru.edu.qamid.ui.utils.TestDataGenerator;

@LargeTest
@Epic("Управление новостями")
@Feature("Создание новостей")
@Owner("Мещанинов Сергей")
public class NewsCreateNegativeTest extends BaseUiTest {

    private NewsScreen newsScreen;
    private final List<String> createdNewsTitles = new ArrayList<>();

    @Before
    public void setUp() {
        if (isOnAuthScreen()) {
            // Мы на экране авторизации: логинимся напрямую
            newsScreen = loginDirectly();
        } else {
            // Мы уже в приложении (на главном/другом экране): не логинимся, просто оборачиваем экран
            newsScreen = new NewsScreen();
        }
    }

    @Test
    @Step
    @DisplayName("Создание новости с некорректной категорией")
    @Description("Создание новости, проверяем невозможность указания некорректной категории")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Негативные сценарии")
    public void shouldStayOnCreateScreenWithInvalidCategory() {
        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();

        AllureStepHelper.step("Открытие формы создания новости", () -> {
            NewsEditScreen editScreen = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews();

            AllureStepHelper.step("Заполнение данных с некорректной категорией", () -> {
                editScreen.enterCategoryText("ошибка")
                        .enterTitle(title1)
                        .confirmDateAndTime()
                        .enterDescription(description);
            });

            AllureStepHelper.step("Попытка сохранения и проверка, что экран не закрылся", () -> {
                editScreen.saveNewsExpectingFailure()
                        .assertStillOnCreateScreen();
            });
        });
    }

    @Test
    @DisplayName("Создание новости с пустым полем Категория")
    @Description("Создание новости, проверяем невозможность сохранения новости с пустым полем категория")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Негативные сценарии")
    public void shouldStayOnCreateScreenWithEmptyCategory() {
        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();

        AllureStepHelper.step("Открытие формы создания новости", () -> {
            NewsEditScreen editScreen = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews();

            AllureStepHelper.step("Заполнение данных без указания категории", () -> {
                editScreen.enterTitle(title1)
                        .confirmDateAndTime()
                        .enterDescription(description);
            });

            AllureStepHelper.step("Попытка сохранения и проверка, что экран не закрылся", () -> {
                editScreen.saveNewsExpectingFailure()
                        .assertStillOnCreateScreen();
            });
        });
    }

    @Test
    @DisplayName("Создание новости с пустым полем Заголовок")
    @Description("Создание новости, проверяем невозможность сохранения новости с пустым полем Заголовок")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Негативные сценарии")
    public void shouldStayOnCreateScreenWithEmptyTitle() {
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        AllureStepHelper.step("Открытие формы создания новости", () -> {
            NewsEditScreen editScreen = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews();

            AllureStepHelper.step("Заполнение данных с пустым заголовком", () -> {
                editScreen.selectCategory(randomCategory)
                        .enterTitle("")
                        .confirmDateAndTime()
                        .enterDescription(description);
            });

            AllureStepHelper.step("Попытка сохранения и проверка, что экран не закрылся", () -> {
                editScreen.saveNewsExpectingFailure()
                        .assertStillOnCreateScreen();
            });
        });
    }

    @Test
    @DisplayName("Создание новости с пустым полем Дата публикации")
    @Description("Создание новости, проверяем невозможность сохранения новости с пустым полем Дата публикации")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Негативные сценарии")
    public void shouldStayOnCreateScreenWithEmptyDate() {
        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        AllureStepHelper.step("Открытие формы создания новости", () -> {
            NewsEditScreen editScreen = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews();

            AllureStepHelper.step("Заполнение данных без даты публикации", () -> {
                editScreen.enterCategoryText(randomCategory)
                        .enterTitle(title1)
                        .confirmTime()
                        .enterDescription(description);
            });

            AllureStepHelper.step("Попытка сохранения и проверка, что экран не закрылся", () -> {
                editScreen.saveNewsExpectingFailure()
                        .assertStillOnCreateScreen();
            });
        });
    }

    @Test
    @DisplayName("Создание новости с пустым полем Время публикации")
    @Description("Создание новости, проверяем невозможность сохранения новости с пустым полем Время публикации")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Негативные сценарии")
    public void shouldStayOnCreateScreenWithEmptyTime() {
        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        AllureStepHelper.step("Открытие формы создания новости", () -> {
            NewsEditScreen editScreen = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews();

            AllureStepHelper.step("Заполнение данных без времени публикации", () -> {
                editScreen.enterCategoryText(randomCategory)
                        .enterTitle(title1)
                        .confirmDate()
                        .enterDescription(description);
            });

            AllureStepHelper.step("Попытка сохранения и проверка, что экран не закрылся", () -> {
                editScreen.saveNewsExpectingFailure()
                        .assertStillOnCreateScreen();
            });
        });
    }

    @Test
    @DisplayName("Создание новости с пустым полем Описание")
    @Description("Создание новости, проверяем невозможность сохранения новости с пустым полем Описание")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Негативные сценарии")
    public void shouldStayOnCreateScreenWithEmptyDescription() {
        String title1 = TestDataGenerator.generateNewsTitle();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        AllureStepHelper.step("Открытие формы создания новости", () -> {
            NewsEditScreen editScreen = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews();

            AllureStepHelper.step("Заполнение данных без описания", () -> {
                editScreen.enterCategoryText(randomCategory)
                        .enterTitle(title1)
                        .confirmDateAndTime();
            });

            AllureStepHelper.step("Попытка сохранения и проверка, что экран не закрылся", () -> {
                editScreen.saveNewsExpectingFailure()
                        .assertStillOnCreateScreen();
            });
        });
    }
}
