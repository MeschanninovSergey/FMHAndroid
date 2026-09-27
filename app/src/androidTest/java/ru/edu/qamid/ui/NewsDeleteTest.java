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

import java.time.LocalDate;
import java.util.UUID;

import ru.edu.qamid.ui.base.BaseUiTest;
import ru.edu.qamid.ui.screens.NewsEditScreen;
import ru.edu.qamid.ui.screens.NewsListScreen;
import ru.edu.qamid.ui.screens.NewsScreen;
import ru.edu.qamid.ui.utils.NewsCategoryRandomizer;
import ru.edu.qamid.ui.utils.TestDataGenerator;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class NewsDeleteTest extends BaseUiTest {

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
            newsScreen.logout();
        } catch (Exception e) {
            try {
                pressBack();
                newsScreen.logout();
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    @Description("Отмена создания новой новости")
    @Severity(SeverityLevel.NORMAL)
    public void shouldCancelNewsCreation() {
        LocalDate tomorrow = LocalDate.now().plusDays(3);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsEditScreen editScreen = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews();

        NewsListScreen newsList = editScreen
                .enterCategoryText(randomCategory)
                .enterTitle(title1)
                .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .confirmTime()
                .enterDescription(description)
                .clickCancelButton()
                .confirmCancelDialog();

        newsList.assertNewsItemDoesNotExist(title1);
    }

    @Test
    @Description("Удаление новости")
    @Severity(SeverityLevel.NORMAL)
    public void shouldDeleteNews() {
        LocalDate tomorrow = LocalDate.now().plusDays(3);

        String title = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

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

        newsListAfterSave.pullToRefresh();
        newsListAfterSave.scrollUntilNewsItemFound(title)
                .assertNewsItemVisible(title);

        newsListAfterSave
                .deleteItemByTitle(title)
                .confirmDeleteDialog();

        newsListAfterSave.pullToRefresh();
        newsListAfterSave.assertNewsItemDoesNotView(title);
    }

    @Test
    @Description("Отмена удаления новости")
    @Severity(SeverityLevel.NORMAL)
    public void shouldNotDeleteNewsAfterCancel() {
        LocalDate tomorrow = LocalDate.now().plusDays(3);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

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

        newsList.pullToRefresh();
        newsList.scrollUntilNewsItemFound(title1);
        newsList.assertNewsItemVisible(title1);

        newsList.deleteItemByTitle(title1)
                .cancelDeleteDialog();

        newsList.pullToRefresh();
        newsList.assertNewsItemVisible(title1);

        newsList.deleteItemByTitle(title1)
                .confirmDeleteDialog();
    }
}