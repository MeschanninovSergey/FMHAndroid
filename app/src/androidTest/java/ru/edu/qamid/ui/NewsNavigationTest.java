package ru.edu.qamid.ui;

import static androidx.test.espresso.Espresso.pressBack;

import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;

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
import ru.edu.qamid.ui.screens.MissionScreen;
import ru.edu.qamid.ui.screens.NewsEditScreen;
import ru.edu.qamid.ui.screens.NewsListScreen;
import ru.edu.qamid.ui.screens.NewsScreen;
import ru.edu.qamid.ui.utils.AllureStepHelper;

@LargeTest
@Epic("Навигация")
@Feature("Переходы между экранами")
@Owner("Мещанинов Сергей")
public class NewsNavigationTest extends BaseUiTest {

    private NewsScreen newsScreen;

    @Before
    public void setUp() {
        if (isOnAuthScreen()) {
            newsScreen = loginAsDefaultUser();
        } else {
            newsScreen = new NewsScreen();
        }
    }

    @Test
    @DisplayName("Переход на страницу Новости")
    @Description("Переход на страницу Новости, проверяем что страница новости успешно открывается")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Меню")
    public void openAllNewsTest() {
        AllureStepHelper.step("Открытие страницы Новости через меню", () -> {
            NewsListScreen newsList = newsScreen.openNewsManagement();
            newsList.assertEditButtonDisplayed();
        });
    }

    @Test
    @DisplayName("Переход на страницу Цитаты с главной страницы")
    @Description("Переход на Цитаты со страницы Главная, проверка открытия страницы Цитаты")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Цитаты")
    public void appSwitchToQuotesFromMainPage() {
        AllureStepHelper.step("Открытие страницы Цитаты с главной страницы", () -> {
            MissionScreen mission = newsScreen.openMission();
            mission.assertTitleVisible();
        });
    }

    @Test
    @DisplayName("Переход на страницу Цитаты со страницы новостей")
    @Description("Переход на Цитаты со страницы новости, проверка открытия страницы Цитаты")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Цитаты")
    public void appSwitchToQuotesFromNewsPage() {
        AllureStepHelper.step("Переход на страницу Цитаты со страницы новостей", () -> {
            MissionScreen mission = newsScreen
                    .openNewsManagement()
                    .openMission();
            mission.assertTitleVisible();
        });
    }

    @Test
    @DisplayName("Возвращение на главную с экрана новостей")
    @Description("Возвращение на главную с экрана новостей, проверяем успешное открытие страницы главная")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Меню")
    public void shouldReturnToMainPageFromNewsSection() {
        AllureStepHelper.step("Открытие страницы Новости и возврат на главную через меню", () -> {
            NewsListScreen newsList = newsScreen.openNewsManagement();
            newsList.assertEditButtonDisplayed();

            NewsScreen mainScreen = newsList.returnToMainPage();
            mainScreen.assertAllNewsDisplayed();
        });
    }

    @Test
    @DisplayName("Открытие панели управления")
    @Description("Открытие панели управления, проверяем успешное открытие панели управления")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Панель управления")
    public void appOpenControlPanel() {
        AllureStepHelper.step("Открытие панели управления новостями", () -> {
            NewsEditScreen editScreen = newsScreen
                    .openNewsManagement()
                    .openEditMode();
            editScreen.assertControlPanelVisible();
        });
    }

    @Test
    @DisplayName("Возврат на главную через кнопку назад")
    @Description("Переход на страницу Главная со страницы Новости через кнопку назад, проверяем что после нажатия кнопки назад мы оказались на странице главная")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Кнопка назад")
    public void shouldNavigateBackToMainFromNews() {
        AllureStepHelper.step("Открытие страницы Новости", () -> {
            // Заходим на страницу управления новостями
            newsScreen.openNewsManagement();
        });

        AllureStepHelper.step("Нажатие кнопки назад и проверка возврата на главную", () -> {
            // Нажимаем системную кнопку «Назад»
            pressBack();

            // Проверяем, что мы вернулись на главный экран
            newsScreen.assertAllNewsDisplayed();
        });
    }
}
