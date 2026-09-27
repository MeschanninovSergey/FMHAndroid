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
public class NewsExpandTest extends BaseUiTest {

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
        } catch (Exception ignored) {
        }
    }

    @Test
    @Description("Разворачивание и сворачивание новости")
    @Severity(SeverityLevel.MINOR)
    public void shouldExpandAndCollapseNewsItem() {
        LocalDate tomorrow = LocalDate.now().plusDays(6);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

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
        newsList.scrollToNewsItem(title1)
                .clickNewsItemTitle(title1)
                .assertNewsDescriptionDisplayed(title1, description);

        newsList.clickNewsItemTitle(title1)
                .assertNewsDescriptionNotDisplayed(description)
                .deleteItemByTitle(title1)
                .confirmDeleteDialog();
    }
}