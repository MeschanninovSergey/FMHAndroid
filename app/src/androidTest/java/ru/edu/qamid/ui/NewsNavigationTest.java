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

import ru.edu.qamid.ui.base.BaseUiTest;
import ru.edu.qamid.ui.screens.AboutScreen;
import ru.edu.qamid.ui.screens.MissionScreen;
import ru.edu.qamid.ui.screens.NewsEditScreen;
import ru.edu.qamid.ui.screens.NewsListScreen;
import ru.edu.qamid.ui.screens.NewsScreen;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class NewsNavigationTest extends BaseUiTest {

    private NewsScreen newsScreen;

    @Before
    public void setUp() {
        newsScreen = loginAsDefaultUser();
    }

    @After
    public void tearDown() {
        try {
            newsScreen.logout();
        } catch (Exception e) {
            //
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
    }

    @Test
    @Description("Переход на страницу Новости")
    @Severity(SeverityLevel.CRITICAL)
    public void openAllNewsTest() {
        newsScreen
                .openNewsManagement()
                .assertEditButtonDisplayed();
    }

    @Test
    @Description("Переход на Цитаты со страницы Главная")
    @Severity(SeverityLevel.CRITICAL)
    public void appSwitchToQuotesFromMainPage() {
        MissionScreen mission = newsScreen.openMission();
        mission.assertTitleVisible();
    }

    @Test
    @Description("Переход на Цитаты со страницы новости")
    @Severity(SeverityLevel.CRITICAL)
    public void appSwitchToQuotesFromNewsPage() {
        MissionScreen mission = newsScreen
                .openNewsManagement()
                .openMission();

        mission.assertTitleVisible();
    }

    @Test
    @Description("Возвращение на главную с экрана новостей")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldReturnToMainPageFromNewsSection() {
        NewsListScreen newsList = newsScreen.openNewsManagement();
        newsList.assertEditButtonDisplayed();

        NewsScreen mainScreen = newsList.returnToMainPage();
        mainScreen.assertAllNewsDisplayed();
    }

    @Test
    @Description("Открытие панели управления")
    @Severity(SeverityLevel.CRITICAL)
    public void appOpenControlPanel() {
        NewsEditScreen editScreen = newsScreen
                .openNewsManagement()
                .openEditMode();

        editScreen.assertControlPanelVisible();
    }

    @Test
    @Description("Переход на страницу Главная со страницы Новости через кнопку назад")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldNavigateBackToMainFromNews() {
        NewsListScreen newsList = newsScreen.openNewsManagement();
        NewsScreen mainScreen = newsList.pressBack();
        mainScreen.assertAllNewsDisplayed();
    }
}