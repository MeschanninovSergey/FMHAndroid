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
@Feature("Удаление новостей")
@Owner("Мещанинов Сергей")
public class NewsDeleteTest extends BaseUiTest {

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
    @DisplayName("Отмена создания новой новости")
    @Description("Отмена создания новости, проверяем что после отмены новость не сохраняется")
    @Severity(SeverityLevel.NORMAL)
    @Story("Отмена создания новости")
    public void shouldCancelNewsCreation() {
        LocalDate tomorrow = LocalDate.now().plusDays(3);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        AllureStepHelper.step("Открытие формы создания новости", () -> {
            NewsEditScreen editScreen = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews();

            AllureStepHelper.step("Заполнение данных для новости", () -> {
                editScreen.enterCategoryText(randomCategory)
                        .enterTitle(title1)
                        .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                        .confirmTime()
                        .enterDescription(description);
            });

            AllureStepHelper.step("Отмена создания и подтверждение диалога", () -> {
                NewsListScreen newsList = editScreen
                        .clickCancelButton()
                        .confirmCancelDialog();

                AllureStepHelper.step("Проверка, что новость не сохранилась", () -> {
                    newsList.assertNewsItemDoesNotExist(title1);
                });
            });
        });
    }

    @Test
    @DisplayName("Удаление новости")
    @Description("Удаление новости, проверяем что после удаления новость исчезает из списка")
    @Severity(SeverityLevel.NORMAL)
    @Story("Удаление новости")
    public void shouldDeleteNews() {
        LocalDate tomorrow = LocalDate.now().plusDays(3);

        String title = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title);

        AllureStepHelper.step("Создание новости", () -> {
            NewsListScreen newsListAfterSave = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews()
                    .selectCategory(randomCategory)
                    .enterTitle(title)
                    .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                    .confirmTime()
                    .enterDescription(description)
                    .saveNews();

            AllureStepHelper.step("Проверка наличия новости в списке", () -> {
                newsListAfterSave.pullToRefresh();
                newsListAfterSave.scrollUntilNewsItemFound(title)
                        .assertNewsItemVisible(title);
            });

            AllureStepHelper.step("Удаление новости и подтверждение", () -> {
                newsListAfterSave
                        .deleteItemByTitle(title)
                        .confirmDeleteDialog();
            });

            AllureStepHelper.step("Проверка отсутствия новости в списке", () -> {
                newsListAfterSave.pullToRefresh();
                newsListAfterSave.assertNewsItemDoesNotView(title);
            });
        });
    }

    @Test
    @DisplayName("Отмена удаления новости")
    @Description("Отмена удаления новости, проверяем что после отмены удаления новость присутствует в списке")
    @Severity(SeverityLevel.NORMAL)
    @Story("Отмена удаления новости")
    public void shouldNotDeleteNewsAfterCancel() {
        LocalDate tomorrow = LocalDate.now().plusDays(3);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title1);

        AllureStepHelper.step("Создание новости для теста отмены удаления", () -> {
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

            AllureStepHelper.step("Проверка наличия новости перед попыткой удаления", () -> {
                newsList.pullToRefresh()
                        .scrollUntilNewsItemFound(title1)
                        .assertNewsItemVisible(title1);
            });

            AllureStepHelper.step("Попытка удаления с отменой в диалоге", () -> {
                newsList.deleteItemByTitle(title1)
                        .cancelDeleteDialog();
            });

            AllureStepHelper.step("Проверка, что новость осталась в списке после отмены", () -> {
                newsList.pullToRefresh()
                        .assertNewsItemVisible(title1);
            });
        });
    }
}
