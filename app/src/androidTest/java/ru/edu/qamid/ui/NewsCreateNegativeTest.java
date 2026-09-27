package ru.edu.qamid.ui;

import static androidx.test.espresso.Espresso.pressBack;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import ru.edu.qamid.ui.base.BaseUiTest;
import ru.edu.qamid.ui.screens.NewsEditScreen;
import ru.edu.qamid.ui.screens.NewsScreen;
import ru.edu.qamid.ui.utils.NewsCategoryRandomizer;
import ru.edu.qamid.ui.utils.TestDataGenerator;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class NewsCreateNegativeTest extends BaseUiTest {

    private NewsScreen newsScreen;

    @Before
    public void setUp() {
        newsScreen = loginAsDefaultUser();
    }

    @After
    public void tearDown() {
        try {
            pressBack();
            pressBack();
            pressBack();
            newsScreen.logout();
        } catch (Exception e) {
            try {
                pressBack();
                pressBack();
                newsScreen.logout();
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    @Description("Создание новости с некорректной категорией")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldStayOnCreateScreenWithInvalidCategory() {

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();

        NewsEditScreen editScreen = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews();

        editScreen.enterCategoryText("ошибка")
                .enterTitle(title1)
                .confirmDateAndTime()
                .enterDescription(description)
                .saveNewsExpectingFailure()
                .assertStillOnCreateScreen();
    }

    @Test
    @Description("Создание новости с пустым полем Категория")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldStayOnCreateScreenWithEmptyCategory() {

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();

        NewsEditScreen editScreen = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews();

        editScreen.enterTitle(title1)
                .confirmDateAndTime()
                .enterDescription(description)
                .saveNewsExpectingFailure()
                .assertStillOnCreateScreen();
    }

    @Test
    @Description("Создание новости с пустым полем Заголовок")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldStayOnCreateScreenWithEmptyTitle() {

        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsEditScreen editScreen = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews();

        editScreen.selectCategory(randomCategory)
                .enterTitle("")
                .confirmDateAndTime()
                .enterDescription(description)
                .saveNewsExpectingFailure()
                .assertStillOnCreateScreen();
    }

    @Test

    @Description("Создание новости с пустым полем Дата публикации")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldStayOnCreateScreenWithEmptyDate() {

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsEditScreen editScreen = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews();

        editScreen.enterCategoryText(randomCategory)
                .enterTitle(title1)
                .confirmTime()
                .enterDescription(description)
                .saveNewsExpectingFailure()
                .assertStillOnCreateScreen();
    }

    @Test
    @Description("Создание новости с пустым полем Время публикации")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldStayOnCreateScreenWithEmptyTime() {

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsEditScreen editScreen = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews();

        editScreen.enterCategoryText(randomCategory)
                .enterTitle(title1)
                .confirmDate()
                .enterDescription(description)
                .saveNewsExpectingFailure()
                .assertStillOnCreateScreen();
    }

    @Test
    @Description("Создание новости с пустым полем Описание")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldStayOnCreateScreenWithEmptyDescription() {

        String title1 = TestDataGenerator.generateNewsTitle();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsEditScreen editScreen = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews();

        editScreen.enterCategoryText(randomCategory)
                .enterTitle(title1)
                .confirmDateAndTime()
                .saveNewsExpectingFailure()
                .assertStillOnCreateScreen();
    }
}