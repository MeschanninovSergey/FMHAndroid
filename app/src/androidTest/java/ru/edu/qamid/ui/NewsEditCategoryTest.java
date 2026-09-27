package ru.edu.qamid.ui;

import static androidx.test.espresso.Espresso.pressBack;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
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
public class NewsEditCategoryTest extends BaseUiTest {

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
    @Description("Изменение Категории на валидное значение и проверка сохранения")
    @Severity(SeverityLevel.NORMAL)
    public void shouldEditNewsCategoryAndVerify() {
        LocalDate tomorrow = LocalDate.now().plusDays(4);

        String title1 = TestDataGenerator.generateNewsTitle();
        String description = "Test_" + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();
        String randomCategory2 = NewsCategoryRandomizer.getRandomCategory();

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

        NewsListScreen updatedList = newsList
                .editItemByTitle(title1)
                .enterCategoryText(randomCategory2)
                .saveNews();

        updatedList.pullToRefresh();

        updatedList
                .scrollUntilNewsItemFound(title1)
                .editItemByTitle(title1)
                .assertCategoryEquals(randomCategory2);
    }
}