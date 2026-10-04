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
@Feature("Сортировка новостей")
@Owner("Мещанинов Сергей")
public class NewsSortTest extends BaseUiTest {

    private NewsScreen newsScreen;

    private NewsListScreen filteredNewsList;
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
    @DisplayName("Сортировка новостей: переключение порядка сортировки")
    @Description("Проверка переключения сортировки новостей по дате (убывание/возрастание) при наличии двух новостей в фильтре")
    @Severity(SeverityLevel.NORMAL)
    @Story("Сортировка по дате")
    public void shouldToggleNewsSortByDate() {
        LocalDate date1 = LocalDate.now().plusDays(8);
        LocalDate date2 = LocalDate.now().plusDays(9);
        LocalDate startDate = LocalDate.now().plusDays(8);
        LocalDate endDate = LocalDate.now().plusDays(9);

        String title1 = TestDataGenerator.generateNewsTitle();
        String title2 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String description2 = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();
        String randomCategory2 = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title1);
        createdNewsTitles.add(title2);

        AllureStepHelper.step("Создание первой новости (дата +8 дней)", () -> {
            newsScreen.openNewsManagement()
                    .openEditMode()
                    .clickAddNews()
                    .selectCategory(randomCategory)
                    .enterTitle(title1)
                    .setAndConfirmDate(date1.getYear(), date1.getMonthValue(), date1.getDayOfMonth())
                    .setAndConfirmTime(23, 59)
                    .enterDescription(description)
                    .saveNews()
                    .pullToRefresh();
        });

        AllureStepHelper.step("Создание второй новости (дата +9 дней)", () -> {
            newsScreen.openNewsManagement()
                    .openEditMode()
                    .clickAddNews()
                    .selectCategory(randomCategory2)
                    .enterTitle(title2)
                    .setAndConfirmDate(date2.getYear(), date2.getMonthValue(), date2.getDayOfMonth())
                    .setAndConfirmTime(0, 1)
                    .enterDescription(description2)
                    .saveNews();
        });

        // ВАЖНО: здесь мы один раз получаем экран и запоминаем его в переменную
        AllureStepHelper.step("Применение фильтра по диапазону дат", () -> {
            NewsListScreen newsList = newsScreen.openNewsManagement();
            newsList.openEditMode()
                    .openFilter()
                    .setStartDate(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth())
                    .setEndDate(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth())
                    .clickFilterButton();

            // Сохраняем ссылку на этот экран, чтобы использовать дальше
            this.filteredNewsList = newsList;
        });

        AllureStepHelper.step("Проверка сортировки: первое переключение (ожидаем новость с более поздней датой на позиции 0)", () -> {
            // Используем сохранённый экран — никаких openNewsManagement()!
            this.filteredNewsList.clickSortButton()
                    .assertNewsItemPosition(0, title2);
        });

        AllureStepHelper.step("Проверка сортировки: второе переключение (ожидаем новость с более ранней датой на позиции 0)", () -> {
            // Снова тот же экран
            this.filteredNewsList.clickSortButton()
                    .assertNewsItemPosition(0, title1);
        });
    }

    @Test
    @DisplayName("Сортировка новостей: одна новость в фильтре")
    @Description("Проверка поведения сортировки при наличии единственной новости в фильтре — она остаётся видимой при любых переключениях")
    @Severity(SeverityLevel.NORMAL)
    @Story("Сортировка по дате")
    public void shouldSortWithSingleNewsItem() {
        LocalDate newsDate = LocalDate.now().plusDays(8);
        LocalDate filterStart = LocalDate.now().plusDays(8);
        LocalDate filterEnd = LocalDate.now().plusDays(9);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title1);

        AllureStepHelper.step("Создание единственной новости (дата +8 дней)", () -> {
            newsScreen.openNewsManagement()
                    .openEditMode()
                    .clickAddNews()
                    .selectCategory(randomCategory)
                    .enterTitle(title1)
                    .setAndConfirmDate(newsDate.getYear(), newsDate.getMonthValue(), newsDate.getDayOfMonth())
                    .setAndConfirmTime(12, 0)
                    .enterDescription(description)
                    .saveNews()
                    .pullToRefresh();
        });

        AllureStepHelper.step("Применение фильтра, включающего дату новости", () -> {
            // Один раз получаем экран и сохраняем его
            this.filteredNewsList = newsScreen.openNewsManagement()
                    .openEditMode()
                    .openFilter()
                    .setStartDate(filterStart.getYear(), filterStart.getMonthValue(), filterStart.getDayOfMonth())
                    .setEndDate(filterEnd.getYear(), filterEnd.getMonthValue(), filterEnd.getDayOfMonth())
                    .clickFilterButton();
        });

        AllureStepHelper.step("Проверка видимости новости до сортировки", () -> {
            // Работаем с сохранённым экраном — никаких лишних переходов
            filteredNewsList.scrollUntilNewsItemFound(title1)
                    .assertNewsItemVisible(title1);
        });

        AllureStepHelper.step("Первое переключение сортировки — новость остаётся видимой", () -> {
            filteredNewsList.clickSortButton()
                    .scrollUntilNewsItemFound(title1)
                    .assertNewsItemVisible(title1);
        });

        AllureStepHelper.step("Второе переключение сортировки — новость остаётся видимой", () -> {
            filteredNewsList.clickSortButton()
                    .scrollUntilNewsItemFound(title1)
                    .assertNewsItemVisible(title1);
        });
    }
}
