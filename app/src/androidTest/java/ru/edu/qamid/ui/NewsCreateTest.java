package ru.edu.qamid.ui;

import static androidx.test.espresso.Espresso.pressBack;

import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.UUID;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import ru.edu.qamid.ui.base.BaseUiTest;
import ru.edu.qamid.ui.screens.NewsEditScreen;
import ru.edu.qamid.ui.screens.NewsListScreen;
import ru.edu.qamid.ui.screens.NewsScreen;
import ru.edu.qamid.ui.utils.NewsCategoryRandomizer;
import ru.edu.qamid.ui.utils.TestDataGenerator;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class NewsCreateTest extends BaseUiTest {

    private NewsScreen newsScreen;

    @Before
    public void setUp() {
        newsScreen = loginAsDefaultUser();
    }

    @After
    public void tearDown() {
        try {
            newsScreen.logout();
            return;
        } catch (Exception ignored) {
        }
        for (int i = 0; i < 5; i++) {
            try {
                pressBack();
            } catch (Exception ignored) {
                break;
            }
        }
        try {
            newsScreen.logout();
        } catch (Exception ignored) {
        }
    }

    @Test
    @Description("Успешное создание новости")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldCreateNewsAndVerifyItInList() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

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
        newsList.scrollUntilNewsItemFound(title1);
        newsList.assertNewsItemVisible(title1);

        newsList.deleteItemByTitle(title1)
                .confirmDeleteDialog();
    }

    @Test
    @Description("Проверка созданной новости после выхода из приложения и новой авторизации")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldNewsPersistAfterRelogin() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

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
        newsList.scrollToNewsItem(title1);
        newsList.assertNewsItemVisible(title1);

        pressBack();

        newsScreen.logout();
        newsScreen = loginAsDefaultUser();
        newsScreen.openNewsManagement()
                .openEditMode()
                .scrollToNewsItem(title1)
                .assertNewsItemVisible(title1);
        pressBack();
    }

    @Test
    @Description("Создание новости со специальными символами")
    @Severity(SeverityLevel.MINOR)
    public void shouldCreateNewsWithSpecialChars() {
        LocalDate tomorrow = LocalDate.now().plusDays(2);

        String title = "Тест \"кавычки\" & <script>alert(1)</script> — символы";
        String description = "Test_{}[]@#$%^&* " + UUID.randomUUID().toString();
        String randomCategory = NewsCategoryRandomizer.getRandomCategory();

        NewsListScreen newsList = newsScreen
                .openNewsManagement()
                .openEditMode()
                .clickAddNews()
                .selectCategory(randomCategory)
                .enterTitle(title)
                .setAndConfirmDate(tomorrow.getYear(), tomorrow.getMonthValue(), tomorrow.getDayOfMonth())
                .confirmTime()
                .enterDescription(description)
                .saveNews();

        newsList.pullToRefresh();
        newsList.scrollUntilNewsItemFound(title);
        newsList.assertNewsItemVisible(title);

        newsList.deleteItemByTitle(title)
                .confirmDeleteDialog();
    }
}