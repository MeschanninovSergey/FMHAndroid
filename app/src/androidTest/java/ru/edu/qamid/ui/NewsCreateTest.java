package ru.edu.qamid.ui;

import static androidx.test.espresso.Espresso.pressBack;

import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
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
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.edu.qamid.ui.base.BaseUiTest;
import ru.edu.qamid.ui.screens.NewsEditScreen;
import ru.edu.qamid.ui.screens.NewsListScreen;
import ru.edu.qamid.ui.screens.NewsScreen;
import ru.edu.qamid.ui.utils.AllureStepHelper;
import ru.edu.qamid.ui.utils.NewsCategoryRandomizer;
import ru.edu.qamid.ui.utils.TestDataGenerator;

@LargeTest
@Epic("Управление новостями")
@Feature("Создание новостей")
@Owner("Мещанинов Сергей")
public class NewsCreateTest extends BaseUiTest {

    private NewsScreen newsScreen;
    private final List<String> createdNewsTitles = new ArrayList<>();

    @Before
    public void setUp() {
        if (isOnAuthScreen()) {
            newsScreen = loginDirectly();
        } else {
            newsScreen = new NewsScreen();
        }
    }

    @After
    public void tearDown() {
        for (String title : createdNewsTitles) {
            AllureStepHelper.step("Очистка: удаление новости «" + title + "»", () -> {
                try {
                    newsScreen.openNewsManagement()
                            .openEditMode()
                            .deleteItemByTitle(title)
                            .confirmDeleteDialog();
                } catch (Exception e) {
                    System.out.println("Не удалось удалить новость «" + title + "»: " + e.getMessage());
                }
            });
        }
        createdNewsTitles.clear();
    }

    @Test
    @DisplayName("Создание новости и проверка в списке")
    @Description("Успешное создание новости")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Позитивные сценарии")
    public void shouldCreateNewsAndVerifyItInList() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title1);

        AllureStepHelper.step("Открытие формы создания новости", () -> {
            NewsEditScreen editScreen = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews();

            AllureStepHelper.step("Заполнение и сохранение новости", () -> {
                NewsListScreen newsList = editScreen
                        .selectCategory(randomCategory)
                        .enterTitle(title1)
                        .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                        .confirmTime()
                        .enterDescription(description)
                        .saveNews();

                AllureStepHelper.step("Проверка новости в списке", () -> {
                    newsList.pullToRefresh();
                    newsList.scrollUntilNewsItemFound(title1);
                    newsList.assertNewsItemVisible(title1);
                });
            });
        });
    }

    @Test
    @DisplayName("Создание новости, проверяем что после операции выход/вход новость отображается")
    @Description("Проверка созданной новости после выхода из приложения и новой авторизации")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Позитивные сценарии")
    public void shouldNewsPersistAfterRelogin() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title1);

        AllureStepHelper.step("Создание новости", () -> {
            NewsListScreen newsList = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews()
                    .selectCategory(randomCategory)
                    .enterTitle(title1)
                    .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                    .confirmTime()
                    .enterDescription(description)
                    .saveNews();

            AllureStepHelper.step("Проверка новости в списке до релогина", () -> {
                newsList.pullToRefresh()
                        .scrollToNewsItem(title1)
                        .assertNewsItemVisible(title1);
            });
        });

        AllureStepHelper.step("Выход и повторная авторизация", () -> {
            pressBack();
            newsScreen.logout();                 // <-- просто вызываем, без присваивания
            newsScreen = loginDirectly();        // <-- после выхода логинимся заново
        });

        AllureStepHelper.step("Проверка новости после релогина", () -> {
            newsScreen.openNewsManagement()
                    .openEditMode()
                    .scrollUntilNewsItemFound(title1)
                    .assertNewsItemVisible(title1);
        });
    }

    @Test
    @DisplayName("Создание новости, проверяем новость со специальными символами отображается")
    @Description("Создание новости со специальными символами")
    @Severity(SeverityLevel.MINOR)
    @Story("Позитивные сценарии")
    public void shouldCreateNewsWithSpecialChars() {
        LocalDate tomorrow = LocalDate.now().plusDays(2);

        String title = "Тест \"кавычки\" & <script>alert(1)</script> — символы";
        String description = "Test_{}[]@#$%^&* " + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title);

        AllureStepHelper.step("Открытие формы создания новости", () -> {
            NewsListScreen newsList = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews()
                    .selectCategory(randomCategory)
                    .enterTitle(title)
                    .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                    .confirmTime()
                    .enterDescription(description)
                    .saveNews();

            AllureStepHelper.step("Проверка новости со спецсимволами в списке", () -> {
                newsList.pullToRefresh();
                newsList.scrollUntilNewsItemFound(title);
                newsList.assertNewsItemVisible(title);
            });
        });
    }
}