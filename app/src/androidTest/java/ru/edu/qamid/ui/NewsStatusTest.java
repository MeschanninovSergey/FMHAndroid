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
@Feature("Статус новости")
@Owner("Мещанинов Сергей")
public class NewsStatusTest extends BaseUiTest {

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
    @DisplayName("Перевод новости в статус «Не активна»")
    @Description("Создание новости, перевод её в статус «Не активна», проверка статуса и удаление тестовой новости")
    @Severity(SeverityLevel.NORMAL)
    @Story("Деактивация новости")
    public void shouldDeactivateNews() {
        LocalDate tomorrow = LocalDate.now().plusDays(8);

        String title = TestDataGenerator.generateNewsTitle();
        createdNewsTitles.add(title);
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsEditScreen editScreen = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews();

        NewsListScreen newsList = editScreen
                .selectCategory(randomCategory)
                .enterTitle(title)
                .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .confirmTime()
                .enterDescription(description)
                .saveNews();

        newsList.pullToRefresh();
        newsList.scrollUntilNewsItemFound(title);
        newsList.assertNewsItemVisible(title);

        NewsListScreen updatedList = newsList
                .editItemByTitle(title)
                .toggleActiveSwitch("Активна")
                .saveNewsWithScroll();

        updatedList.assertNewsItemStatus(title, "НЕ АКТИВНА");
    }

    @Test
    @DisplayName("Цикл деактивации и повторной активации новости")
    @Description("Создание новости, её деактивация, последующая активация, проверка обоих статусов и удаление тестовой новости")
    @Severity(SeverityLevel.NORMAL)
    @Story("Деактивация/активация новости")
    public void shouldDeactivateAndReactivateNews() {
        LocalDate tomorrow = LocalDate.now().plusDays(8);

        String title = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title);

        final NewsListScreen[] currentList = new NewsListScreen[1];

        AllureStepHelper.step("Создание новости", () -> {
            currentList[0] = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews()
                    .selectCategory(randomCategory)
                    .enterTitle(title)
                    .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                    .confirmTime()
                    .enterDescription(description)
                    .saveNews();

            currentList[0].pullToRefresh()
                    .scrollUntilNewsItemFound(title)
                    .assertNewsItemVisible(title);
        });

        AllureStepHelper.step("Перевод новости в статус «Не активна» и проверка", () -> {
            currentList[0] = currentList[0]
                    .editItemByTitle(title)
                    .toggleActiveSwitch("Активна")
                    .saveNewsWithScroll();

            currentList[0].assertNewsItemStatus(title, "НЕ АКТИВНА");
        });

        AllureStepHelper.step("Возврат новости в статус «Активна» и проверка", () -> {
            currentList[0] = currentList[0]
                    .editItemByTitle(title)
                    .toggleActiveSwitch("Не активна")
                    .saveNewsWithScroll();

            currentList[0].pullToRefresh()
                    .scrollUntilNewsItemFound(title)
                    .assertNewsItemVisible(title)
                    .assertNewsItemStatus(title, "АКТИВНА");
        });
    }
}
