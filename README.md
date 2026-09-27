
# Запуск автотестов

## Требования
- Android Studio 4.x+
- Android SDK (API 29+)
- Физическое устройство или эмулятор с Android 10+
- Allure CLI (для генерации отчёта)

## Запуск тестов

### Все тесты
```bash
./gradlew connectedAndroidTest
```
### Отдельный класс
```bash
./gradlew connectedAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=ru.edu.qamid.ui.NegativeAuthTest
```
### Один метод
```bash
./gradlew connectedAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=ru.edu.qamid.ui.NegativeAuthTest#AuthWithWrongPassword
```
