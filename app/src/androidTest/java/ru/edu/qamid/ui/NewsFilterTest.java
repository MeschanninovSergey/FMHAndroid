package ru.edu.qamid.ui;

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
import ru.edu.qamid.ui.screens.NewsListScreen;
import ru.edu.qamid.ui.screens.NewsScreen;
import ru.edu.qamid.ui.utils.AllureStepHelper;
import ru.edu.qamid.ui.utils.NewsCategoryRandomizer;
import ru.edu.qamid.ui.utils.TestDataGenerator;

@LargeTest
@Epic("Управление новостями")
@Feature("Фильтрация новостей")
@Owner("Мещанинов Сергей")
public class NewsFilterTest extends BaseUiTest {

    private NewsScreen newsScreen;
    private final List<String> createdNewsTitles = new ArrayList<>();

    @Before
    public void setUp() {
        if (isOnAuthScreen()) {
            newsScreen = loginAsDefaultUser();
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
    @DisplayName("Фильтр по дате: новость из будущего не видна")
    @Description("Фильтр по дате: новость из будущего не видна после фильтра новостей с сегодняшней датой")
    @Severity(SeverityLevel.NORMAL)
    @Story("Фильтр по дате")
    public void shouldNotDisplayFutureDateNewsAfterFilter() {
        LocalDate tomorrow = LocalDate.now().plusDays(7);
        LocalDate today = LocalDate.now();

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title1);

        AllureStepHelper.step("Создание новости с датой из будущего", () -> {
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

            AllureStepHelper.step("Проверка, что новость отображается в списке", () -> {
                newsList.pullToRefresh();
                newsList.assertNewsItemVisible(title1);
            });

            AllureStepHelper.step("Применение фильтра с сегодняшней датой", () -> {
                newsList.openNewsManagement()
                        .openEditMode()
                        .openFilter()
                        .setStartDate(today.getYear(), today.getMonthValue(), today.getDayOfMonth())
                        .setEndDate(today.getYear(), today.getMonthValue(), today.getDayOfMonth())
                        .clickFilterButton();
            });

            AllureStepHelper.step("Проверка, что новость из будущего скрыта фильтром", () -> {
                newsList.assertNewsItemDoesNotExist(title1);
            });
        });
    }

    @Test
    @DisplayName("Фильтр по дате: новость вне диапазона скрыта")
    @Description("Фильтр по дате: новость вне диапазона скрыта")
    @Severity(SeverityLevel.NORMAL)
    @Story("Фильтр по дате")
    public void appFilterToDate() {
        LocalDate tomorrow = LocalDate.now().plusDays(7);
        LocalDate dayAfterTomorrow = LocalDate.now().plusDays(9);

        String title1 = TestDataGenerator.generateNewsTitle();
        String category1 = NewsCategoryRandomizer.getRandomCategory();
        String description = "Test_" + UUID.randomUUID().toString();

        String title2 = TestDataGenerator.generateNewsTitle();
        String category2 = NewsCategoryRandomizer.getRandomCategory();
        String description2 = "Test_" + UUID.randomUUID().toString();

        createdNewsTitles.add(title1);
        createdNewsTitles.add(title2);

        AllureStepHelper.step("Создание первой новости (в пределах диапазона фильтра)", () -> {
            newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews()
                    .selectCategory(category1)
                    .enterTitle(title1)
                    .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                    .confirmTime()
                    .enterDescription(description)
                    .saveNews();
        });

        AllureStepHelper.step("Создание второй новости (вне диапазона фильтра)", () -> {
            NewsListScreen newsList = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews()
                    .selectCategory(category2)
                    .enterTitle(title2)
                    .setAndConfirmDate(dayAfterTomorrow.getYear(), dayAfterTomorrow.getMonthValue(), dayAfterTomorrow.getDayOfMonth())
                    .confirmTime()
                    .enterDescription(description2)
                    .saveNews();

            AllureStepHelper.step("Проверка, что обе новости отображаются", () -> {
                newsList.pullToRefresh()
                        .scrollUntilNewsItemFound(title2)
                        .assertNewsItemVisible(title2);
            });

            AllureStepHelper.step("Применение фильтра по дате", () -> {
                // Исправлено: корректно вычисляем день для endDate
                LocalDate startFilterDate = tomorrow.plusDays(0);
                LocalDate endFilterDate = tomorrow.plusDays(1);
                newsList.openNewsManagement()
                        .openEditMode()
                        .openFilter()
                        .setStartDate(startFilterDate.getYear(), startFilterDate.getMonthValue(), startFilterDate.getDayOfMonth())
                        .setEndDate(endFilterDate.getYear(), endFilterDate.getMonthValue(), endFilterDate.getDayOfMonth())
                        .clickFilterButton();
            });

            AllureStepHelper.step("Проверка: новость вне диапазона скрыта, новость в диапазоне видна", () -> {
                newsList
                        .assertNewsItemDoesNotExist(title2)
                        .scrollUntilNewsItemFound(title1)
                        .assertNewsItemVisible(title1);
            });
        });
    }

    @Test
    @DisplayName("Фильтр по дате: новость в диапазоне фильтра видна")
    @Description("Фильтр по дате: новость в диапазоне фильтра видна")
    @Severity(SeverityLevel.NORMAL)
    @Story("Фильтр по дате")
    public void shouldDisplayNewsWithinFilterRange() {
        LocalDate tomorrow = LocalDate.now().plusDays(8);
        LocalDate startDate = LocalDate.now().plusDays(7);
        LocalDate endDate = LocalDate.now().plusDays(9);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title1);

        AllureStepHelper.step("Создание новости в пределах диапазона фильтра", () -> {
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

            AllureStepHelper.step("Проверка, что новость отображается в списке", () -> {
                newsList.pullToRefresh();
                newsList.scrollToNewsItem(title1)
                        .assertNewsItemVisible(title1);
            });

            AllureStepHelper.step("Применение фильтра с диапазоном, включающим дату новости", () -> {
                newsList.openFilter()
                        .setStartDate(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth())
                        .setEndDate(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth())
                        .clickFilterButton();
            });

            AllureStepHelper.step("Проверка: новость видна после фильтрации", () -> {
                newsList.scrollToNewsItem(title1)
                        .assertNewsItemVisible(title1);
            });
        });
    }

    @Test
    @DisplayName("Фильтр по дате: один день")
    @Description("Фильтр по дате: фильтр с одинаковыми датами начала и конца")
    @Severity(SeverityLevel.NORMAL)
    @Story("Фильтр по дате")
    public void shouldDisplayNewsWithSameStartEndFilterDate() {
        LocalDate tomorrow = LocalDate.now().plusDays(7);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title1);

        AllureStepHelper.step("Создание новости с конкретной датой", () -> {
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

            AllureStepHelper.step("Проверка, что новость отображается в списке", () -> {
                newsList.pullToRefresh();
                newsList.scrollToNewsItem(title1)
                        .assertNewsItemVisible(title1);
            });

            AllureStepHelper.step("Применение фильтра с одинаковыми датами начала и конца", () -> {
                newsList.openFilter()
                        .setStartDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                        .setEndDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                        .clickFilterButton();
            });

            AllureStepHelper.step("Проверка: новость видна", () -> {
                newsList.scrollToNewsItem(title1)
                        .assertNewsItemVisible(title1);
            });
        });
    }
}
