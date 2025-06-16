package com.example.yourday.viewmodel.health_and_fitness

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalFoodItem
import com.example.yourday.repository.health_and_fitness.FoodItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FoodItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FoodItemRepository
    val foodItems: MutableStateFlow<List<LocalFoodItem>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = FoodItemRepository(db)
        loadFoodItems()
    }

    private fun loadFoodItems() {
        viewModelScope.launch {
            repository.getAll().collect { items ->
                foodItems.value = items
            }
        }
    }

    fun upsertFoodItem(item: LocalFoodItem) {
        viewModelScope.launch {
            repository.upsert(item)
        }
    }

    fun deleteFoodItem(item: LocalFoodItem) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }

    // 1. Функция загрузки начальных данных (при регистрации/входе)
    fun loadInitialFoodItems() {
        viewModelScope.launch {
            // Все продукты из CSV файла (первые 10 для примера)
            val initialFoodItems = listOf(
                LocalFoodItem(1, "Яблоко", 6, 52.0, 0.3, 0.2, 14.0),
                LocalFoodItem(2, "Банан", 6, 89.0, 1.1, 0.3, 23.0),
                LocalFoodItem(3, "Апельсин", 6, 43.0, 0.9, 0.2, 8.1),
                LocalFoodItem(4, "Груша", 6, 57.0, 0.4, 0.1, 15.0),
                LocalFoodItem(5, "Персик", 6, 39.0, 0.9, 0.3, 9.5),
                LocalFoodItem(6, "Клубника", 6, 32.0, 0.7, 0.3, 7.7),
                LocalFoodItem(7, "Малина", 6, 52.0, 1.2, 0.7, 11.9),
                LocalFoodItem(8, "Черника", 6, 57.0, 0.7, 0.3, 14.5),
                LocalFoodItem(9, "Киви", 6, 61.0, 1.1, 0.5, 14.7),
                LocalFoodItem(10, "Ананас", 6, 50.0, 0.5, 0.1, 13.1),
                LocalFoodItem(11, "Манго", 6, 60.0, 0.8, 0.4, 15.0),
                LocalFoodItem(12, "Гранат", 6, 83.0, 1.7, 1.2, 18.7),
                LocalFoodItem(13, "Виноград", 6, 69.0, 0.7, 0.2, 17.2),
                LocalFoodItem(14, "Авокадо", 6, 160.0, 2.0, 15.0, 9.0),
                LocalFoodItem(15, "Лимон", 6, 29.0, 1.1, 0.3, 9.3),
                LocalFoodItem(16, "Морковь", 6, 41.0, 0.9, 0.2, 9.6),
                LocalFoodItem(17, "Огурец", 6, 15.0, 0.7, 0.1, 3.6),
                LocalFoodItem(18, "Помидор", 6, 18.0, 0.9, 0.2, 3.9),
                LocalFoodItem(19, "Болгарский перец", 6, 27.0, 1.0, 0.3, 6.0),
                LocalFoodItem(20, "Брокколи", 6, 35.0, 2.4, 0.4, 7.2),
                LocalFoodItem(21, "Цветная капуста", 6, 25.0, 1.9, 0.3, 5.0),
                LocalFoodItem(22, "Кабачок", 6, 17.0, 1.2, 0.3, 3.1),
                LocalFoodItem(23, "Баклажан", 6, 24.0, 1.0, 0.2, 5.7),
                LocalFoodItem(24, "Тыква", 6, 26.0, 1.0, 0.1, 6.5),
                LocalFoodItem(25, "Свекла", 6, 44.0, 1.7, 0.2, 10.0),
                LocalFoodItem(26, "Картофель вареный", 6, 93.0, 2.5, 0.1, 21.0),
                LocalFoodItem(27, "Лук репчатый", 6, 40.0, 1.1, 0.1, 9.3),
                LocalFoodItem(28, "Чеснок", 6, 149.0, 6.4, 0.5, 33.1),
                LocalFoodItem(29, "Шпинат", 6, 23.0, 2.9, 0.4, 3.6),
                LocalFoodItem(30, "Салат листовой", 6, 15.0, 1.4, 0.2, 2.9),
                LocalFoodItem(31, "Фасоль стручковая", 6, 31.0, 1.8, 0.1, 7.0),
                LocalFoodItem(32, "Горох зеленый", 6, 81.0, 5.4, 0.4, 14.5),
                LocalFoodItem(33, "Кукуруза вареная", 6, 96.0, 3.4, 1.5, 21.0),
                LocalFoodItem(34, "Сельдерей", 6, 14.0, 0.7, 0.2, 3.0),
                LocalFoodItem(35, "Редис", 6, 16.0, 0.7, 0.1, 3.4),
                LocalFoodItem(36, "Овсяная каша", 6, 68.0, 2.4, 1.4, 12.0),
                LocalFoodItem(37, "Гречневая каша", 6, 101.0, 3.4, 0.6, 21.0),
                LocalFoodItem(38, "Рис вареный", 6, 130.0, 2.7, 0.3, 28.0),
                LocalFoodItem(39, "Киноа", 6, 120.0, 4.4, 1.9, 21.3),
                LocalFoodItem(40, "Перловая крупа", 6, 123.0, 2.3, 0.4, 28.2),
                LocalFoodItem(41, "Пшеничная каша", 6, 135.0, 4.7, 0.4, 26.1),
                LocalFoodItem(42, "Ячневая крупа", 6, 76.0, 2.3, 0.3, 15.7),
                LocalFoodItem(43, "Булгур", 6, 83.0, 3.1, 0.2, 18.6),
                LocalFoodItem(44, "Манная крупа", 6, 328.0, 10.3, 1.0, 73.3),
                LocalFoodItem(45, "Кукурузная крупа", 6, 337.0, 8.3, 1.2, 75.0),
                LocalFoodItem(46, "Хлеб цельнозерновой", 6, 247.0, 13.0, 3.4, 41.0),
                LocalFoodItem(47, "Хлеб ржаной", 6, 259.0, 8.5, 3.3, 48.0),
                LocalFoodItem(48, "Макароны из твердых сортов", 6, 131.0, 5.0, 1.1, 25.0),
                LocalFoodItem(49, "Отруби овсяные", 6, 246.0, 17.3, 7.0, 66.0),
                LocalFoodItem(50, "Попкорн без масла", 6, 375.0, 12.0, 4.0, 78.0),
                LocalFoodItem(51, "Куриная грудка", 6, 165.0, 31.0, 3.6, 0.0),
                LocalFoodItem(52, "Филе индейки", 6, 135.0, 29.0, 1.7, 0.0),
                LocalFoodItem(53, "Говядина тушеная", 6, 220.0, 32.0, 10.0, 0.0),
                LocalFoodItem(54, "Свинина нежирная", 6, 242.0, 27.0, 14.0, 0.0),
                LocalFoodItem(55, "Баранина", 6, 294.0, 25.0, 21.0, 0.0),
                LocalFoodItem(56, "Утка", 6, 337.0, 19.0, 28.0, 0.0),
                LocalFoodItem(57, "Кролик", 6, 173.0, 33.0, 3.5, 0.0),
                LocalFoodItem(58, "Печень говяжья", 6, 135.0, 20.4, 3.1, 3.9),
                LocalFoodItem(59, "Куриное бедро", 6, 209.0, 26.0, 11.0, 0.0),
                LocalFoodItem(60, "Куриные крылья", 6, 203.0, 30.0, 8.0, 0.0),
                LocalFoodItem(61, "Ветчина", 6, 145.0, 22.0, 6.0, 1.0),
                LocalFoodItem(62, "Колбаса вареная", 6, 250.0, 12.0, 22.0, 1.5),
                LocalFoodItem(63, "Сардельки", 6, 296.0, 12.0, 27.0, 3.0),
                LocalFoodItem(64, "Бекон", 6, 541.0, 37.0, 42.0, 1.0),
                LocalFoodItem(65, "Куриная печень", 6, 116.0, 16.9, 4.8, 0.9),
                LocalFoodItem(66, "Лосось", 6, 206.0, 22.0, 12.0, 0.0),
                LocalFoodItem(67, "Тунец консервированный", 6, 116.0, 25.0, 0.9, 0.0),
                LocalFoodItem(68, "Треска", 6, 82.0, 17.8, 0.7, 0.0),
                LocalFoodItem(69, "Скумбрия", 6, 191.0, 18.0, 13.2, 0.0),
                LocalFoodItem(70, "Сельдь", 6, 158.0, 17.7, 9.0, 0.0),
                LocalFoodItem(71, "Креветки", 6, 99.0, 24.0, 0.3, 0.2),
                LocalFoodItem(72, "Кальмары", 6, 100.0, 18.0, 2.2, 2.0),
                LocalFoodItem(73, "Мидии", 6, 77.0, 11.9, 2.2, 3.7),
                LocalFoodItem(74, "Крабовое мясо", 6, 87.0, 18.1, 1.1, 0.0),
                LocalFoodItem(75, "Икра красная", 6, 245.0, 31.5, 15.0, 0.0),
                LocalFoodItem(76, "Форель", 6, 119.0, 20.5, 3.5, 0.0),
                LocalFoodItem(77, "Палтус", 6, 111.0, 22.5, 1.9, 0.0),
                LocalFoodItem(78, "Осьминог", 6, 82.0, 14.9, 1.0, 2.2),
                LocalFoodItem(79, "Устрицы", 6, 81.0, 9.0, 2.0, 4.0),
                LocalFoodItem(80, "Морская капуста", 6, 49.0, 0.9, 0.2, 12.0),
                LocalFoodItem(81, "Творог 5%", 6, 121.0, 17.0, 5.0, 3.0),
                LocalFoodItem(82, "Творог обезжиренный", 6, 72.0, 16.5, 0.5, 1.3),
                LocalFoodItem(83, "Молоко 2.5%", 6, 52.0, 3.4, 2.5, 4.7),
                LocalFoodItem(84, "Кефир 2.5%", 6, 53.0, 3.4, 2.5, 4.7),
                LocalFoodItem(85, "Йогурт натуральный", 6, 60.0, 4.3, 3.2, 6.2),
                LocalFoodItem(86, "Греческий йогурт", 6, 59.0, 10.0, 0.4, 3.6),
                LocalFoodItem(87, "Сметана 15%", 6, 162.0, 2.6, 15.0, 3.0),
                LocalFoodItem(88, "Сыр твердый", 6, 402.0, 25.0, 33.0, 1.3),
                LocalFoodItem(89, "Сыр фета", 6, 264.0, 14.2, 21.3, 4.1),
                LocalFoodItem(90, "Сыр моцарелла", 6, 280.0, 28.0, 17.0, 3.1),
                LocalFoodItem(91, "Ряженка", 6, 54.0, 2.9, 2.5, 4.2),
                LocalFoodItem(92, "Сливочное масло", 6, 717.0, 0.9, 81.0, 0.1),
                LocalFoodItem(93, "Сыр плавленый", 6, 235.0, 16.8, 18.0, 2.0),
                LocalFoodItem(94, "Пармезан", 6, 392.0, 35.7, 25.8, 3.2),
                LocalFoodItem(95, "Брынза", 6, 260.0, 22.0, 20.0, 0.0),
                LocalFoodItem(96, "Миндаль", 6, 609.0, 21.0, 53.0, 13.0),
                LocalFoodItem(97, "Грецкий орех", 6, 654.0, 15.2, 65.2, 13.7),
                LocalFoodItem(98, "Арахис", 6, 567.0, 25.8, 49.2, 16.1),
                LocalFoodItem(99, "Кешью", 6, 553.0, 18.5, 43.9, 30.2),
                LocalFoodItem(100, "Фисташки", 6, 562.0, 20.3, 45.3, 27.5),
                LocalFoodItem(101, "Фундук", 6, 628.0, 15.0, 60.8, 16.7),
                LocalFoodItem(102, "Семена чиа", 6, 486.0, 16.5, 30.7, 42.1),
                LocalFoodItem(103, "Тыквенные семечки", 6, 559.0, 30.2, 49.0, 10.7),
                LocalFoodItem(104, "Подсолнечные семечки", 6, 584.0, 21.0, 51.0, 20.0),
                LocalFoodItem(105, "Кунжут", 6, 573.0, 18.0, 50.0, 23.0),
                LocalFoodItem(106, "Льняное семя", 6, 534.0, 18.3, 42.2, 28.9),
                LocalFoodItem(107, "Арахисовая паста", 6, 588.0, 25.0, 50.0, 20.0),
                LocalFoodItem(108, "Миндальная паста", 6, 614.0, 21.0, 56.0, 19.0),
                LocalFoodItem(109, "Кокосовая стружка", 6, 660.0, 6.9, 64.5, 23.7),
                LocalFoodItem(110, "Какао-порошок", 6, 228.0, 19.6, 13.7, 57.9),
                LocalFoodItem(111, "Яйцо куриное", 6, 155.0, 13.0, 11.0, 1.1),
                LocalFoodItem(112, "Оливковое масло", 6, 898.0, 0.0, 99.8, 0.0),
                LocalFoodItem(113, "Мед", 6, 304.0, 0.3, 0.0, 82.4),
                LocalFoodItem(114, "Горчица", 6, 67.0, 4.4, 4.0, 5.3),
                LocalFoodItem(115, "Кетчуп", 6, 93.0, 1.8, 0.3, 22.2),
                LocalFoodItem(116, "Соевый соус", 6, 53.0, 8.0, 0.1, 4.9),
                LocalFoodItem(117, "Темный шоколад 70%", 6, 598.0, 7.8, 42.7, 45.9),
                LocalFoodItem(118, "Молочный шоколад", 6, 535.0, 7.7, 29.7, 59.4),
                LocalFoodItem(119, "Кофе молотый", 6, 0.0, 0.1, 0.0, 0.0),
                LocalFoodItem(120, "Чай черный", 6, 0.0, 0.0, 0.0, 0.0),
                LocalFoodItem(121, "Питайя (драконий фрукт)", 6, 60.0, 0.5, 0.4, 13.0),
                LocalFoodItem(122, "Маракуйя", 6, 97.0, 2.2, 0.7, 23.0),
                LocalFoodItem(123, "Папайя", 6, 43.0, 0.5, 0.3, 11.0),
                LocalFoodItem(124, "Гуава", 6, 68.0, 2.6, 1.0, 14.0),
                LocalFoodItem(125, "Личи", 6, 66.0, 0.8, 0.4, 16.0),
                LocalFoodItem(126, "Карамбола", 6, 31.0, 1.0, 0.3, 7.0),
                LocalFoodItem(127, "Дуриан", 6, 147.0, 1.5, 5.3, 27.0),
                LocalFoodItem(128, "Рамбутан", 6, 82.0, 0.9, 0.2, 20.0),
                LocalFoodItem(129, "Саподилла", 6, 83.0, 0.4, 1.1, 20.0),
                LocalFoodItem(130, "Фейхоа", 6, 55.0, 0.6, 0.4, 13.0),
                LocalFoodItem(131, "Шампиньоны", 6, 27.0, 3.1, 0.3, 3.3),
                LocalFoodItem(132, "Белые грибы", 6, 34.0, 3.7, 0.4, 3.1),
                LocalFoodItem(133, "Вешенки", 6, 33.0, 3.3, 0.4, 4.2),
                LocalFoodItem(134, "Лисички", 6, 38.0, 1.5, 0.5, 6.9),
                LocalFoodItem(135, "Опята", 6, 22.0, 2.2, 1.2, 2.7),
                LocalFoodItem(136, "Трюфели", 6, 51.0, 6.0, 0.5, 5.0),
                LocalFoodItem(137, "Шиитаке", 6, 34.0, 2.2, 0.5, 6.8),
                LocalFoodItem(138, "Маслята", 6, 19.0, 2.4, 0.7, 1.7),
                LocalFoodItem(139, "Подберезовики", 6, 31.0, 3.3, 0.5, 3.7),
                LocalFoodItem(140, "Грузди", 6, 16.0, 1.8, 0.8, 1.1),
                LocalFoodItem(141, "Сердце куриное", 6, 158.0, 26.0, 5.9, 0.0),
                LocalFoodItem(142, "Почки говяжьи", 6, 86.0, 15.0, 3.0, 1.0),
                LocalFoodItem(143, "Язык говяжий", 6, 146.0, 16.0, 12.0, 0.0),
                LocalFoodItem(144, "Рубец говяжий", 6, 85.0, 15.0, 3.7, 0.0),
                LocalFoodItem(145, "Печень куриная", 6, 116.0, 16.9, 4.8, 0.9),
                LocalFoodItem(146, "Легкие говяжьи", 6, 92.0, 16.0, 3.0, 0.0),
                LocalFoodItem(147, "Мозги говяжьи", 6, 143.0, 10.0, 11.0, 1.0),
                LocalFoodItem(148, "Хрящи куриные", 6, 40.0, 8.0, 1.0, 0.0),
                LocalFoodItem(149, "Утиная печень", 6, 136.0, 19.0, 6.0, 3.0),
                LocalFoodItem(150, "Свиные уши", 6, 233.0, 21.0, 16.0, 0.0),
                LocalFoodItem(151, "Спирулина", 6, 290.0, 57.0, 8.0, 24.0),
                LocalFoodItem(152, "Нори", 6, 35.0, 6.0, 0.3, 5.0),
                LocalFoodItem(153, "Вакаме", 6, 45.0, 3.0, 0.6, 9.0),
                LocalFoodItem(154, "Дульсе", 6, 35.0, 2.0, 0.6, 8.0),
                LocalFoodItem(155, "Хлорелла", 6, 410.0, 58.0, 9.0, 23.0),
                LocalFoodItem(156, "Сывороточный протеин", 6, 400.0, 80.0, 5.0, 10.0),
                LocalFoodItem(157, "Казеин", 6, 360.0, 75.0, 3.0, 8.0),
                LocalFoodItem(158, "BCAA", 6, 0.0, 100.0, 0.0, 0.0),
                LocalFoodItem(159, "Гейнер", 6, 380.0, 30.0, 5.0, 60.0),
                LocalFoodItem(160, "Креатин", 6, 0.0, 0.0, 0.0, 0.0),
                LocalFoodItem(161, "Куркума", 6, 312.0, 9.7, 3.3, 67.0),
                LocalFoodItem(162, "Имбирь сушеный", 6, 335.0, 9.0, 4.2, 71.0),
                LocalFoodItem(163, "Корица", 6, 247.0, 4.0, 1.2, 81.0),
                LocalFoodItem(164, "Карри", 6, 325.0, 12.0, 14.0, 58.0),
                LocalFoodItem(165, "Паприка", 6, 282.0, 14.0, 13.0, 54.0),
                LocalFoodItem(166, "Тмин", 6, 375.0, 18.0, 22.0, 44.0),
                LocalFoodItem(167, "Кориандр", 6, 298.0, 12.0, 18.0, 55.0),
                LocalFoodItem(168, "Базилик сушеный", 6, 233.0, 23.0, 4.0, 47.0),
                LocalFoodItem(169, "Орегано", 6, 265.0, 9.0, 4.3, 69.0),
                LocalFoodItem(170, "Розмарин", 6, 331.0, 4.9, 15.0, 64.0),
                LocalFoodItem(171, "Кимчи", 6, 32.0, 1.1, 0.2, 7.0),
                LocalFoodItem(172, "Соленые огурцы", 6, 12.0, 0.6, 0.1, 2.3),
                LocalFoodItem(173, "Квашеная капуста", 6, 19.0, 0.9, 0.1, 4.3),
                LocalFoodItem(174, "Комбуча", 6, 20.0, 0.0, 0.0, 5.0),
                LocalFoodItem(175, "Мисо-паста", 6, 199.0, 12.0, 6.0, 26.0),
                LocalFoodItem(176, "Темпе", 6, 193.0, 19.0, 11.0, 9.0),
                LocalFoodItem(177, "Натто", 6, 212.0, 18.0, 11.0, 14.0),
                LocalFoodItem(178, "Йогурт греческий", 6, 59.0, 10.0, 0.4, 3.6),
                LocalFoodItem(179, "Кефир", 6, 53.0, 3.4, 2.5, 4.7),
                LocalFoodItem(180, "Чайный гриб", 6, 30.0, 0.0, 0.0, 8.0),
                LocalFoodItem(181, "Тофу", 6, 76.0, 8.0, 4.8, 1.9),
                LocalFoodItem(182, "Сейтан", 6, 370.0, 75.0, 1.0, 14.0),
                LocalFoodItem(183, "Овсяное молоко", 6, 43.0, 0.5, 1.5, 7.0),
                LocalFoodItem(184, "Миндальное молоко", 6, 24.0, 0.5, 1.1, 3.4),
                LocalFoodItem(185, "Веганский сыр", 6, 280.0, 12.0, 22.0, 8.0),
                LocalFoodItem(186, "Веганский майонез", 6, 680.0, 0.5, 75.0, 2.0),
                LocalFoodItem(187, "Растительный йогурт", 6, 60.0, 3.0, 3.5, 5.0),
                LocalFoodItem(188, "Протеиновый батончик", 6, 350.0, 20.0, 12.0, 40.0),
                LocalFoodItem(189, "Соевое мясо", 6, 296.0, 52.0, 1.0, 30.0),
                LocalFoodItem(190, "Нутовая мука", 6, 387.0, 22.0, 6.0, 58.0),
                LocalFoodItem(191, "Артишок", 6, 47.0, 3.3, 0.2, 10.5),
                LocalFoodItem(192, "Бок-чой (китайская капуста)", 6, 13.0, 1.5, 0.2, 2.2),
                LocalFoodItem(193, "Физалис", 6, 53.0, 1.9, 0.7, 11.2),
                LocalFoodItem(194, "Морской окунь", 6, 97.0, 18.4, 2.0, 0.0),
                LocalFoodItem(195, "Козье молоко", 6, 69.0, 3.6, 4.1, 4.5),
                LocalFoodItem(196, "Сыр рикотта", 6, 174.0, 11.3, 13.0, 3.0),
                LocalFoodItem(197, "Кедровые орехи", 6, 673.0, 13.7, 68.4, 13.1),
                LocalFoodItem(198, "Тахини (кунжутная паста)", 6, 595.0, 17.0, 53.8, 21.2),
                LocalFoodItem(199, "Черная смородина", 6, 63.0, 1.4, 0.4, 15.4),
                LocalFoodItem(200, "Ягоды годжи", 6, 349.0, 14.3, 0.4, 77.1),
                LocalFoodItem(201, "Батат", 6, 86.0, 1.6, 0.1, 20.1),
                LocalFoodItem(202, "Руккола", 6, 25.0, 2.6, 0.7, 3.7),
                LocalFoodItem(203, "Пак-чой", 6, 13.0, 1.5, 0.2, 2.2),
                LocalFoodItem(204, "Топинамбур", 6, 73.0, 2.0, 0.0, 17.4),
                LocalFoodItem(205, "Кольраби", 6, 27.0, 1.7, 0.1, 6.2),
                LocalFoodItem(206, "Редис дайкон", 6, 18.0, 0.6, 0.1, 4.1),
                LocalFoodItem(207, "Мангольд", 6, 19.0, 1.8, 0.2, 3.7),
                LocalFoodItem(208, "Кресс-салат", 6, 32.0, 2.6, 0.7, 5.5),
                LocalFoodItem(209, "Арракача", 6, 38.0, 1.1, 0.3, 9.0),
                LocalFoodItem(210, "Черри томаты", 6, 18.0, 0.9, 0.2, 3.9),
                LocalFoodItem(211, "Хурма", 6, 127.0, 0.8, 0.4, 33.5),
                LocalFoodItem(212, "Фейхоа", 6, 61.0, 0.7, 0.4, 15.2),
                LocalFoodItem(213, "Кумкват", 6, 71.0, 1.9, 0.9, 15.9),
                LocalFoodItem(214, "Желтая малина", 6, 52.0, 1.2, 0.7, 11.9),
                LocalFoodItem(215, "Бойзенова ягода", 6, 55.0, 1.4, 0.3, 12.2),
                LocalFoodItem(216, "Черноплодная рябина", 6, 52.0, 1.4, 0.2, 13.6),
                LocalFoodItem(217, "Мушмула", 6, 47.0, 0.4, 0.2, 12.1),
                LocalFoodItem(218, "Сахарное яблоко", 6, 94.0, 2.1, 0.3, 23.6),
                LocalFoodItem(219, "Тамарилло", 6, 31.0, 2.0, 0.4, 6.1),
                LocalFoodItem(220, "Азимина", 6, 80.0, 1.2, 1.2, 18.8),
                LocalFoodItem(221, "Орех пекан", 6, 691.0, 9.2, 72.0, 13.9),
                LocalFoodItem(222, "Макадамия", 6, 718.0, 7.9, 75.8, 13.8),
                LocalFoodItem(223, "Бразильский орех", 6, 659.0, 14.3, 66.4, 12.3),
                LocalFoodItem(224, "Семена конопли", 6, 553.0, 31.6, 48.8, 8.7),
                LocalFoodItem(225, "Семена подсолнечника (очищенные)", 6, 584.0, 20.8, 51.5, 20.0),
                LocalFoodItem(226, "Тилапия", 6, 96.0, 20.1, 1.7, 0.0),
                LocalFoodItem(227, "Морской черт", 6, 76.0, 14.5, 1.5, 0.0),
                LocalFoodItem(228, "Угорь", 6, 236.0, 23.7, 15.0, 0.0),
                LocalFoodItem(229, "Морской язык", 6, 83.0, 16.5, 1.5, 0.0),
                LocalFoodItem(230, "Раки", 6, 77.0, 15.9, 1.0, 1.2),
                LocalFoodItem(231, "Оленина", 6, 120.0, 22.0, 3.0, 0.0),
                LocalFoodItem(232, "Конина", 6, 175.0, 21.4, 9.9, 0.0),
                LocalFoodItem(233, "Перепелка", 6, 192.0, 19.6, 12.1, 0.0),
                LocalFoodItem(234, "Фазан", 6, 181.0, 22.7, 9.7, 0.0),
                LocalFoodItem(235, "Гусь", 6, 370.0, 15.2, 33.6, 0.0),
                LocalFoodItem(236, "Айран", 6, 40.0, 1.1, 1.5, 4.0),
                LocalFoodItem(237, "Катык", 6, 56.0, 2.8, 3.2, 4.0),
                LocalFoodItem(238, "Снежок", 6, 79.0, 2.8, 1.5, 14.0),
                LocalFoodItem(239, "Тан", 6, 24.0, 1.1, 1.5, 2.0),
                LocalFoodItem(240, "Ласси", 6, 98.0, 3.5, 3.8, 12.0),
                LocalFoodItem(241, "Амарант", 6, 371.0, 14.5, 7.0, 65.2),
                LocalFoodItem(242, "Тефф", 6, 367.0, 13.3, 2.4, 73.1),
                LocalFoodItem(243, "Сорго", 6, 329.0, 10.6, 3.5, 72.1),
                LocalFoodItem(244, "Полба", 6, 338.0, 14.6, 2.4, 70.2),
                LocalFoodItem(245, "Камют", 6, 337.0, 14.7, 2.2, 70.4),
                LocalFoodItem(246, "Мисо", 6, 199.0, 12.0, 6.0, 26.0),
                LocalFoodItem(247, "Нутовая мука", 6, 387.0, 22.0, 6.0, 58.0),
                LocalFoodItem(248, "Кокосовая мука", 6, 443.0, 19.3, 13.3, 60.0),
                LocalFoodItem(249, "Сироп топинамбура", 6, 267.0, 0.0, 0.0, 70.0),
                LocalFoodItem(250, "Патока", 6, 296.0, 0.0, 0.0, 78.7)
            )

            // Очищаем старые данные
            repository.deleteAll()

            // Добавляем новые
            initialFoodItems.forEach { item ->
                repository.upsert(item)
            }

            // Загружаем обновленный список
            loadFoodItems()
        }
    }

    // 2. Функция очистки данных (при выходе)
    fun clearFoodItems() {
        viewModelScope.launch {
            repository.deleteAll()
            foodItems.value = emptyList()
        }
    }
}