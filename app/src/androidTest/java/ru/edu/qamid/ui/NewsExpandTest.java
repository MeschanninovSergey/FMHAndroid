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
import ru.edu.qamid.ui.screens.NewsEditScreen;
import ru.edu.qamid.ui.screens.NewsListScreen;
import ru.edu.qamid.ui.screens.NewsScreen;
import ru.edu.qamid.ui.utils.AllureStepHelper;
import ru.edu.qamid.ui.utils.NewsCategoryRandomizer;
import ru.edu.qamid.ui.utils.TestDataGenerator;

@LargeTest
@Epic("Управление новостями")
@Feature("Просмотр новостей")
@Owner("Мещанинов Сергей")
public class NewsExpandTest extends BaseUiTest {

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
    @DisplayName("Разворачивание и сворачивание новости")
    @Description("Разворачивание и сворачивание новости, проверяем раскрытие и сворачивание описания новости")
    @Severity(SeverityLevel.MINOR)
    @Story("Сворачивание/разворачивание")
    public void shouldExpandAndCollapseNewsItem() {
        LocalDate tomorrow = LocalDate.now().plusDays(6);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title1);

        AllureStepHelper.step("Создание тестовой новости", () -> {
            NewsEditScreen editScreen = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews();

            NewsListScreen newsList = editScreen
                    .selectCategory(randomCategory)
                    .enterTitle(title1)
                    .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                    .confirmTime()
                    .enterDescription(description)
                    .saveNews();

            AllureStepHelper.step("Обновление списка и поиск созданной новости", () -> {
                newsList.pullToRefresh()
                        .scrollUntilNewsItemFound(title1)
                        .assertNewsItemVisible(title1);
            });
        });

        AllureStepHelper.step("Разворачивание и сворачивание новости: проверка отображения и скрытия описания", () -> {
            // Один раз открываем нужный контекст
            NewsListScreen newsList = newsScreen
                    .openNewsManagement()
                    .openEditMode();

            // 1. Разворачиваем и проверяем, что описание видно
            newsList.scrollUntilNewsItemFound(title1)
                    .clickNewsItemTitle(title1)                 // разворачиваем
                    .assertNewsDescriptionDisplayed(title1, description);

            // 2. Тут же сворачиваем (повторный клик по заголовку) и проверяем, что описания нет
            newsList.clickNewsItemTitle(title1)               // сворачиваем
                    .assertNewsDescriptionNotDisplayed(description);
        });
    }
}
