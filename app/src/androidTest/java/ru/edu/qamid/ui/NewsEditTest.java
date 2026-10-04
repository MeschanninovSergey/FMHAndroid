package ru.edu.qamid.ui;

import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
@Feature("Редактирование новостей")
@Owner("Мещанинов Сергей")
public class NewsEditTest extends BaseUiTest {

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
    @DisplayName("Изменение Заголовка на валидное значение")
    @Description("Изменение Заголовка на валидное значение, проверяем что заголовок в новости изменился")
    @Severity(SeverityLevel.NORMAL)
    @Story("Изменение полей")
    public void shouldEditNewsTitleAndVerify() {
        LocalDate tomorrow = LocalDate.now().plusDays(5);

        String title1 = TestDataGenerator.generateNewsTitle();
        String title2 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        // Добавляем в список только реально созданную новость (title1).
        // title2 — это новое значение, оно ещё не существует в системе.
        createdNewsTitles.add(title1);

        AllureStepHelper.step("Создание новости с исходным заголовком", () -> {
            NewsEditScreen editScreen = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews();

            AllureStepHelper.step("Заполнение данных и сохранение новости", () -> {
                NewsListScreen newsList = editScreen
                        .selectCategory(randomCategory)
                        .enterTitle(title1)
                        .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                        .confirmTime()
                        .enterDescription(description)
                        .saveNews();

                AllureStepHelper.step("Проверка наличия новости в списке", () -> {
                    newsList.pullToRefresh()
                            .scrollToNewsItem(title1)
                            .assertNewsItemVisible(title1);
                });
            });
        });

        AllureStepHelper.step("Редактирование заголовка новости", () -> {
            NewsListScreen updatedList = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .editItemByTitle(title1)
                    .clearTitle()
                    .enterTitle(title2)
                    .saveNews();

            AllureStepHelper.step("Проверка, что заголовок обновился и старый заголовок отсутствует", () -> {
                updatedList
                        .pullToRefresh()
                        .scrollToNewsItem(title2)
                        .assertNewsItemVisible(title2)
                        .assertNewsItemDoesNotExist(title1);
            });
        });
    }

    @Test
    @DisplayName("Изменение Даты новости на валидное значение")
    @Description("Изменение Даты новости на валидное значение, проверяем что дата публикации новости изменилась")
    @Severity(SeverityLevel.NORMAL)
    @Story("Изменение полей")
    public void shouldEditNewsDateAndVerify() {
        LocalDate tomorrow = LocalDate.now().plusDays(5);
        LocalDate afterTomorrow = LocalDate.now().plusDays(6);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title1);

        AllureStepHelper.step("Создание новости с исходной датой", () -> {
            NewsEditScreen editScreen = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .clickAddNews();

            AllureStepHelper.step("Заполнение данных и сохранение новости", () -> {
                NewsListScreen newsList = editScreen
                        .selectCategory(randomCategory)
                        .enterTitle(title1)
                        .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                        .confirmTime()
                        .enterDescription(description)
                        .saveNews();

                AllureStepHelper.step("Проверка наличия новости в списке", () -> {
                    newsList.pullToRefresh()
                            .scrollUntilNewsItemFound(title1)
                            .assertNewsItemVisible(title1);
                });
            });
        });

        AllureStepHelper.step("Редактирование даты новости", () -> {
            NewsListScreen updatedList = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .editItemByTitle(title1)
                    .setAndConfirmDate(afterTomorrow.getYear(), afterTomorrow.getMonthValue(), afterTomorrow.getDayOfMonth())
                    .saveNews();

            AllureStepHelper.step("Проверка, что новость отображается с обновлённой датой", () -> {
                updatedList.pullToRefresh()
                        .scrollUntilNewsItemFound(title1);

                NewsEditScreen editScreen = updatedList
                        .editItemByTitle(title1);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                String expectedDateString = afterTomorrow.format(formatter);

                editScreen.assertPublishDate(expectedDateString);

                editScreen.saveNews();

            });
        });
    }

    @Test
    @DisplayName("Изменение времени новости на валидное значение")
    @Description("Изменение времени новости на валидное значение и проверка сохранения")
    @Severity(SeverityLevel.NORMAL)
    @Story("Изменение полей")
    public void shouldEditNewsTimeAndVerify() {
        LocalDate tomorrow = LocalDate.now().plusDays(5);
        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title1);

        AllureStepHelper.step("Создание новости: категория «" + randomCategory + "», дата " + tomorrow, () -> {
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

            newsList.pullToRefresh();
            newsList.scrollUntilNewsItemFound(title1);
            newsList.assertNewsItemVisible(title1);
        });

        AllureStepHelper.step("Редактирование: установка нового времени (15:30) и сохранение", () -> {
            NewsListScreen updatedList = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .scrollUntilNewsItemFound(title1)
                    .editItemByTitle(title1)
                    .setAndConfirmTime(15, 30)
                    .saveNews();

            updatedList.pullToRefresh()
                    .scrollUntilNewsItemFound(title1)
                    .editItemByTitle(title1)
                    .assertPublishTime("15:30")
                    .saveNews();
        });
    }

    @Test
    @DisplayName("Изменение Описания на валидное значение")
    @Description("Изменение Описания на валидное значение, проверяем что описание новости изменилось")
    @Severity(SeverityLevel.NORMAL)
    @Story("Изменение полей")
    public void shouldEditNewsDescriptionAndVerify() {
        LocalDate tomorrow = LocalDate.now().plusDays(5);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String description2 = "Updated Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        createdNewsTitles.add(title1);

        AllureStepHelper.step("Создание новости с исходным описанием", () -> {
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

            newsList.pullToRefresh()
                    .scrollUntilNewsItemFound(title1)
                    .assertNewsItemVisible(title1);
        });

        AllureStepHelper.step("Редактирование описания новости", () -> {
            newsScreen.openNewsManagement()
                    .openEditMode()
                    .editItemByTitle(title1)
                    .clearDescription()
                    .enterDescription(description2)
                    .saveNews();
        });

        AllureStepHelper.step("Проверка, что описание обновилось", () -> {
            newsScreen.openNewsManagement()
                    .openEditMode()
                    .scrollUntilNewsItemFound(title1)
                    .clickNewsItemTitle(title1)
                    .assertNewsDescriptionDisplayed(title1, description2);
        });
    }
}
