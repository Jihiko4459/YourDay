package com.example.yourday.enums

enum class DayOfTheWeek(val id: Int, val dayWeeklyName: String) {
    MONDAY(1, "Понедельник"),
    TUESDAY(2, "Вторник"),
    WEDNESDAY(3, "Среда"),
    THURSDAY(4, "Четверг"),
    FRIDAY(5, "Пятница"),
    SATURDAY(6, "Суббота"),
    SUNDAY(7, "Воскресенье");

    companion object {
        fun getAll(): List<DayOfTheWeek> = entries.toList()
    }
}

enum class Gender(val id: Int, val genderName: String) {
    FEMALE(1, "Женский"),
    MALE(2, "Мужской");

    companion object {
        fun getAll(): List<Gender> = entries.toList()
    }
}
enum class ActivityType(
    val id: Int,
    val activityTypeName: String,
    val caloriesPerMin: Double,
    val iconUrl: String
) {
    RUNNING(
        id = 1,
        activityTypeName = "Бег",
        caloriesPerMin = 15.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//running_vgwo4ekd04xl%201.svg"
    ),
    WALKING(
        id = 2,
        activityTypeName = "Ходьба",
        caloriesPerMin = 6.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//walk_h6da4yqbend6%201.svg"
    ),
    CYCLING(
        id = 3,
        activityTypeName = "Велоспорт",
        caloriesPerMin = 10.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//cyclist_22i4htgezad9%201.svg"
    ),
    SWIMMING(
        id = 4,
        activityTypeName = "Плавание",
        caloriesPerMin = 9.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//swimming_dsp856fod376%201.svg"
    ),
    YOGA(
        id = 5,
        activityTypeName = "Йога",
        caloriesPerMin = 5.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//Group.svg"
    ),
    WORKOUT(
        id = 6,
        activityTypeName = "Тренировка",
        caloriesPerMin = 10.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//dumbbells_0rljf1lyt3gw%201.svg"
    ),
    SHOPPING(
        id = 7,
        activityTypeName = "Поход в магазин",
        caloriesPerMin = 4.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//shopping_cart_kn65d3gmkbvj%201.svg"
    ),
    WALKING_DOG(
        id = 8,
        activityTypeName = "Выгул собаки",
        caloriesPerMin = 5.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//dog_fals6qp13l3h%201.svg"
    ),
    CLEANING(
        id = 9,
        activityTypeName = "Уборка дома",
        caloriesPerMin = 5.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//broom_2ebf73plnojg%201.svg"
    ),
    HIKING(
        id = 10,
        activityTypeName = "Поход",
        caloriesPerMin = 7.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//camping_09eo57zvdr80%201.svg"
    ),
    FISHING(
        id = 11,
        activityTypeName = "Рыбалка",
        caloriesPerMin = 4.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//fishing_ohanh408p7ac%201.svg"
    ),
    SKIING(
        id = 12,
        activityTypeName = "Лыжи",
        caloriesPerMin = 12.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//skier_j1pwxf2vhlsa%201.svg"
    ),
    DANCING(
        id = 13,
        activityTypeName = "Танцы",
        caloriesPerMin = 8.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//dancing_bwj9463nfjr9%201.svg"
    ),
    ACTING(
        id = 14,
        activityTypeName = "Актерское мастерство",
        caloriesPerMin = 4.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//theatre_xhpbtryeeih0%201.svg"
    ),
    PLAYING_INSTRUMENT(
        id = 15,
        activityTypeName = "Игра на инструменте",
        caloriesPerMin = 3.0,
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//guitar_1n96kd6ki4lg%201.svg"
    );
}

enum class GoalAndHabitType(
    val id: Int,
    val nameGoalAndHabitType: String,
    val iconUrl: String
) {
    BOOKS(
        id = 1,
        nameGoalAndHabitType = "Книги",
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/Frame.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9GcmFtZS5zdmciLCJpYXQiOjE3NDkyNzY0NzYsImV4cCI6MTc4MDgxMjQ3Nn0.s4qKaDsxr7Zhb2Ep6bp15Nqfr5-ubXIjIoB8c4dzTPg"
    ),
    HEALTH(
        id = 2,
        nameGoalAndHabitType = "Здоровье",
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/healthy-recognition.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9oZWFsdGh5LXJlY29nbml0aW9uLnN2ZyIsImlhdCI6MTc0OTI3NjQ5MiwiZXhwIjoxNzgwODEyNDkyfQ.yiMmzxIG5sIUSlvNLcTIWETWx9welQKTQliaPKeMEps"
    ),
    EDUCATION(
        id = 3,
        nameGoalAndHabitType = "Обучение",
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/Education.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9FZHVjYXRpb24uc3ZnIiwiaWF0IjoxNzQ5Mjc2NDk5LCJleHAiOjE3ODA4MTI0OTl9.YkSQp2Ns7qP1IoadtytUP7uLHMcaXVLrgnWXsLMNA-M"
    ),
    CREATIVITY(
        id = 4,
        nameGoalAndHabitType = "Творчество",
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/masks-theater.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9tYXNrcy10aGVhdGVyLnN2ZyIsImlhdCI6MTc0OTI3NjUxMSwiZXhwIjoxNzgwODEyNTExfQ.2cQzTRh84_0xM8b10ZEYG7nAC89nRDYMjJDdbVvXjAc"
    ),
    MONEY(
        id = 5,
        nameGoalAndHabitType = "Деньги",
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/Frame-1.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQ-taGFiaXQtdHlwZS1pY29ucy9GcmFtZS0xLnN2ZyIsImlhdCI6MTc0OTI3NjUyMCwiZXhwIjoxNzgwODEyNTIwfQ.1EGS1N_en2U99wtCgAuRfz7gHUfhq8ijQOF1hxKxgAo"
    ),
    WORK(
        id = 6,
        nameGoalAndHabitType = "Работа",
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/briefcase.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9icmllZmNhc2Uuc3ZnIiwiaWF0IjoxNzQ5Mjc2NTM0LCJleHAiOjE3ODA4MTI1MzR9.flxtO-uGOO0nml32ee4z7CLJzc2TewkUWeEuuf-pEBc"
    ),
    FAMILY(
        id = 7,
        nameGoalAndHabitType = "Семья",
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/family.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9mYW1pbHkuc3ZnIiwiaWF0IjoxNzQ5Mjc2NTQxLCJleHAiOjE3ODA4MTI1NDF9.xi09ZYTFsOSkgx7ZpXNGzLjjbeQDKsNK3nFXENWAdC8"
    ),
    COMMUNICATION(
        id = 8,
        nameGoalAndHabitType = "Общение",
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/chatbubble-ellipses-outline.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9jaGF0YnViYmxlLWVsbGlwc2VzLW91dGxpbmUuc3ZnIiwiaWF0IjoxNzQ5Mjc2NTQ5LCJleHAiOjE3ODA4MTI1NDl9.IawzsQY9jpSUQdHg6HL1f0USPkTEV5YgonJeMux6TYs"
    ),
    PERSONAL_CHALLENGES(
        id = 9,
        nameGoalAndHabitType = "Личные вызовы",
        iconUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/Frame-2.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9GcmFtZS0yLnN2ZyIsImlhdCI6MTc0OTI3NjU1NCwiZXhwIjoxNzgwODEyNTU0fQ.1A3wWlbqU3N-lT1OHjeRu-vemXOUX3AjOWyXrKf-zwE"
    );
}
// GoalStatus enum
enum class GoalStatus(
    val id: Int,
    val nameGoalStatus: String
) {
    NOT_STARTED(1, "Не начата"),
    IN_PROGRESS(2, "В процессе"),
    COMPLETED(3, "Завершена");
}

// Unit enum
enum class UnitType(
    val id: Int,
    val nameUnitType: String
) {
    TABLET(1, "таб."),
    PACKAGE(2, "пак."),
    TIMES(3, "раз"),
    INJECTION(4, "укол"),
    DROPS(5, "кап."),
    GRAMS(6, "г"),
    MILLILITERS(7, "мл"),
    HOURS(8, "ч"),
    MINUTES(9, "мин"),
    KILOMETERS(10, "км"),
    STEPS(11, "шаг"),
    KILOGRAMS(12, "кг"),
    CENTIMETERS(13, "см");
}

