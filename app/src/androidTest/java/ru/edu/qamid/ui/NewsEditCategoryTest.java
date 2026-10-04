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
@Feature("Редактирование новостей")
@Owner("Мещанинов Сергей")
public class NewsEditCategoryTest extends BaseUiTest {

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
    @DisplayName("Изменение Категории на валидное значение")
    @Description("Изменение Категории на валидное значение, проверяем что категория новости изменилась")
    @Severity(SeverityLevel.NORMAL)
    @Story("Изменение категории")
    public void shouldEditNewsCategoryAndVerify() {
        LocalDate tomorrow = LocalDate.now().plusDays(4);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();
        String randomCategory2 = NewsCategoryRandomizer.getRandomCategory();

        // Добавляем новость в список для последующей очистки
        createdNewsTitles.add(title1);

        AllureStepHelper.step("Создание новости с исходной категорией", () -> {
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

        AllureStepHelper.step("Редактирование категории новости", () -> {
            NewsListScreen updatedList = newsScreen
                    .openNewsManagement()
                    .openEditMode()
                    .editItemByTitle(title1)
                    .enterCategoryText(randomCategory2)
                    .saveNews();

            AllureStepHelper.step("Обновление списка и поиск новости после редактирования", () -> {
                updatedList.pullToRefresh();
                updatedList.scrollUntilNewsItemFound(title1);
            });

            AllureStepHelper.step("Проверка, что категория обновлена", () -> {
                updatedList
                        .editItemByTitle(title1)
                        .assertCategoryEquals(randomCategory2)
                        .saveNews();
            });
        });
    }
}
