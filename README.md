<div align="center" dir="auto">
  <h1>🏋️ PlanGym</h1>
  <p><strong>مدیریت زندگی، عادت‌ها و تمرینات ورزشی در یک اپلیکیشن اندروید مدرن</strong></p>
  <p><em>Plan your life, build habits, and crush your workouts — all in one app.</em></p>
</div>

---

## فهرست / Table of Contents

- [فارسی](#فارسی)
- [English](#english)

---

## فارسی

### 📋 معرفی

**PlanGym** یک اپلیکیشن اندروید جامع برای **برنامه‌ریزی روزانه، عادت‌سازی و ثبت تمرینات ورزشی** است. این اپ با طراحی مدرن Material 3 و قابلیت切换 زبان بین فارسی و انگلیسی، تجربه‌ای روان و کاربرپسند ارائه می‌دهد.

### ✨ ویژگی‌ها

- **📝 برنامه‌ریز (Planner)**: مدیریت پروژه‌ها و تسک‌ها با اولویت‌بندی (کم، متوسط، زیاد، فوری)، زیرتسک، تاریخ سررسید و یادآور
- **🔥 ردیاب عادت (Habit Tracker)**: ثبت عادت‌های روزانه/هفتگی/ماهانه با نمایش streak (تعداد روزهای پیاپی)
- **💪 ثبت تمرین ورزشی**: ساخت روتین، اضافه کردن تمرینات، ثبت ست‌ها با تکرار و وزن + تایمر استراحت خودکار
- **⏱️ کرنومتر تمرین**: تایمر عمومی برای تمرینات
- **📊 داشبورد**: نقل قول روزانه، تقویم ۷ روزه، خلاصه تسک‌ها و عادت‌های امروز
- **🎨 سه تم رنگی**: Calm Minimal (روشن)، Glass Violet (تیره بنفش)، Fitness Dark (مشکی با نئون)
- **🌐 دو زبانه**: فارسی (با پشتیبانی کامل از تاریخ جلالی و اعداد فارسی) و انگلیسی
- **🔔 یادآور**: نوتیفیکیشن خودکار برای تسک‌ها و عادت‌ها
- **📱 آفلاین**: تمام داده‌ها به صورت محلی با Room Database ذخیره می‌شود

### 🛠 تکنولوژی‌ها

| تکنولوژی | کاربرد |
|----------|--------|
| Kotlin 2.1 | زبان برنامه‌نویسی |
| Jetpack Compose | رابط کاربری مدرن و اعلانی |
| Material 3 | طراحی سیستم UI |
| Hilt | تزریق وابستگی |
| Room Database | ذخیره‌سازی محلی |
| WorkManager | زمان‌بندی تسک‌های پس‌زمینه |
| DataStore Preferences | تنظیمات تم و زبان |
| Navigation Compose | مسیریابی بین صفحات |
| Kotlin Coroutines | پردازش ناهمزمان |

### 🏗 معماری

پروژه از معماری **Clean Architecture** با سه لایه پیروی می‌کند:

- **UI Layer**: کامپوزبل‌ها و ViewModelها با StateFlow
- **Domain Layer**: repository interfaces و مدل‌های دامنه (Priority, Recurrence, ExerciseType)
- **Data Layer**: Room entities, DAOها و پیاده‌سازی repositoryها

### 🚀 شروع کار

1. پروژه را clone کنید
2. در Android Studio باز کنید (Ladybug یا جدیدتر)
3. Sync Project with Gradle Files
4. اجرا روی شبیه‌ساز یا دستگاه فیزیکی (Android API 24+)

### 📦 ساختار پروژه

```
com.ladystoneco.plangym/
├── MainActivity.kt
├── PlangymApp.kt
├── data/
│   ├── local/ (Room DB, DAOs, Entities)
│   └── repository/
├── di/ (Hilt Modules)
├── domain/
│   ├── model/
│   └── repository/
├── ui/
│   ├── habit/
│   ├── home/
│   ├── navigation/
│   ├── planner/
│   ├── theme/
│   ├── util/
│   └── workout/
├── util/
└── worker/
```

---

## English

### 📋 Overview

**PlanGym** is a comprehensive Android application for **daily planning, habit building, and workout tracking**. Built with modern Android development tools and a sleek Material 3 interface, it supports both Persian (Farsi) and English languages.

### ✨ Features

- **📝 Planner**: Manage projects and tasks with priorities (Low, Medium, High, Urgent), subtasks, due dates, and reminders
- **🔥 Habit Tracker**: Track daily/weekly/monthly habits with visual streak counters
- **💪 Workout Logger**: Create routines, add exercises, log sets with reps/weights, and auto-rest timer
- **⏱️ Workout Timer**: General-purpose stopwatch
- **📊 Dashboard**: Daily motivational quotes, 7-day calendar, today's task/habit summary
- **🎨 Three Themes**: Calm Minimal (light), Glass Violet (dark purple), Fitness Dark (black with neon)
- **🌐 Bilingual**: English & Persian (Farsi) with full Jalali date and Persian digit support
- **🔔 Reminders**: Background notifications for tasks and habits via WorkManager
- **📱 Offline**: All data stored locally with Room Database

### 🛠 Tech Stack

| Technology | Purpose |
|-----------|---------|
| Kotlin 2.1 | Programming language |
| Jetpack Compose | Declarative UI toolkit |
| Material 3 | Modern design system |
| Hilt | Dependency injection |
| Room Database | Local persistence |
| WorkManager | Background task scheduling |
| DataStore Preferences | Theme & locale settings |
| Navigation Compose | Screen navigation |
| Kotlin Coroutines | Async operations |

### 🏗 Architecture

The project follows **Clean Architecture** with three layers:

- **UI Layer**: Composables & ViewModels with StateFlow
- **Domain Layer**: Repository interfaces & domain models (Priority, Recurrence, ExerciseType)
- **Data Layer**: Room entities, DAOs, and repository implementations

### 🚀 Getting Started

1. Clone the repository
2. Open in Android Studio (Ladybug or newer recommended)
3. Sync Project with Gradle Files
4. Run on emulator or physical device (Android API 24+)

### 📦 Project Structure

```
com.ladystoneco.plangym/
├── MainActivity.kt
├── PlangymApp.kt
├── data/
│   ├── local/ (Room DB, DAOs, Entities)
│   └── repository/
├── di/ (Hilt Modules)
├── domain/
│   ├── model/
│   └── repository/
├── ui/
│   ├── habit/
│   ├── home/
│   ├── navigation/
│   ├── planner/
│   ├── theme/
│   ├── util/
│   └── workout/
├── util/
└── worker/
```

---

## 📄 License

MIT License — see the [LICENSE](LICENSE) file for details.

---

<div align="center">
  <p>Made with ❤️ using Kotlin & Jetpack Compose</p>
  <p>ساخته شده با ❤️ با استفاده از کاتلین و جتپک کامپوز</p>
</div>
