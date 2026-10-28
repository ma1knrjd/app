# TeleStar — Android messenger MVP

Это стартовый проект Android-приложения на Kotlin/Jetpack Compose с собственным интерфейсом мессенджера:
- экран чатов и профиля;
- настройки и отдельная кнопка «Админ-панель»;
- редактирование отображаемого номера (это только текст профиля, не подтверждение номера);
- локальные демо-звёзды, Premium и подарки.

Важно: это локальный MVP-демо. Он пока не обеспечивает настоящую регистрацию по SMS, сетевую переписку между разными устройствами, звонки или интеграцию с официальным Telegram. Для этого нужны сервер и отдельная реализация.

## Как открыть и собрать APK
1. Установи Android Studio на компьютер.
2. Создай проект **Empty Activity** с Kotlin и Jetpack Compose (Minimum SDK 26).
3. Замени `app/src/main/java/com/example/telestar/MainActivity.kt` файлом `MainActivity.kt` из этого архива.
4. В `app/build.gradle.kts` убедись, что включены Compose и Material 3. Если проект создан по актуальному шаблону Android Studio, это уже настроено.
5. Нажми **Build → Build APK(s)**. APK обычно появится в `app/build/outputs/apk/debug/`.

Этот архив — исходники, не готовый APK: сборка Android требует Android SDK/Gradle окружения.
