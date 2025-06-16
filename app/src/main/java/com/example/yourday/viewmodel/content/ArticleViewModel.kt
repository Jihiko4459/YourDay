package com.example.yourday.viewmodel.content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalArticle
import com.example.yourday.repository.content.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date


class ArticleViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ArticleRepository
    val articles: MutableStateFlow<List<LocalArticle>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = ArticleRepository(db)
        loadArticles()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            repository.getAll().collect { articlesList ->
                articles.value = articlesList
            }
        }
    }

    fun upsertArticle(article: LocalArticle) {
        viewModelScope.launch {
            repository.upsert(article)
        }
    }

    fun deleteArticle(article: LocalArticle) {
        viewModelScope.launch {
            repository.delete(article)
        }
    }

    // Функция для добавления всех справочных статей при регистрации/входе
    fun addDefaultArticles() {
        val defaultArticles = listOf(
            LocalArticle(
                id = 1,
                title = "Как читать быстрее и запоминать больше",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Как читать быстрее и запоминать больше</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "        }\n" +
                        "        h1 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 24px;\n" +
                        "            margin: 0 0 10px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h3 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 18px;\n" +
                        "            margin: 18px 0 10px 0;\n" +
                        "            font-weight: 500;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        \n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        .reading-technique {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 15px;\n" +
                        "            border-radius: 8px;\n" +
                        "            margin-bottom: 15px;\n" +
                        "        }\n" +
                        "        .technique-title {\n" +
                        "            font-weight: 500;\n" +
                        "            color: #25242E;\n" +
                        "            margin-bottom: 8px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    \n" +
                        "    <p>В эпоху информационной перегрузки способность быстро усваивать текст становится сверхнавыком. Хорошая новость: скорость чтения и качество запоминания можно увеличить в 2-3 раза, применяя специальные техники. В этой статье — научно обоснованные методы, которые работают даже для тех, кто всегда читал медленно.</p>\n" +
                        "\n" +
                        "    <h2>1. Как мы читаем: механизмы восприятия</h2>\n" +
                        "    <p>Традиционное чтение имеет три ключевых ограничения:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Фокусировка на словах</strong> вместо смысловых блоков</li>\n" +
                        "        <li><strong>Субвокализация</strong> (проговаривание текста про себя)</li>\n" +
                        "        <li><strong>Регрессии</strong> (возвраты к уже прочитанному)</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Факт:</strong> Средняя скорость чтения взрослого человека — 200-300 слов в минуту, но мозг способен воспринимать до 800-1000 слов при правильной тренировке.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>2. Техники скорочтения</h2>\n" +
                        "\n" +
                        "    <div class=\"reading-technique\">\n" +
                        "        <p class=\"technique-title\">1. Метод указателя</p>\n" +
                        "        <p>Водите карандашом или пальцем под строкой. Это:</p>\n" +
                        "        <ul>\n" +
                        "            <li>Уменьшает количество регрессий на 50%</li>\n" +
                        "            <li>Позволяет контролировать темп</li>\n" +
                        "            <li>Увеличивает фокус внимания</li>\n" +
                        "        </ul>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"reading-technique\">\n" +
                        "        <p class=\"technique-title\">2. Чтение по диагонали</p>\n" +
                        "        <p>Фокусируйтесь на центре строки, воспринимая текст периферическим зрением. Начинайте с лёгких материалов, постепенно увеличивая сложность.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"reading-technique\">\n" +
                        "        <p class=\"technique-title\">3. Расширение зоны восприятия</p>\n" +
                        "        <p>Тренируйтесь видеть 3-5 слов одновременно. Используйте приложения-тренажёры (например, Spreeder) для развития этого навыка.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Техника</th>\n" +
                        "            <th>Скорость (слов/мин)</th>\n" +
                        "            <th>Запоминание</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Традиционное чтение</td>\n" +
                        "            <td>200-300</td>\n" +
                        "            <td>50-60%</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>С указателем</td>\n" +
                        "            <td>300-400</td>\n" +
                        "            <td>60-70%</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Диагональное</td>\n" +
                        "            <td>400-600</td>\n" +
                        "            <td>40-50%</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>С расширенным восприятием</td>\n" +
                        "            <td>600-1000</td>\n" +
                        "            <td>30-40%</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h2>3. Методы улучшения запоминания</h2>\n" +
                        "    <p>Скорость без понимания бессмысленна. Как запоминать больше:</p>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        <p>\"Читать без размышления — всё равно что есть без переваривания.\" — Эдмунд Бёрк</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <ol>\n" +
                        "        <li><strong>Предварительный просмотр:</strong> Пробегитесь по заголовкам, введению и заключению перед чтением</li>\n" +
                        "        <li><strong>Активные вопросы:</strong> Формулируйте вопросы к тексту до и во время чтения</li>\n" +
                        "        <li><strong>Метод Корнелла:</strong> Делайте заметки с разделением на ключевые идеи, вопросы и резюме</li>\n" +
                        "        <li><strong>Интервальные повторения:</strong> Возвращайтесь к материалу через 1 день, 1 неделю и 1 месяц</li>\n" +
                        "        <li><strong>Объяснение другому:</strong> Попробуйте пересказать прочитанное воображаемому слушателю</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <h2>4. Оптимальные условия для чтения</h2>\n" +
                        "    <p>Что повышает эффективность:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Освещение:</strong> Естественный свет или LED-лампа 4000-5000K</li>\n" +
                        "        <li><strong>Поза:</strong> Спина прямая, текст на расстоянии 30-40 см</li>\n" +
                        "        <li><strong>Время:</strong> Первые 2 часа после пробуждения — пик когнитивных способностей</li>\n" +
                        "        <li><strong>Гидратация:</strong> Стакан воды перед чтением улучшает когнитивные функции</li>\n" +
                        "        <li><strong>Отсутствие отвлекающих факторов:</strong> Режим \"Не беспокоить\" на 25-минутные интервалы</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Важно:</strong> Чередуйте техники в зависимости от типа материала. Художественную литературу лучше читать традиционно, а технические документы — с применением методов скорочтения.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>5. Тренировочный план на 21 день</h2>\n" +
                        "    <p>Как выработать привычку эффективного чтения:</p>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Неделя</th>\n" +
                        "            <th>Фокус</th>\n" +
                        "            <th>Упражнения</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>1</td>\n" +
                        "            <td>Устранение регрессий</td>\n" +
                        "            <td>Чтение с указателем, закрытие прочитанного листом</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>2</td>\n" +
                        "            <td>Расширение зоны восприятия</td>\n" +
                        "            <td>Тренажёры скорочтения, чтение колонками</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>3</td>\n" +
                        "            <td>Скорость + запоминание</td>\n" +
                        "            <td>Активные вопросы, метод Корнелла</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h2>Заключение</h2>\n" +
                        "    <p>Навык быстрого чтения с хорошим запоминанием — это комбинация правильных техник, регулярной практики и осознанного подхода. Начните с одного метода, добавьте второй через неделю, и уже через месяц вы заметите значительный прогресс.</p>\n" +
                        "\n" +
                        "    <p>Помните: цель не в том, чтобы прочитать как можно больше, а в том, чтобы извлекать максимум ценного из каждого текста. Скорость придёт с практикой, а понимание — с правильным подходом.</p>\n" +
                        "</body>\n" +
                        "</html>\n",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Thinking%20woman%20reading%20book%20at%20desk.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL1RoaW5raW5nIHdvbWFuIHJlYWRpbmcgYm9vayBhdCBkZXNrLnBuZyIsImlhdCI6MTc0OTIwNDY0NSwiZXhwIjoxNzgwNzQwNjQ1fQ.sWl97b1sRzfNsI9wq6CpVdONM-g89R-UiGGOVy3Socg",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 2,
                title = "Как перестать откладывать дела на потом",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Как перестать откладывать дела на потом</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h3 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 18px;\n" +
                        "            margin: 18px 0 10px 0;\n" +
                        "            font-weight: 500;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        \n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        .technique {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 15px;\n" +
                        "            border-radius: 8px;\n" +
                        "            margin-bottom: 20px;\n" +
                        "        }\n" +
                        "        .technique-title {\n" +
                        "            font-weight: 500;\n" +
                        "            color: #25242E;\n" +
                        "            margin-bottom: 8px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "    <p>Прокрастинация — бич современного общества. По статистике, 95% людей периодически откладывают важные дела, а 20% делают это систематически. В этой статье вы найдёте научно обоснованные методы борьбы с прокрастинацией, которые помогут наконец взять свою продуктивность под контроль.</p>\n" +
                        "\n" +
                        "    <h2>1. Почему мы откладываем дела?</h2>\n" +
                        "    <p>Основные психологические причины прокрастинации:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Страх неудачи:</strong> Лучше не делать, чем сделать плохо</li>\n" +
                        "        <li><strong>Перфекционизм:</strong> Ожидание \"идеального момента\"</li>\n" +
                        "        <li><strong>Недооценка времени:</strong> \"Я успею это сделать позже\"</li>\n" +
                        "        <li><strong>Неясность цели:</strong> Размытое представление о результате</li>\n" +
                        "        <li><strong>Эмоциональный дискомфорт:</strong> Задача вызывает негативные эмоции</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Исследования показывают:</strong> Прокрастинация активирует те же зоны мозга, что и физическая боль. Наш мозг буквально пытается избежать \"боли\" от выполнения задачи.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>2. 7 проверенных методов борьбы с прокрастинацией</h2>\n" +
                        "\n" +
                        "    <div class=\"technique\">\n" +
                        "        <p class=\"technique-title\">1. Метод \"Помидора\"</p>\n" +
                        "        <p>25 минут работы — 5 минут отдыха. Короткие интервалы делают задачу менее пугающей.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"technique\">\n" +
                        "        <p class=\"technique-title\">2. Правило 2-х минут</p>\n" +
                        "        <p>Если задача занимает меньше 2 минут — делайте сразу. Остальные — разбивайте на 2-минутные этапы.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"technique\">\n" +
                        "        <p class=\"technique-title\">3. Техника \"Швейцарского сыра\"</p>\n" +
                        "        <p>Делайте в задаче небольшие \"дырки\" — начинайте с самых простых частей, постепенно приближаясь к сложным.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"technique\">\n" +
                        "        <p class=\"technique-title\">4. Метод \"А что если\"</p>\n" +
                        "        <p>Задайте себе: \"Что если я сделаю это прямо сейчас?\" и \"Что если я не сделаю этого?\" Сравнение последствий мотивирует.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"technique\">\n" +
                        "        <p class=\"technique-title\">5. Визуализация завершения</p>\n" +
                        "        <p>Ярко представьте чувство удовлетворения после выполнения задачи. Это создаст позитивную мотивацию.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"technique\">\n" +
                        "        <p class=\"technique-title\">6. \"Поедание лягушки\"</p>\n" +
                        "        <p>Делайте самое неприятное дело первым с утра. Остальной день покажется лёгким.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"technique\">\n" +
                        "        <p class=\"technique-title\">7. Метод \"10 секунд\"</p>\n" +
                        "        <p>Когда возникает желание отложить — считайте от 10 до 1 и начинайте действовать на \"1\".</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>3. Как создать антипрокрастинационную среду</h2>\n" +
                        "    <p>Организуйте пространство и время для продуктивности:</p>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Что мешает</th>\n" +
                        "            <th>Как исправить</th>\n" +
                        "            <th>Эффект</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Отвлекающие уведомления</td>\n" +
                        "            <td>Режим \"Не беспокоить\"</td>\n" +
                        "            <td>+40% концентрации</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Размытые дедлайны</td>\n" +
                        "            <td>Конкретные сроки для подзадач</td>\n" +
                        "            <td>+35% выполнения</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Неорганизованное рабочее место</td>\n" +
                        "            <td>Минимализм на столе</td>\n" +
                        "            <td>+25% продуктивности</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Отсутствие ритуалов</td>\n" +
                        "            <td>Утренний алгоритм старта</td>\n" +
                        "            <td>Быстрый вход в поток</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        <p>\"Прокрастинация — это не проблема управления временем, это проблема управления эмоциями.\" — доктор Тимоти Пичил</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>4. Психологические трюки для мгновенной мотивации</h2>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Метод \"Только начать\":</strong> Договоритесь с собой поработать всего 5 минут</li>\n" +
                        "        <li><strong>Игра \"Несделанный список\":</strong> Запретите себе делать задачу 10 минут — возникнет желание начать</li>\n" +
                        "        <li><strong>Принцип \"Публичности\":</strong> Расскажите о своих планах друзьям</li>\n" +
                        "        <li><strong>Техника \"Награда после\":</strong> Приятное занятие только после выполнения</li>\n" +
                        "        <li><strong>Метод \"Чужой взгляд\":</strong> Представьте, что за вами наблюдают</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h2>5. Долгосрочные стратегии</h2>\n" +
                        "    <p>Как выработать устойчивость к прокрастинации:</p>\n" +
                        "    <ol>\n" +
                        "        <li><strong>Анализ триггеров:</strong> Замечайте, какие мысли/ситуации вызывают желание отложить</li>\n" +
                        "        <li><strong>Система привычек:</strong> Создавайте рутины, а не полагайтесь на силу воли</li>\n" +
                        "        <li><strong>Энергоменеджмент:</strong> Планируйте сложные задачи на пики своей активности</li>\n" +
                        "        <li><strong>Эмоциональная гигиена:</strong> Учитесь справляться с дискомфортом без избегания</li>\n" +
                        "        <li><strong>Гибкость:</strong> Разрешите себе иногда откладывать — без чувства вины</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Важно:</strong> Борьба с прокрастинацией — это марафон, а не спринт. Начните с малого — внедряйте по одной технике за раз и отслеживайте прогресс.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>Заключение</h2>\n" +
                        "    <p>Победить прокрастинацию полностью невозможно — и не нужно. Ваша цель не идеальная продуктивность, а осознанное управление своим временем и энергией. Начните с самых простых методов из этой статьи, отмечайте маленькие победы и постепенно формируйте новые привычки.</p>\n" +
                        "\n" +
                        "    <p>Помните: каждый момент — это новый шанс начать. Даже если вы откладывали это годами, сегодня — идеальный день, чтобы изменить ситуацию!</p>\n" +
                        "</body>\n" +
                        "</html>\n",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Woman%20watches%20a%20movie%20with%20popcorn.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL1dvbWFuIHdhdGNoZXMgYSBtb3ZpZSB3aXRoIHBvcGNvcm4ucG5nIiwiaWF0IjoxNzQ5MjA0OTYwLCJleHAiOjE3ODA3NDA5NjB9.IkrV5rmBYNoPnWnPpTSJ9do3o0pxHZeOi0WJf0EoAIs",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 3,
                title = "Метод Pomodoro: как работать без выгорания",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Метод Pomodoro: как работать без выгорания</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h3 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 18px;\n" +
                        "            margin: 18px 0 10px 0;\n" +
                        "            font-weight: 500;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        \n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        .step {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 12px;\n" +
                        "            border-radius: 6px;\n" +
                        "            margin-bottom: 15px;\n" +
                        "        }\n" +
                        "        .step-title {\n" +
                        "            font-weight: 500;\n" +
                        "            color: #25242E;\n" +
                        "            margin-bottom: 8px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "    <p>Метод Pomodoro — это простая, но мощная техника тайм-менеджмента, которая помогает сохранять концентрацию и избегать переутомления. Разработанный в конце 1980-х годов Франческо Чирилло, этот метод превратился в глобальное движение продуктивности. В этой статье вы узнаете, как правильно применять Pomodoro для максимальной эффективности без выгорания.</p>\n" +
                        "\n" +
                        "    <h2>1. Суть метода Pomodoro</h2>\n" +
                        "    <p>Основной принцип: работа делится на интервалы по 25 минут (помодоро), между которыми следуют короткие перерывы. После 4 помодоро — длинный перерыв.</p>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Почему это работает:</strong> Наш мозг оптимально сохраняет концентрацию в течение 20-30 минут. Регулярные перерывы предотвращают усталость и поддерживают высокий уровень продуктивности.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>2. Пошаговая инструкция</h2>\n" +
                        "\n" +
                        "    <div class=\"step\">\n" +
                        "        <p class=\"step-title\">Шаг 1. Подготовка</p>\n" +
                        "        <p>Выберите задачу, которую будете выполнять. Убедитесь, что у вас есть:</p>\n" +
                        "        <ul>\n" +
                        "            <li>Таймер (можно использовать специальные приложения или обычный)</li>\n" +
                        "            <li>Лист для учёта помодоро</li>\n" +
                        "            <li>Минимум отвлекающих факторов</li>\n" +
                        "        </ul>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"step\">\n" +
                        "        <p class=\"step-title\">Шаг 2. Настройка таймера</p>\n" +
                        "        <p>Установите таймер на 25 минут. Традиционно используется кухонный таймер в виде помидора (отсюда и название метода).</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"step\">\n" +
                        "        <p class=\"step-title\">Шаг 3. Работа</p>\n" +
                        "        <p>Работайте, не отвлекаясь, до сигнала таймера. Если возникла посторонняя мысль или задача — запишите её и сразу возвращайтесь к работе.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"step\">\n" +
                        "        <p class=\"step-title\">Шаг 4. Короткий перерыв</p>\n" +
                        "        <p>Когда прозвучит сигнал, сделайте перерыв 5 минут. Встаньте, пройдитесь, сделайте лёгкую разминку или просто закройте глаза.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"step\">\n" +
                        "        <p class=\"step-title\">Шаг 5. Цикл повторения</p>\n" +
                        "        <p>После 4 помодоро (около 2 часов работы) сделайте длинный перерыв 15-30 минут. Это время для полноценного отдыха.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Этап</th>\n" +
                        "            <th>Длительность</th>\n" +
                        "            <th>Рекомендации</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Помодоро</td>\n" +
                        "            <td>25 мин</td>\n" +
                        "            <td>Только одна задача, никаких отвлечений</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Короткий перерыв</td>\n" +
                        "            <td>5 мин</td>\n" +
                        "            <td>Физическая активность, отдых для глаз</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Длинный перерыв</td>\n" +
                        "            <td>15-30 мин</td>\n" +
                        "            <td>Приём пищи, медитация, прогулка</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h2>3. Научное обоснование эффективности</h2>\n" +
                        "    <p>Метод Pomodoro работает благодаря нескольким психологическим принципам:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Эффект дедлайна:</strong> Ограниченное время повышает концентрацию</li>\n" +
                        "        <li><strong>Микроцели:</strong> Разбивка на отрезки делает задачи менее пугающими</li>\n" +
                        "        <li><strong>Психологическая разгрузка:</strong> Регулярные перерывы предотвращают усталость</li>\n" +
                        "        <li><strong>Принцип вознаграждения:</strong> Предвкушение перерыва мотивирует</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        <p>\"Pomodoro — это не просто таймер, это философия уважения к своему времени и психической энергии.\"</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>4. Продвинутые техники</h2>\n" +
                        "    <p>Как адаптировать метод под свои нужды:</p>\n" +
                        "\n" +
                        "    <h3>Гибкое Pomodoro</h3>\n" +
                        "    <p>Можно регулировать длительность интервалов:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Для сложных задач: 50 минут работы / 10 минут отдыха</li>\n" +
                        "        <li>Для творческой работы: 90 минут работы / 20 минут отдыха</li>\n" +
                        "        <li>Для рутинных задач: 15 минут работы / 3 минуты отдыха</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h3>Тематические Pomodoro</h3>\n" +
                        "    <p>Выделяйте разные типы помодоро под разные задачи:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Красные — важные срочные задачи</li>\n" +
                        "        <li>Зелёные — обучение и развитие</li>\n" +
                        "        <li>Синие — рутинные операции</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h3>Pomodoro для командной работы</h3>\n" +
                        "    <p>Синхронизированные интервалы работы и перерывов для всей команды повышают коллективную продуктивность.</p>\n" +
                        "\n" +
                        "    <h2>5. Частые ошибки и как их избежать</h2>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Игнорирование перерывов:</strong> Перерывы — обязательная часть метода</li>\n" +
                        "        <li><strong>Многозадачность:</strong> Одно помодоро — одна задача</li>\n" +
                        "        <li><strong>Жёсткое следование времени:</strong> Адаптируйте длительность под свои ритмы</li>\n" +
                        "        <li><strong>Неучёт прерванных помодоро:</strong> Если отвлеклись — начинайте заново</li>\n" +
                        "        <li><strong>Отсутствие анализа:</strong> В конце дня просматривайте выполненные помодоро</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Совет:</strong> Используйте первые 5 минут нового помодоро для планирования, а последние 5 — для подведения итогов. Это увеличит осознанность работы.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>6. Лучшие приложения для Pomodoro</h2>\n" +
                        "    <p>Автоматизируйте процесс с помощью этих инструментов:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Focus To-Do:</strong> Таймер + трекер задач</li>\n" +
                        "        <li><strong>Pomodone:</strong> Интеграция с Trello, Asana</li>\n" +
                        "        <li><strong>Be Focused:</strong> Простой и удобный интерфейс</li>\n" +
                        "        <li><strong>Forest:</strong> Игрофикация метода</li>\n" +
                        "        <li><strong>Tide:</strong> Таймер с фоновыми звуками</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h2>Заключение</h2>\n" +
                        "    <p>Метод Pomodoro — это не просто техника тайм-менеджмента, а целостный подход к организации работы и отдыха. Начните с классического варианта (25/5), поэкспериментируйте с длительностью интервалов и найдите свой идеальный ритм. Уже через неделю применения вы заметите, как увеличилась не только продуктивность, но и удовольствие от работы.</p>\n" +
                        "\n" +
                        "    <p>Помните: цель Pomodoro — не просто сделать больше, а работать умнее и сохранять энергию в течение всего дня!</p>\n" +
                        "</body>\n" +
                        "</html>\n",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Woman%20works%20remotely%20with%20laptop%20on%20sofa.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL1dvbWFuIHdvcmtzIHJlbW90ZWx5IHdpdGggbGFwdG9wIG9uIHNvZmEucG5nIiwiaWF0IjoxNzQ5MjA0NzkxLCJleHAiOjE3ODA3NDA3OTF9.5QrSOywfGxUSPE-mmY3j2emBJDU0zkvWAhXqDHZI25U",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 4,
                title = "Утренняя зарядка за 10 минут: комплекс для ленивых",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Утренняя зарядка за 10 минут: комплекс для ленивых</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "        } \n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h3 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 18px;\n" +
                        "            margin: 18px 0 10px 0;\n" +
                        "            font-weight: 500;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        \n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        .exercise {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 12px;\n" +
                        "            border-radius: 6px;\n" +
                        "            margin-bottom: 15px;\n" +
                        "        }\n" +
                        "        .exercise-title {\n" +
                        "            font-weight: 500;\n" +
                        "            color: #25242E;\n" +
                        "            margin-bottom: 8px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "    <p>Не хватает времени или мотивации на полноценную тренировку? Этот 10-минутный утренний комплекс специально создан для тех, кто хочет быстро взбодриться, не вставая с постели раньше времени. Никакого оборудования, только ваше тело и 10 минут!</p>\n" +
                        "\n" +
                        "    <h2>1. Почему именно утренняя зарядка?</h2>\n" +
                        "    <p>Всего 10 минут утром дают:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Ускорение метаболизма</strong> на весь день</li>\n" +
                        "        <li><strong>Улучшение кровообращения</strong> и работы мозга</li>\n" +
                        "        <li><strong>Коррекцию осанки</strong> после сна</li>\n" +
                        "        <li><strong>Заряд энергии</strong> без кофеина</li>\n" +
                        "        <li><strong>Укрепление дисциплины</strong> с самого утра</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Исследования показывают:</strong> Люди, делающие утреннюю зарядку, на 30% продуктивнее в первой половине дня и реже страдают от дневной усталости.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>2. 10-минутный комплекс \"Для ленивых\"</h2>\n" +
                        "    <p>Выполняйте упражнения последовательно по 1 минуте каждое:</p>\n" +
                        "\n" +
                        "    <div class=\"exercise\">\n" +
                        "        <p class=\"exercise-title\">1. Потягивания в постели</p>\n" +
                        "        <p>Лёжа на спине, тянитесь руками вверх, а ногами вниз. Затем сделайте \"звезду\", максимально растягиваясь в стороны. Отлично пробуждает тело.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"exercise\">\n" +
                        "        <p class=\"exercise-title\">2. Кошечка-корова</p>\n" +
                        "        <p>На четвереньках: на вдохе прогибаем спину (смотрим вверх), на выдохе округляем (подбородок к груди). Улучшает гибкость позвоночника.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"exercise\">\n" +
                        "        <p class=\"exercise-title\">3. Мини-мостик</p>\n" +
                        "        <p>Лёжа на спине, ноги согнуты: поднимаем таз, сжимаем ягодицы на 2 секунды, опускаем. Укрепляет ягодицы и поясницу.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"exercise\">\n" +
                        "        <p class=\"exercise-title\">4. Повороты сидя</p>\n" +
                        "        <p>Сидя на кровати: поворачиваем корпус в стороны, руки помогают усилить поворот. Разминает спину и улучшает пищеварение.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"exercise\">\n" +
                        "        <p class=\"exercise-title\">5. Ножницы лёжа</p>\n" +
                        "        <p>Лёжа на спине: поднимаем ноги под углом 45° и скрещиваем. Укрепляет пресс и улучшает кровообращение в ногах.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"exercise\">\n" +
                        "        <p class=\"exercise-title\">6. Приседания у кровати</p>\n" +
                        "        <p>10-15 неглубоких приседаний, держась за край кровати для баланса. Активизирует крупные мышцы ног.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"exercise\">\n" +
                        "        <p class=\"exercise-title\">7. Вращения плечами</p>\n" +
                        "        <p>Стоя: круговые движения плечами вперед и назад. Снимает напряжение с шеи и плеч.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"exercise\">\n" +
                        "        <p class=\"exercise-title\">8. Глубокое дыхание</p>\n" +
                        "        <p>Стоя: 5 глубоких вдохов через нос с поднятием рук, выдохов через рот с опусканием. Наполняет тело кислородом.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Упражнение</th>\n" +
                        "            <th>Эффект</th>\n" +
                        "            <th>Вариант для совсем ленивых</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Потягивания</td>\n" +
                        "            <td>Пробуждение тела</td>\n" +
                        "            <td>Лежа в кровати</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Кошечка-корова</td>\n" +
                        "            <td>Гибкость позвоночника</td>\n" +
                        "            <td>Сидя на кровати</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Мини-мостик</td>\n" +
                        "            <td>Укрепление ягодиц</td>\n" +
                        "            <td>Просто сжатие ягодиц</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Повороты</td>\n" +
                        "            <td>Массаж органов</td>\n" +
                        "            <td>Повороты головы</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h2>3. Как сделать зарядку привычкой?</h2>\n" +
                        "    <p>7 лайфхаков для регулярности:</p>\n" +
                        "    <ol>\n" +
                        "        <li><strong>Не вставайте с кровати</strong> — начинайте прямо лёжа</li>\n" +
                        "        <li><strong>Привяжите к привычке</strong> — после стакана воды/после будильника</li>\n" +
                        "        <li><strong>Мини-версия</strong> — если нет 10 минут, сделайте хотя бы 2 упражнения</li>\n" +
                        "        <li><strong>Техника \"2 дня\"</strong> — не пропускайте больше 1 дня подряд</li>\n" +
                        "        <li><strong>Визуальный трекер</strong> — отмечайте в календаре выполненные дни</li>\n" +
                        "        <li><strong>Упрощайте</strong> — можно делать в пижаме, без коврика</li>\n" +
                        "        <li><strong>Награждайте себя</strong> — приятным душем или чаем после</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        <p>\"Утренняя зарядка — это как чистка зубов для всего тела. Не нужно ждать вдохновения, просто делайте это автоматически.\"</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>4. Частые ошибки начинающих</h2>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Слишком интенсивно</strong> — начинайте с комфортной нагрузки</li>\n" +
                        "        <li><strong>Задерживают дыхание</strong> — дышите глубоко и ритмично</li>\n" +
                        "        <li><strong>Делают резкие движения</strong> — особенно после сна</li>\n" +
                        "        <li><strong>Пропускают разминку</strong> — первые 2 минуты должны быть плавными</li>\n" +
                        "        <li><strong>Ждут мгновенных результатов</strong> — эффект накопительный</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h2>5. Дополнительные советы</h2>\n" +
                        "    <p>Как усилить эффект от мини-зарядки:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Открывайте окно</strong> — кислород усилит бодрящий эффект</li>\n" +
                        "        <li><strong>Включите музыку</strong> — ритмичные треки добавят энергии</li>\n" +
                        "        <li><strong>Пейте воду</strong> — стакан до и после восполнит потерю жидкости</li>\n" +
                        "        <li><strong>Улыбайтесь</strong> — запускает выработку эндорфинов</li>\n" +
                        "        <li><strong>Добавьте ароматерапию</strong> — цитрусовые или мятные масла бодрят</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Важно:</strong> Даже если вы сделали только половину комплекса — это уже победа. Главное — регулярность, а не идеальное выполнение.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>Заключение</h2>\n" +
                        "    <p>Этот 10-минутный комплекс доказал: чтобы улучшить здоровье и самочувствие, не нужны часы в спортзале. Начните с малого — прямо завтра, не дожидаясь понедельника. Через 2-3 недели вы заметите, как тело само просится на зарядку, а день начинается с бодрости и хорошего настроения.</p>\n" +
                        "\n" +
                        "    <p>Помните: идеальная зарядка — не самая длинная или сложная, а та, которую вы делаете регулярно!</p>\n" +
                        "</body>\n" +
                        "</html>",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Woman%20with%20dumbbells%20lifting%20leg.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL1dvbWFuIHdpdGggZHVtYmJlbGxzIGxpZnRpbmcgbGVnLnBuZyIsImlhdCI6MTc0OTIwNDc3NiwiZXhwIjoxNzgwNzQwNzc2fQ.TXtKeBhWhtgdyx1clk6eAp0raRm0-tX9gzJ3zqgVfTA",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 5,
                title = "Как начать бегать и не бросить через неделю",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Как начать бегать и не бросить через неделю</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h3 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 18px;\n" +
                        "            margin: 18px 0 10px 0;\n" +
                        "            font-weight: 500;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        \n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "    <p>Бег — один из самых доступных видов спорта, но статистика показывает, что 80% новичков бросают его в первые недели. В этой статье вы найдёте проверенную стратегию, которая поможет не только начать бегать, но и превратить это в устойчивую привычку.</p>\n" +
                        "\n" +
                        "    <h2>1. Почему мы бросаем бег?</h2>\n" +
                        "    <p>Основные причины отказа от бега в первые недели:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Слишком интенсивный старт:</strong> Попытка сразу бегать быстро и долго</li>\n" +
                        "        <li><strong>Отсутствие видимого прогресса:</strong> Нетерпение быстрых результатов</li>\n" +
                        "        <li><strong>Физический дискомфорт:</strong> Боли в мышцах, одышка</li>\n" +
                        "        <li><strong>Нехватка мотивации:</strong> Неочевидность цели</li>\n" +
                        "        <li><strong>Плохая организация:</strong> Неудобное время, неподходящая экипировка</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Факт:</strong> Для формирования устойчивой привычки к бегу требуется в среднем 6-8 недель регулярных занятий.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>2. Правильный старт: методика \"Бег/Ходьба\"</h2>\n" +
                        "    <p>Идеальная программа для начинающих (первые 4 недели):</p>\n" +
                        "    \n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Неделя</th>\n" +
                        "            <th>Схема</th>\n" +
                        "            <th>Общее время</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>1</td>\n" +
                        "            <td>1 мин бега / 2 мин ходьбы</td>\n" +
                        "            <td>21 мин (7 циклов)</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>2</td>\n" +
                        "            <td>2 мин бега / 2 мин ходьбы</td>\n" +
                        "            <td>24 мин (6 циклов)</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>3</td>\n" +
                        "            <td>3 мин бега / 1 мин ходьбы</td>\n" +
                        "            <td>24 мин (6 циклов)</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>4</td>\n" +
                        "            <td>4 мин бега / 1 мин ходьбы</td>\n" +
                        "            <td>25 мин (5 циклов)</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h3>Как определить комфортный темп?</h3>\n" +
                        "    <p>Во время бега вы должны:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Мочь говорить полными предложениями</li>\n" +
                        "        <li>Не чувствовать жжения в мышцах</li>\n" +
                        "        <li>Дышать через нос или нос/рот одновременно</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h2>3. Психологические приёмы для мотивации</h2>\n" +
                        "\n" +
                        "    <h3>Метод \"Цепочки\"</h3>\n" +
                        "    <p>Отмечайте в календаре каждый день пробежки. Ваша цель — не разрывать цепочку.</p>\n" +
                        "\n" +
                        "    <h3>Правило 5 минут</h3>\n" +
                        "    <p>Если нет настроения бегать, договоритесь с собой пробежать всего 5 минут. В 80% случаев вы продолжите.</p>\n" +
                        "\n" +
                        "    <h3>Визуализация результата</h3>\n" +
                        "    <p>Создайте доску визуализации с изображениями того, чего хотите достичь через бег.</p>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        <p>\"Бег — это не про скорость или расстояние. Бег — это про диалог с самим собой и победу над своими слабостями.\"</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>4. Как избежать типичных ошибок новичков</h2>\n" +
                        "    <ol>\n" +
                        "        <li><strong>Не экономьте на кроссовках.</strong> Подберите специальные беговые кроссовки в профессиональном магазине.</li>\n" +
                        "        <li><strong>Не бегайте натощак.</strong> Легкий перекус за 30-60 минут до пробежки обязателен.</li>\n" +
                        "        <li><strong>Не игнорируйте разминку.</strong> 5-7 минут динамической растяжки снизят риск травм.</li>\n" +
                        "        <li><strong>Не увеличивайте нагрузку резко.</strong> Правило 10%: увеличивайте дистанцию не более чем на 10% в неделю.</li>\n" +
                        "        <li><strong>Не сравнивайте себя с другими.</strong> Ваш главный соперник — вы вчерашний.</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <h2>5. Как превратить бег в привычку</h2>\n" +
                        "    <p>7 стратегий для закрепления привычки:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Фиксируйте время.</strong> Бегайте в одно и то же время.</li>\n" +
                        "        <li><strong>Найдите компанию.</strong> Бег с партнёром увеличивает ответственность.</li>\n" +
                        "        <li><strong>Отслеживайте прогресс.</strong> Используйте приложения типа Strava или Nike Run Club.</li>\n" +
                        "        <li><strong>Создайте ритуал.</strong> Определённая музыка, маршрут или форма.</li>\n" +
                        "        <li><strong>Вознаграждайте себя.</strong> Приятные бонусы за достижение мини-целей.</li>\n" +
                        "        <li><strong>Разнообразьте.</strong> Меняйте маршруты, темп, поверхность.</li>\n" +
                        "        <li><strong>Планируйте заранее.</strong> Готовьте форму с вечера.</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Важно:</strong> Пропуск 1-2 тренировок — не катастрофа. Главное — вернуться к графику, не делая длительных перерывов.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>Заключение</h2>\n" +
                        "    <p>Начать бегать — легко. Не бросить — вот настоящий вызов. Используйте постепенное увеличение нагрузки, находите внутреннюю мотивацию и помните: даже 10 минут бега лучше, чем ничего. Через 2 месяца регулярных занятий вы удивитесь, как бег может изменить не только ваше тело, но и мышление.</p>\n" +
                        "\n" +
                        "    <p>Ваша первая цель — не скорость или расстояние, а формирование привычки. Остальное придёт само!</p>\n" +
                        "</body>\n" +
                        "</html>\n",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/running%20courier%20girl.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL3J1bm5pbmcgY291cmllciBnaXJsLnBuZyIsImlhdCI6MTc0OTIwNTEyMiwiZXhwIjoxNzgwNzQxMTIyfQ.3HC0ojgta14DpT0JlWwTF9sMtZP-v_pvxGaJB17hiA0",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 6,
                title = "Что есть до и после тренировки",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Что есть до и после тренировки</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h3 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 18px;\n" +
                        "            margin: 18px 0 10px 0;\n" +
                        "            font-weight: 500;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        \n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <p>Правильное питание до и после тренировки — залог эффективных занятий и быстрого восстановления. В этой статье вы узнаете, какие продукты помогут получить максимум от ваших тренировок, а каких следует избегать.</p>\n" +
                        "\n" +
                        "    <h2>1. Почему важно правильно питаться вокруг тренировки?</h2>\n" +
                        "    <p>Стратегическое питание в тренировочные дни помогает:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Повысить эффективность тренировки</li>\n" +
                        "        <li>Ускорить восстановление мышц</li>\n" +
                        "        <li>Уменьшить повреждение мышечных волокон</li>\n" +
                        "        <li>Оптимизировать синтез белка</li>\n" +
                        "        <li>Поддержать энергетический баланс</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Факт:</strong> Правильное питание вокруг тренировки может увеличить её эффективность на 20-30%.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>2. Питание перед тренировкой</h2>\n" +
                        "\n" +
                        "    <h3>За 2-3 часа до тренировки</h3>\n" +
                        "    <p>Идеальный приём пищи должен содержать:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Сложные углеводы:</strong> Коричневый рис, киноа, овсянка, цельнозерновой хлеб</li>\n" +
                        "        <li><strong>Белок:</strong> Куриная грудка, рыба, тофу, яйца</li>\n" +
                        "        <li><strong>Немного полезных жиров:</strong> Авокадо, орехи, оливковое масло</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h3>За 30-60 минут до тренировки</h3>\n" +
                        "    <p>Если не успели поесть заранее, подойдут:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Бананы</li>\n" +
                        "        <li>Греческий йогурт с ягодами</li>\n" +
                        "        <li>Протеиновый коктейль</li>\n" +
                        "        <li>Небольшая порция орехов</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Тип тренировки</th>\n" +
                        "            <th>Рекомендуемые продукты</th>\n" +
                        "            <th>Запрещённые продукты</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Силовая</td>\n" +
                        "            <td>Углеводы + белок (овсянка с яйцами)</td>\n" +
                        "            <td>Жирная пища, бобовые</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Кардио</td>\n" +
                        "            <td>Легкие углеводы (банан, тост с мёдом)</td>\n" +
                        "            <td>Молочные продукты, клетчатка</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Интервальная</td>\n" +
                        "            <td>Быстрые и медленные углеводы (фрукты + орехи)</td>\n" +
                        "            <td>Жирное мясо, жареная пища</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h2>3. Питание после тренировки</h2>\n" +
                        "\n" +
                        "    <h3>В первые 30 минут (\"углеводное окно\")</h3>\n" +
                        "    <p>Лучшие варианты для быстрого восстановления:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Фруктовый смузи с протеином</li>\n" +
                        "        <li>Шоколадное молоко</li>\n" +
                        "        <li>Бананы с арахисовой пастой</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h3>Через 1-2 часа после тренировки</h3>\n" +
                        "    <p>Основной приём пищи должен включать:</p>\n" +
                        "    <ol>\n" +
                        "        <li><strong>Белок:</strong> Курица, рыба, говядина, тофу (20-40 г)</li>\n" +
                        "        <li><strong>Углеводы:</strong> Коричневый рис, гречка, сладкий картофель</li>\n" +
                        "        <li><strong>Овощи:</strong> Брокколи, шпинат, перец</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        <p>\"Питание после тренировки — это не просто еда, это продолжение вашей тренировки на биохимическом уровне.\"</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>4. Гидратация: до, во время и после</h2>\n" +
                        "    <p>Правильный питьевой режим:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>За 2 часа до:</strong> 500 мл воды</li>\n" +
                        "        <li><strong>За 30 минут до:</strong> 250 мл воды</li>\n" +
                        "        <li><strong>Во время:</strong> 100-200 мл каждые 15 минут</li>\n" +
                        "        <li><strong>После:</strong> 500 мл в течение часа</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h2>5. Примеры меню для разных целей</h2>\n" +
                        "\n" +
                        "    <h3>Для набора мышечной массы</h3>\n" +
                        "    <p><strong>До:</strong> Овсянка с бананом и протеином<br>\n" +
                        "    <strong>После:</strong> Говядина с рисом и овощами</p>\n" +
                        "\n" +
                        "    <h3>Для похудения</h3>\n" +
                        "    <p><strong>До:</strong> Греческий йогурт с ягодами<br>\n" +
                        "    <strong>После:</strong> Куриная грудка с киноа и салатом</p>\n" +
                        "\n" +
                        "    <h3>Для выносливости</h3>\n" +
                        "    <p><strong>До:</strong> Тосты с мёдом и банан<br>\n" +
                        "    <strong>После:</strong> Лосось с бататом и брокколи</p>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Важно:</strong> Индивидуальные потребности могут отличаться в зависимости от типа телосложения, интенсивности тренировок и личных особенностей метаболизма.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>Заключение</h2>\n" +
                        "    <p>Правильное питание вокруг тренировки — такой же важный элемент прогресса, как и сами занятия. Начните с базовых рекомендаций, отслеживайте свои ощущения и постепенно адаптируйте схему под свои потребности. Помните: нет универсального решения, но есть научно обоснованные принципы, которые помогут вам достичь лучших результатов.</p>\n" +
                        "\n" +
                        "    <p>Экспериментируйте, прислушивайтесь к своему телу — и ваши тренировки выйдут на новый уровень!</p>\n" +
                        "</body>\n" +
                        "</html>\n",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Woman%20grocery%20shopping.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL1dvbWFuIGdyb2Nlcnkgc2hvcHBpbmcucG5nIiwiaWF0IjoxNzQ5MjA0NzIwLCJleHAiOjE3ODA3NDA3MjB9.ecAbEmsiPLAdaeHCC5ECpiWu9GcADicJsl04zHCf16g",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 7,
                title = "Как вести бюджет, если нет времени",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Как вести бюджет, если нет времени</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h3 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 18px;\n" +
                        "            margin: 18px 0 10px 0;\n" +
                        "            font-weight: 500;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        \n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "\n" +
                        "    <p>В современном ритме жизни у многих просто не хватает времени на детальный учёт финансов. Однако контроль бюджета — основа финансового благополучия. В этой статье вы узнаете простые и эффективные методы учёта расходов для занятых людей.</p>\n" +
                        "\n" +
                        "    <h2>1. Почему важно вести бюджет, даже когда нет времени?</h2>\n" +
                        "    <p>Контроль финансов даёт вам:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Чёткое понимание, куда уходят деньги</li>\n" +
                        "        <li>Возможность находить скрытые резервы</li>\n" +
                        "        <li>Защиту от неожиданных финансовых проблем</li>\n" +
                        "        <li>Базис для долгосрочного планирования</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Факт:</strong> Люди, которые регулярно отслеживают свои расходы, в среднем накапливают на 20% больше сбережений за год.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>2. Минималистичные методы учёта бюджета</h2>\n" +
                        "\n" +
                        "    <h3>Метод \"Трёх категорий\"</h3>\n" +
                        "    <p>Разделите все расходы всего на 3 группы:</p>\n" +
                        "    <ol>\n" +
                        "        <li><strong>Основные:</strong> Жильё, еда, транспорт, связь</li>\n" +
                        "        <li><strong>Образ жизни:</strong> Развлечения, кафе, хобби</li>\n" +
                        "        <li><strong>Будущее:</strong> Сбережения, инвестиции, обучение</li>\n" +
                        "    </ol>\n" +
                        "    <p>Раз в неделю просто записывайте суммы по этим категориям.</p>\n" +
                        "\n" +
                        "    <h3>Метод \"Конвертов\" в цифровом формате</h3>\n" +
                        "    <p>Создайте несколько виртуальных \"конвертов\" (отдельные счета или карты):</p>\n" +
                        "    <ul>\n" +
                        "        <li>Основные расходы (50% дохода)</li>\n" +
                        "        <li>Свободные траты (30%)</li>\n" +
                        "        <li>Накопления (20%)</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Метод</th>\n" +
                        "            <th>Время в неделю</th>\n" +
                        "            <th>Эффективность</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Традиционный учёт</td>\n" +
                        "            <td>2-3 часа</td>\n" +
                        "            <td>Высокая</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Метод трёх категорий</td>\n" +
                        "            <td>15-20 минут</td>\n" +
                        "            <td>Средняя</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Автоматизированный учёт</td>\n" +
                        "            <td>5 минут</td>\n" +
                        "            <td>Высокая</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h2>3. Автоматизация — спасение для занятых</h2>\n" +
                        "    <p>Используйте технологии, чтобы экономить время:</p>\n" +
                        "\n" +
                        "    <h3>Банковские приложения</h3>\n" +
                        "    <p>Большинство современных банков предлагают:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Автоматическую категоризацию расходов</li>\n" +
                        "        <li>Напоминания о регулярных платежах</li>\n" +
                        "        <li>Анализ финансовых привычек</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h3>Специализированные сервисы</h3>\n" +
                        "    <p>Популярные приложения для автоматического учёта:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>MoneyLover:</strong> Простой интерфейс, синхронизация между устройствами</li>\n" +
                        "        <li><strong>ZenMoney:</strong> Подробная аналитика, планирование целей</li>\n" +
                        "        <li><strong>CoinKeeper:</strong> Визуальное представление бюджета</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        <p>\"Автоматизация учёта бюджета — это как автопилот для ваших финансов. Вы задаёте настройки один раз, а система работает на вас постоянно.\"</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>4. Экспресс-анализ бюджета за 5 минут</h2>\n" +
                        "    <p>Раз в месяц проводите быстрый анализ:</p>\n" +
                        "    <ol>\n" +
                        "        <li>Сравните доходы и расходы</li>\n" +
                        "        <li>Выделите 3 самые крупные статьи расходов</li>\n" +
                        "        <li>Проверьте, соответствуют ли траты вашим приоритетам</li>\n" +
                        "        <li>Скорректируйте бюджет на следующий месяц</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <h2>5. Полезные привычки для экономии времени</h2>\n" +
                        "    <p>Эти простые правила помогут держать бюджет под контролем:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Единый день для финансов:</strong> Выделите 15 минут в определённый день недели</li>\n" +
                        "        <li><strong>Чек-лист:</strong> Создайте шаблон для быстрой проверки бюджета</li>\n" +
                        "        <li><strong>Финансовые ритуалы:</strong> Например, проверка бюджета за утренним кофе</li>\n" +
                        "        <li><strong>Голосовые заметки:</strong> Быстро фиксируйте расходы в течение дня</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Совет:</strong> Начните с самого простого метода и постепенно усложняйте систему по мере появления времени и привычки к учёту.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>Заключение</h2>\n" +
                        "    <p>Вести бюджет при нехватке времени — реально. Ключ в выборе подходящего метода и использовании технологий. Даже минимальный учёт лучше, чем полное отсутствие контроля. Начните с 5 минут в день — и вы удивитесь, насколько яснее станет ваша финансовая картина.</p>\n" +
                        "\n" +
                        "    <p>Помните: идеальный бюджет — не тот, который учитывает каждую копейку, а тот, который работает на вас, не отнимая драгоценное время.</p>\n" +
                        "</body>\n" +
                        "</html>\n",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Dollar%20bills%20and%20credit%20card%20in%20wallet.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL0RvbGxhciBiaWxscyBhbmQgY3JlZGl0IGNhcmQgaW4gd2FsbGV0LnBuZyIsImlhdCI6MTc0OTIwNDU0NywiZXhwIjoxNzgwNzQwNTQ3fQ.GqzEQ6Cy5isDpXGaRHpQPxTYOctW2Ef4vpcq42jYsFE",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 8,
                title = "Инвестиции для начинающих: с чего начать",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Инвестиции для начинающих: с чего начать</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        \n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "   \n" +
                        "    <p>Инвестиции — это мощный инструмент для создания капитала, но для новичков они могут казаться сложными и запутанными. В этой статье мы разберём основы инвестирования, которые помогут вам сделать первые шаги на пути к финансовой независимости.</p>\n" +
                        "\n" +
                        "    <h2>1. Почему важно инвестировать?</h2>\n" +
                        "    <p>Деньги, лежащие без движения, теряют свою ценность из-за инфляции. Инвестиции помогают:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Сохранить и приумножить капитал</li>\n" +
                        "        <li>Обеспечить финансовую безопасность в будущем</li>\n" +
                        "        <li>Достичь долгосрочных финансовых целей</li>\n" +
                        "        <li>Создать пассивный доход</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Важно:</strong> Инвестиции всегда связаны с риском. Чем выше потенциальная доходность, тем выше риск потери средств.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>2. Основные принципы инвестирования</h2>\n" +
                        "    <p>Прежде чем начать инвестировать, усвойте эти фундаментальные правила:</p>\n" +
                        "\n" +
                        "    <ol>\n" +
                        "        <li><strong>Диверсификация:</strong> Не кладите все яйца в одну корзину. Распределяйте средства между разными активами.</li>\n" +
                        "        <li><strong>Долгосрочная перспектива:</strong> Рынки колеблются, но в долгосрочной перспективе показывают рост.</li>\n" +
                        "        <li><strong>Регулярность:</strong> Лучше инвестировать небольшие суммы регулярно, чем крупные суммы время от времени.</li>\n" +
                        "        <li><strong>Контроль эмоций:</strong> Не поддавайтесь панике при падении рынка и эйфории при его росте.</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <h2>3. С чего начать: пошаговый план</h2>\n" +
                        "\n" +
                        "    <h3>Шаг 1. Определите финансовые цели</h3>\n" +
                        "    <p>Чётко сформулируйте, для чего вы инвестируете:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Пенсионные накопления</li>\n" +
                        "        <li>Покупка недвижимости</li>\n" +
                        "        <li>Образование детей</li>\n" +
                        "        <li>Финансовая подушка безопасности</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h3>Шаг 2. Оцените свою готовность к риску</h3>\n" +
                        "    <p>Ответьте на вопросы:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Какой процент потерь вы готовы пережить?</li>\n" +
                        "        <li>На какой срок вы готовы вложить деньги?</li>\n" +
                        "        <li>Есть ли у вас долги с высокими процентами (их лучше погасить сначала)?</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h3>Шаг 3. Сформируйте стартовый капитал</h3>\n" +
                        "    <p>Начните с создания финансовой подушки (3-6 месячных расходов), а затем выделяйте на инвестиции 10-20% от дохода.</p>\n" +
                        "\n" +
                        "    <h3>Шаг 4. Выберите инструменты</h3>\n" +
                        "    <p>Основные варианты для начинающих:</p>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Инструмент</th>\n" +
                        "            <th>Доходность</th>\n" +
                        "            <th>Риск</th>\n" +
                        "            <th>Срок</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Банковские вклады</td>\n" +
                        "            <td>Низкая</td>\n" +
                        "            <td>Низкий</td>\n" +
                        "            <td>Краткосрочный</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Облигации</td>\n" +
                        "            <td>Средняя</td>\n" +
                        "            <td>Низкий-средний</td>\n" +
                        "            <td>Среднесрочный</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>ETF-фонды</td>\n" +
                        "            <td>Средняя-высокая</td>\n" +
                        "            <td>Средний</td>\n" +
                        "            <td>Долгосрочный</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Акции</td>\n" +
                        "            <td>Высокая</td>\n" +
                        "            <td>Высокий</td>\n" +
                        "            <td>Долгосрочный</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h3>Шаг 5. Откройте брокерский счёт</h3>\n" +
                        "    <p>Выберите надёжного брокера с низкими комиссиями. В России популярны:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Тинькофф Инвестиции</li>\n" +
                        "        <li>Финам</li>\n" +
                        "        <li>ВТБ Инвестиции</li>\n" +
                        "        <li>Сбербанк Инвестор</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h2>4. Распространённые ошибки новичков</h2>\n" +
                        "    <p>Избегайте этих ловушек:</p>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        <p>\"Самая большая ошибка — это не начинать инвестировать из-за страха или незнания. Второй по величине ошибкой является попытка быстро разбогатеть.\"</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <ul>\n" +
                        "        <li><strong>Инвестирование последних денег:</strong> Вкладывайте только свободные средства.</li>\n" +
                        "        <li><strong>Погоня за трендами:</strong> Не покупайте активы только потому, что они сейчас популярны.</li>\n" +
                        "        <li><strong>Частая торговля:</strong> Избегайте спекуляций, особенно на начальном этапе.</li>\n" +
                        "        <li><strong>Игнорирование комиссий:</strong> Даже небольшие комиссии существенно снижают доходность в долгосрочной перспективе.</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h2>5. Полезные ресурсы для обучения</h2>\n" +
                        "    <p>Перед тем как инвестировать реальные деньги, изучите теорию:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Книги: \"Разумный инвестор\" Бенджамина Грэма, \"Психология инвестиций\" Карла Ричардса</li>\n" +
                        "        <li>Курсы на Coursera: \"Финансовые рынки\" от Yale University</li>\n" +
                        "        <li>Подкасты: \"Инвестиции на пальцах\", \"Финграм ТВ\"</li>\n" +
                        "        <li>Блоги: Investopedia (англ.), \"Т—Ж Инвестиции\"</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h2>Заключение</h2>\n" +
                        "    <p>Инвестиции — это марафон, а не спринт. Начинайте с малого, учитесь на ошибках (лучше на чужих), придерживайтесь стратегии и не поддавайтесь эмоциям. Даже небольшие, но регулярные инвестиции со временем могут превратиться в значительный капитал благодаря сложному проценту.</p>\n" +
                        "\n" +
                        "    <p>Главное — начать. Сегодня — лучшее время для этого!</p>\n" +
                        "</body>\n" +
                        "</html>\n",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Piggy%20bank,%20money%20stacks%20and%20coins.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL1BpZ2d5IGJhbmssIG1vbmV5IHN0YWNrcyBhbmQgY29pbnMucG5nIiwiaWF0IjoxNzQ5MjA0NjI0LCJleHAiOjE3ODA3NDA2MjR9._m6KX9KQ73lFsLs1ZqtGNs70isffMCSqoPhMFmfXnXE",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 9,
                title = "Как экономить без жестких ограничений",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Как экономить без жестких ограничений</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "            max-width: 800px;\n" +
                        "            margin: 0 auto;\n" +
                        "        }\n" +
                        "        h1 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 24px;\n" +
                        "            margin: 0 0 15px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h3 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 18px;\n" +
                        "            margin: 18px 0 10px 0;\n" +
                        "            font-weight: 600;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        .meta {\n" +
                        "            color: #666;\n" +
                        "            font-size: 14px;\n" +
                        "            margin-bottom: 20px;\n" +
                        "        }\n" +
                        "        .tags {\n" +
                        "            margin: 25px 0;\n" +
                        "        }\n" +
                        "        .tag {\n" +
                        "            display: inline-block;\n" +
                        "            background-color: #f0f0f0;\n" +
                        "            color: #25242E;\n" +
                        "            padding: 4px 10px;\n" +
                        "            border-radius: 15px;\n" +
                        "            margin-right: 8px;\n" +
                        "            font-size: 13px;\n" +
                        "        }\n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        .strategy {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 15px;\n" +
                        "            border-radius: 8px;\n" +
                        "            margin-bottom: 20px;\n" +
                        "        }\n" +
                        "        .saving-method {\n" +
                        "            margin-bottom: 25px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    \n" +
                        "    <p>Экономия часто ассоциируется с лишениями, но на самом деле это искусство разумного распределения ресурсов. В этой статье вы узнаете, как сократить расходы, сохранив качество жизни и даже получая от этого удовольствие.</p>\n" +
                        "    \n" +
                        "    <section class=\"highlight\">\n" +
                        "        <p>Исследования показывают: люди, которые экономят осознанно, а не через жесткие ограничения, в 3 раза чаще придерживаются своих финансовых планов и достигают целей.</p>\n" +
                        "    </section>\n" +
                        "\n" +
                        "    <h2>1. Психология разумной экономии</h2>\n" +
                        "    \n" +
                        "    <section class=\"strategy\">\n" +
                        "        <h3>Правило 20/80 в финансах</h3>\n" +
                        "        <p>80% результата дают 20% усилий. Применительно к экономии:</p>\n" +
                        "        <ul>\n" +
                        "            <li>Определите 20% статей расходов, которые съедают 80% бюджета</li>\n" +
                        "            <li>Сфокусируйтесь на оптимизации именно этих категорий</li>\n" +
                        "            <li>Не зацикливайтесь на мелочах, которые мало влияют на общую картину</li>\n" +
                        "        </ul>\n" +
                        "    </section>\n" +
                        "\n" +
                        "    <section class=\"strategy\">\n" +
                        "        <h3>Эффект \"Невидимых денег\"</h3>\n" +
                        "        <p>Наши мозги по-разному воспринимают:</p>\n" +
                        "        <table>\n" +
                        "            <tr>\n" +
                        "                <th>Тип платежа</th>\n" +
                        "                <th>Психологический эффект</th>\n" +
                        "                <th>Как использовать</th>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td>Наличные</td>\n" +
                        "                <td>Ощутимая потеря</td>\n" +
                        "                <td>Используйте для импульсных покупок</td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td>Бесконтактные платежи</td>\n" +
                        "                <td>Минимизирует чувство расставания с деньгами</td>\n" +
                        "                <td>Отключите для ненужных подписок</td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td>Автоплатежи</td>\n" +
                        "                <td>Полное отсутствие осознания траты</td>\n" +
                        "                <td>Используйте для накоплений</td>\n" +
                        "            </tr>\n" +
                        "        </table>\n" +
                        "    </section>\n" +
                        "\n" +
                        "    <h2>2. Практические методы без ограничений</h2>\n" +
                        "    \n" +
                        "    <section class=\"saving-method\">\n" +
                        "        <h3>Техника \"Финансовый буфер\"</h3>\n" +
                        "        <ol>\n" +
                        "            <li>Определите комфортную сумму на месяц</li>\n" +
                        "            <li>Все, что тратите меньше — отправляйте в отдельный \"буферный\" счет</li>\n" +
                        "            <li>Используйте накопленное для приятных покупок</li>\n" +
                        "        </ol>\n" +
                        "        <p>Это превращает экономию в игру с наградой, а не в ограничение.</p>\n" +
                        "    </section>\n" +
                        "\n" +
                        "    <section class=\"saving-method\">\n" +
                        "        <h3>Система \"Умных замен\"</h3>\n" +
                        "        <p>Не отказывайтесь, а находите более выгодные альтернативы:</p>\n" +
                        "        <ul>\n" +
                        "            <li>Кафе → Кулинарные мастер-классы</li>\n" +
                        "            <li>Такси → Каршеринг + прогулки</li>\n" +
                        "            <li>Новые вещи → Своп-вечеринки</li>\n" +
                        "        </ul>\n" +
                        "    </section>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        \"Богат не тот, кто много зарабатывает, а тот, кто умеет грамотно распоряжаться тем, что имеет\"\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>3. Автоматизация экономии</h2>\n" +
                        "    \n" +
                        "    <section class=\"strategy\">\n" +
                        "        <h3>Приложения и сервисы</h3>\n" +
                        "        <ul>\n" +
                        "            <li><strong>Кэшбек-сервисы</strong> — возвращайте до 30% от повседневных трат</li>\n" +
                        "            <li><strong>Анализ подписок</strong> — Truebill, Subby (автоматически находят ненужные платежи)</li>\n" +
                        "            <li><strong>Инвестиционные боты</strong> — округляйте покупки и автоматически инвестируйте сдачу</li>\n" +
                        "        </ul>\n" +
                        "    </section>\n" +
                        "\n" +
                        "    <h2>4. Финансовые привычки будущего</h2>\n" +
                        "    \n" +
                        "    <ol>\n" +
                        "        <li><strong>Еженедельный финансовый ритуал</strong> — 15 минут на проверку счетов и бюджет</li>\n" +
                        "        <li><strong>Правило 24 часов</strong> — для покупок свыше 5 000 руб.</li>\n" +
                        "        <li><strong>Экономия \"на опережение\"</strong> — перед повышением зарплаты увеличьте процент отложений</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <section class=\"highlight\">\n" +
                        "        <p>Начните с одного метода в неделю. Через месяц вы увидите реальные результаты без стресса и ограничений. Помните: лучшая экономия — это осознанные траты, а не тотальная бережливость.</p>\n" +
                        "    </section>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Wealthy%20woman%20sitting%20on%20a%20safe.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL1dlYWx0aHkgd29tYW4gc2l0dGluZyBvbiBhIHNhZmUucG5nIiwiaWF0IjoxNzQ5MjA0Njg1LCJleHAiOjE3ODA3NDA2ODV9.Kce643MV3BqBwXzfDsOk4DKsrjxI0aooIt_EPAQQDk0",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 10,
                title = "Навыки будущего: чему учиться сейчас",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Навыки будущего: чему учиться сейчас</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "            max-width: 800px;\n" +
                        "            margin: 0 auto;\n" +
                        "        }\n" +
                        "        h1 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 24px;\n" +
                        "            margin: 0 0 15px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h3 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 18px;\n" +
                        "            margin: 18px 0 10px 0;\n" +
                        "            font-weight: 600;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        .meta {\n" +
                        "            color: #666;\n" +
                        "            font-size: 14px;\n" +
                        "            margin-bottom: 20px;\n" +
                        "        }\n" +
                        "        .tags {\n" +
                        "            margin: 25px 0;\n" +
                        "        }\n" +
                        "        .tag {\n" +
                        "            display: inline-block;\n" +
                        "            background-color: #f0f0f0;\n" +
                        "            color: #25242E;\n" +
                        "            padding: 4px 10px;\n" +
                        "            border-radius: 15px;\n" +
                        "            margin-right: 8px;\n" +
                        "            font-size: 13px;\n" +
                        "        }\n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        .skill-category {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 15px;\n" +
                        "            border-radius: 8px;\n" +
                        "            margin-bottom: 20px;\n" +
                        "        }\n" +
                        "        .future-trend {\n" +
                        "            margin-bottom: 25px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <p>Мир меняется беспрецедентными темпами: по данным Всемирного экономического форума, 85% профессий 2030 года еще не существуют. В этой статье мы разберем ключевые навыки, которые обеспечат вам конкурентоспособность в ближайшее десятилетие.</p>\n" +
                        "    \n" +
                        "    <section class=\"highlight\">\n" +
                        "        <p>Исследование McKinsey показало: к 2030 году автоматизация затронет до 375 миллионов работников, но одновременно создаст новые возможности для тех, кто готов адаптироваться.</p>\n" +
                        "    </section>\n" +
                        "\n" +
                        "    <h2>1. Технологические навыки нового поколения</h2>\n" +
                        "    \n" +
                        "    <section class=\"skill-category\">\n" +
                        "        <h3>Искусственный интеллект и машинное обучение</h3>\n" +
                        "        <ul>\n" +
                        "            <li><strong>Основы работы с ИИ</strong> - не только использование ChatGPT, но и понимание его ограничений</li>\n" +
                        "            <li><strong>Анализ данных</strong> - Python, SQL, визуализация данных</li>\n" +
                        "            <li><strong>Автоматизация процессов</strong> - создание простых ботов и скриптов</li>\n" +
                        "        </ul>\n" +
                        "    </section>\n" +
                        "\n" +
                        "    <section class=\"skill-category\">\n" +
                        "        <h3>Квантовые вычисления и блокчейн</h3>\n" +
                        "        <ul>\n" +
                        "            <li><strong>Основы квантовой механики</strong> для понимания новых технологий</li>\n" +
                        "            <li><strong>Смарт-контракты</strong> и децентрализованные приложения</li>\n" +
                        "            <li><strong>Цифровая безопасность</strong> в условиях новых угроз</li>\n" +
                        "        </ul>\n" +
                        "    </section>\n" +
                        "\n" +
                        "    <h2>2. Критически важные soft skills</h2>\n" +
                        "    \n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Навык</th>\n" +
                        "            <th>Почему важен</th>\n" +
                        "            <th>Как развивать</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Эмоциональный интеллект</td>\n" +
                        "            <td>Автоматизация делает \"человеческие\" качества ценнее</td>\n" +
                        "            <td>Практика активного слушания, рефлексия</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Кросс-культурная коммуникация</td>\n" +
                        "            <td>Глобализация требует работы в международных командах</td>\n" +
                        "            <td>Изучение языков, культурные обмены</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Креативное мышление</td>\n" +
                        "            <td>Роботы пока не умеют по-настоящему творить</td>\n" +
                        "            <td>Дизайн-мышление, мозговые штурмы</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        \"В будущем работодатели будут ценить не дипломы, а способность быстро осваивать новые навыки и адаптироваться к изменениям\"\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>3. Навыки для цифровой экономики</h2>\n" +
                        "    \n" +
                        "    <section class=\"future-trend\">\n" +
                        "        <h3>Персональный брендинг в цифровой среде</h3>\n" +
                        "        <ul>\n" +
                        "            <li>Создание профессионального онлайн-присутствия</li>\n" +
                        "            <li>Управление репутацией в соцсетях</li>\n" +
                        "            <li>Эффектная самопрезентация</li>\n" +
                        "        </ul>\n" +
                        "    </section>\n" +
                        "\n" +
                        "    <section class=\"future-trend\">\n" +
                        "        <h3>Управление цифровым благополучием</h3>\n" +
                        "        <ul>\n" +
                        "            <li>Баланс онлайн/оффлайн жизни</li>\n" +
                        "            <li>Защита от цифрового выгорания</li>\n" +
                        "            <li>Критическое восприятие информации</li>\n" +
                        "        </ul>\n" +
                        "    </section>\n" +
                        "\n" +
                        "    <h2>4. Практическое руководство по освоению</h2>\n" +
                        "    \n" +
                        "    <ol>\n" +
                        "        <li><strong>Составьте карту навыков</strong> - оцените текущий уровень и приоритеты</li>\n" +
                        "        <li><strong>Используйте микрообучение</strong> - 30-40 минут в день на курсы</li>\n" +
                        "        <li><strong>Создайте \"песочницу\"</strong> - тестовые проекты для практики</li>\n" +
                        "        <li><strong>Найдите ментора</strong> в интересующей области</li>\n" +
                        "        <li><strong>Применяйте сразу</strong> - ищите возможности использовать новые знания</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <section class=\"highlight\">\n" +
                        "        <p>Начните с одного навыка в месяц. Через год вы будете обладать 12 востребованными компетенциями, что сделает вас ценным специалистом в любой сфере.</p>\n" +
                        "    </section>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>\n",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Artificial%20intelligence%20and%20machine%20learning.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL0FydGlmaWNpYWwgaW50ZWxsaWdlbmNlIGFuZCBtYWNoaW5lIGxlYXJuaW5nLnBuZyIsImlhdCI6MTc0OTIwNDQ0MywiZXhwIjoxNzgwNzQwNDQzfQ.-NsWs5p_wfVlZ6vgFXaztBtxtiON9EWU201KG-dpQSw",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 11,
                title = "Искусство small talk: как не молчать в компании",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Искусство small talk: как не молчать в компании</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "            max-width: 800px;\n" +
                        "            margin: 0 auto;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        .technique {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 15px;\n" +
                        "            border-radius: 8px;\n" +
                        "            margin-bottom: 20px;\n" +
                        "        }\n" +
                        "        .conflict-type {\n" +
                        "            margin-bottom: 30px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    \n" +
                        "    <p>Неловкие паузы в разговоре знакомы каждому. Но small talk — это не просто \"болтовня\", а важный социальный навык, который можно освоить. Вот полное руководство по легкому общению в любой ситуации.</p>\n" +
                        "    \n" +
                        "    <section class=\"highlight\">\n" +
                        "        <p>Исследования показывают: 92% людей испытывают дискомфорт от неловкого молчания в беседе, при этом 68% хотели бы улучшить свои навыки светской беседы.</p>\n" +
                        "    </section>\n" +
                        "    \n" +
                        "    <h2>1. Почему small talk так важен?</h2>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Создает первое впечатление</strong> — 38% симпатии формируется в первые 5 минут общения</li>\n" +
                        "        <li><strong>Открывает возможности</strong> — многие деловые контакты начинаются с неформальной беседы</li>\n" +
                        "        <li><strong>Снимает напряжение</strong> — заполняет паузы в незнакомой компании</li>\n" +
                        "    </ul>\n" +
                        "    \n" +
                        "    <h2>2. Универсальные темы для начала</h2>\n" +
                        "    \n" +
                        "    <section class=\"technique\">\n" +
                        "        <h3>Метод FORM</h3>\n" +
                        "        <p>Легко запоминающаяся структура для начала разговора:</p>\n" +
                        "        <ul>\n" +
                        "            <li><strong>F</strong>amily (семья) — \"Откуда ваша семья?\"</li>\n" +
                        "            <li><strong>O</strong>ccupation (работа) — \"Чем вы увлекаетесь помимо работы?\"</li>\n" +
                        "            <li><strong>R</strong>ecreation (отдых) — \"Как предпочитаете проводить выходные?\"</li>\n" +
                        "            <li><strong>M</strong>otivation (мотивация) — \"Что вас вдохновляет в вашем деле?\"</li>\n" +
                        "        </ul>\n" +
                        "    </section>\n" +
                        "    \n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Ситуация</th>\n" +
                        "            <th>Примеры фраз</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>На вечеринке</td>\n" +
                        "            <td>\"Как вы познакомились с хозяевами?\", \"Что думаете о музыке?\"</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Деловая встреча</td>\n" +
                        "            <td>\"Как добрались?\", \"Какие впечатления от мероприятия?\"</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Ожидание в очереди</td>\n" +
                        "            <td>\"Вы тоже впервые здесь?\", \"Как вам эта погода?\"</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "    \n" +
                        "    <h2>3. Техники поддержания разговора</h2>\n" +
                        "    \n" +
                        "    <section class=\"technique\">\n" +
                        "        <h3>Правило \"3 нити\"</h3>\n" +
                        "        <p>В каждом ответе собеседника находите 3 возможных направления для продолжения:</p>\n" +
                        "        <p><em>— Я вчера ходил в новый ресторан.</em></p>\n" +
                        "        <ol>\n" +
                        "            <li>О еде: \"Какое блюдо вам запомнилось?\"</li>\n" +
                        "            <li>О месте: \"Где находится этот ресторан?\"</li>\n" +
                        "            <li>О компании: \"С кем вы ходили?\"</li>\n" +
                        "        </ol>\n" +
                        "    </section>\n" +
                        "    \n" +
                        "    <div class=\"quote\">\n" +
                        "        \"Хороший собеседник — не тот, кто много говорит, а тот, кто умеет слушать и задавать правильные вопросы\"\n" +
                        "    </div>\n" +
                        "    \n" +
                        "    <h2>4. Как избежать неловких пауз</h2>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Подготовьте 5 универсальных вопросов</strong> на разные случаи</li>\n" +
                        "        <li><strong>Используйте технику \"А что насчет...\"</strong> — плавный переход между темами</li>\n" +
                        "        <li><strong>Держите в запасе 3 интересные истории</strong> из своей жизни</li>\n" +
                        "        <li><strong>Наблюдайте за окружением</strong> — детали интерьера или события могут стать темой</li>\n" +
                        "    </ul>\n" +
                        "    \n" +
                        "    <h2>5. Что делать, если разговор не клеится</h2>\n" +
                        "    <ol>\n" +
                        "        <li>Смените тему — \"Кстати, вы слышали о...\"</li>\n" +
                        "        <li>Признайте ситуацию с юмором — \"Кажется, мы исчерпали все темы про погоду\"</li>\n" +
                        "        <li>Вежливо извинитесь — \"Мне нужно поздороваться со знакомым\"</li>\n" +
                        "    </ol>\n" +
                        "    \n" +
                        "    <section class=\"highlight\">\n" +
                        "        <p>Практикуйтесь ежедневно: начинайте разговоры с бариста, соседями, коллегами. Через 2-3 недели small talk станет естественной частью общения.</p>\n" +
                        "    </section>\n" +
                        "</body>\n" +
                        "</html>\n",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/People%20shopping%20for%20christmas%20presents.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL1Blb3BsZSBzaG9wcGluZyBmb3IgY2hyaXN0bWFzIHByZXNlbnRzLnBuZyIsImlhdCI6MTc0OTIwNTI3OCwiZXhwIjoxNzgwNzQxMjc4fQ.zlmm9dtufA43PltQ1V8PZViw_rAkz3SJXpbDpXKk4co",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 12,
                title = "Конфликты в семье: как решать без ссор",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Конфликты в семье: как решать без ссор</title>\n" +
                        "    <style>\n" +
                        "    @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        \n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        \n" +
                        "        \n" +
                        "        .technique {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 15px;\n" +
                        "            border-radius: 8px;\n" +
                        "            margin-bottom: 20px;\n" +
                        "        }\n" +
                        "        .conflict-type {\n" +
                        "            margin-bottom: 30px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <p>Семейные конфликты неизбежны, но их можно превратить в возможность для роста отношений. Разберём практические методы, которые помогут сохранить гармонию в семье.</p>\n" +
                        "    \n" +
                        "    <section class=\"highlight\">\n" +
                        "        <p>По данным психологических исследований, 68% семейных конфликтов возникают из-за недопонимания, а не реальных разногласий.</p>\n" +
                        "    </section>\n" +
                        "    \n" +
                        "    <h2>1. Типы семейных конфликтов и их причины</h2>\n" +
                        "    \n" +
                        "    <section class=\"conflict-type\">\n" +
                        "        <h3>Бытовые конфликты</h3>\n" +
                        "        <p>Невымытая посуда, разбросанные вещи, несоблюдение договорённостей. Кажутся мелочами, но накапливаются как снежный ком.</p>\n" +
                        "    </section>\n" +
                        "    \n" +
                        "    <section class=\"conflict-type\">\n" +
                        "        <h3>Воспитательные разногласия</h3>\n" +
                        "        <p>Разные подходы к воспитанию детей, несогласованность запретов и поощрений между родителями.</p>\n" +
                        "    </section>\n" +
                        "    \n" +
                        "    <section class=\"conflict-type\">\n" +
                        "        <h3>Финансовые споры</h3>\n" +
                        "        <p>Разное отношение к деньгам, недовольство тратами партнёра, скрытые покупки.</p>\n" +
                        "    </section>\n" +
                        "    \n" +
                        "    <h2>2. Стратегии решения конфликтов</h2>\n" +
                        "    \n" +
                        "    <section class=\"technique\">\n" +
                        "        <h3>Метод \"Я-сообщений\"</h3>\n" +
                        "        <p>Формула: <strong>\"Я чувствую... Когда... Потому что... Я бы хотел...\"</strong></p>\n" +
                        "        <p>Пример: \"Я расстраиваюсь, когда опаздываю на работу, потому что не могу найти ключи. Давай договоримся класть их на полку у двери.\"</p>\n" +
                        "    </section>\n" +
                        "    \n" +
                        "    <section class=\"technique\">\n" +
                        "        <h3>Техника активного слушания</h3>\n" +
                        "        <ol>\n" +
                        "            <li>Полностью сосредоточьтесь на говорящем</li>\n" +
                        "            <li>Не перебивайте</li>\n" +
                        "            <li>Повторяйте своими словами услышанное (\"Правильно ли я понимаю, что...\")</li>\n" +
                        "            <li>Уточняйте (\"Что ты имеешь в виду под...\")</li>\n" +
                        "        </ol>\n" +
                        "    </section>\n" +
                        "    \n" +
                        "    <div class=\"quote\">\n" +
                        "        \"В счастливых семьях не бывает меньше конфликтов, они просто умеют их правильно решать\"\n" +
                        "    </div>\n" +
                        "    \n" +
                        "    <h2>3. Практические шаги при конфликте</h2>\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Шаг</th>\n" +
                        "            <th>Действия</th>\n" +
                        "            <th>Чего избегать</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>1. Остановка</td>\n" +
                        "            <td>Сделать паузу, глубоко вдохнуть</td>\n" +
                        "            <td>Криков, оскорблений</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>2. Анализ</td>\n" +
                        "            <td>Определить истинную причину конфликта</td>\n" +
                        "            <td>Перехода на личности</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>3. Диалог</td>\n" +
                        "            <td>Выслушать позицию другого</td>\n" +
                        "            <td>Монолога</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>4. Решение</td>\n" +
                        "            <td>Найти компромисс</td>\n" +
                        "            <td>Ультиматумов</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "    \n" +
                        "    <h2>4. Профилактика конфликтов</h2>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Семейные советы</strong> - регулярные встречи для обсуждения вопросов</li>\n" +
                        "        <li><strong>Личное пространство</strong> - у каждого должно быть место и время для себя</li>\n" +
                        "        <li><strong>Ритуалы примирения</strong> - установленные способы помириться после ссоры</li>\n" +
                        "        <li><strong>Благодарность</strong> - ежедневно отмечать хорошее в партнёре</li>\n" +
                        "    </ul>\n" +
                        "    \n" +
                        "    <h2>5. Когда нужна помощь специалиста</h2>\n" +
                        "    <p>Обратитесь к семейному психологу, если:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Конфликты повторяются по одному сценарию</li>\n" +
                        "        <li>Вы не можете разговаривать без крика</li>\n" +
                        "        <li>Появились симптомы депрессии у кого-то из членов семьи</li>\n" +
                        "        <li>Конфликты влияют на детей</li>\n" +
                        "    </ul>\n" +
                        "    \n" +
                        "    <section class=\"highlight\">\n" +
                        "        <p>Помните: конфликт — это не война, а сигнал о необходимости изменений. Используйте его как возможность лучше понять друг друга и укрепить отношения.</p>\n" +
                        "    </section>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>\n",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/image.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL2ltYWdlLnBuZyIsImlhdCI6MTc0OTIwNDg1OCwiZXhwIjoxNzgwNzQwODU4fQ.la6hMTlMejLPYg0Jnz0lXLQE235COEfgsCF0JZZA5xo",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 13,
                title = "Как защитить личные данные в интернете",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Как защитить личные данные в интернете</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h3 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 18px;\n" +
                        "            margin: 18px 0 10px 0;\n" +
                        "            font-weight: 500;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        \n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        .security-step {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 15px;\n" +
                        "            border-radius: 8px;\n" +
                        "            margin-bottom: 15px;\n" +
                        "        }\n" +
                        "        .step-title {\n" +
                        "            font-weight: 500;\n" +
                        "            color: #25242E;\n" +
                        "            margin-bottom: 8px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "    <p>В 2025 году защита персональных данных стала критически важным навыком для каждого пользователя интернета. Утечки информации, фишинг и цифровое мошенничество становятся всё изощрённее. В этой статье — полное руководство по защите ваших цифровых следов, финансовой информации и личных файлов.</p>\n" +
                        "\n" +
                        "    <h2>1. Основные угрозы в 2025 году</h2>\n" +
                        "    <p>Современные киберриски, о которых нужно знать:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>ИИ-фишинг:</strong> Персонализированные мошеннические сообщения, генерируемые нейросетями</li>\n" +
                        "        <li><strong>Биометрический взлом:</strong> Кража цифровых отпечатков пальцев и сканов лица</li>\n" +
                        "        <li><strong>Умные устройства-шпионы:</strong> Компрометация данных через IoT-гаджеты</li>\n" +
                        "        <li><strong>Криптоджекинг:</strong> Использование вашего устройства для майнинга без согласия</li>\n" +
                        "        <li><strong>Метавселенские угрозы:</strong> Кража цифровых активов в виртуальных пространствах</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Статистика:</strong> В 2024 году 73% пользователей стали жертвами хотя бы одной формы кибератаки, а средний ущерб от утечки данных составил \$4.5 млн для компаний.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>2. Базовые меры защиты</h2>\n" +
                        "\n" +
                        "    <div class=\"security-step\">\n" +
                        "        <p class=\"step-title\">1. Надёжные пароли</p>\n" +
                        "        <p>Используйте менеджер паролей (Bitwarden, 1Password) и создавайте уникальные комбинации:</p>\n" +
                        "        <ul>\n" +
                        "            <li>Минимум 12 символов</li>\n" +
                        "            <li>Буквы верхнего/нижнего регистра, цифры, спецсимволы</li>\n" +
                        "            <li>Никаких личных данных или простых последовательностей</li>\n" +
                        "        </ul>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"security-step\">\n" +
                        "        <p class=\"step-title\">2. Двухфакторная аутентификация (2FA)</p>\n" +
                        "        <p>Всегда включайте 2FA, предпочтительно через приложение (Google Authenticator, Authy), а не SMS.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"security-step\">\n" +
                        "        <p class=\"step-title\">3. Обновления ПО</p>\n" +
                        "        <p>Включайте автоматические обновления для всех устройств и приложений — 85% атак используют известные уязвимости.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Уровень защиты</th>\n" +
                        "            <th>Риск взлома</th>\n" +
                        "            <th>Рекомендации</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Базовый (пароль)</td>\n" +
                        "            <td>Высокий</td>\n" +
                        "            <td>Минимум для неважных аккаунтов</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Средний (пароль + SMS 2FA)</td>\n" +
                        "            <td>Средний</td>\n" +
                        "            <td>Для почты и соцсетей</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Продвинутый (менеджер паролей + app 2FA)</td>\n" +
                        "            <td>Низкий</td>\n" +
                        "            <td>Для финансовых и рабочих аккаунтов</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Максимальный (аппаратные ключи)</td>\n" +
                        "            <td>Минимальный</td>\n" +
                        "            <td>Для критически важных данных</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h2>3. Финансовая безопасность</h2>\n" +
                        "    <p>Как защитить деньги и платежи:</p>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        <p>\"Цифровая гигиена — это новая финансовая грамотность. Ваши данные стоят денег — не раздавайте их бесплатно.\"</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <ol>\n" +
                        "        <li><strong>Виртуальные карты:</strong> Используйте для онлайн-платежей одноразовые или лимитированные карты</li>\n" +
                        "        <li><strong>Отдельная почта:</strong> Заведите отдельный email для финансовых операций</li>\n" +
                        "        <li><strong>Мониторинг транзакций:</strong> Включите уведомления о всех операциях</li>\n" +
                        "        <li><strong>Биометрия:</strong> Где возможно, используйте Face ID/Touch ID вместо паролей</li>\n" +
                        "        <li><strong>Криптозащита:</strong> Для крупных сумм рассмотрите аппаратные кошельки</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <h2>4. Конфиденциальность в соцсетях</h2>\n" +
                        "    <p>Настройки, которые нужно проверить прямо сейчас:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Ограничьте видимость профиля</strong> только для друзей</li>\n" +
                        "        <li><strong>Отключите индексацию в поисковиках</strong></li>\n" +
                        "        <li><strong>Удалите избыточные данные</strong> (старые посты, геометки)</li>\n" +
                        "        <li><strong>Проверьте подключённые приложения</strong> и отзовите ненужные доступы</li>\n" +
                        "        <li><strong>Используйте псевдонимы</strong> где это возможно</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Важно:</strong> Регулярно проверяйте, какие данные о вас доступны в интернете через сервисы вроде Have I Been Pwned или Google \"Мои действия\".</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>5. Продвинутые методы защиты</h2>\n" +
                        "    <p>Для тех, кто хочет максимальной безопасности:</p>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Технология</th>\n" +
                        "            <th>Применение</th>\n" +
                        "            <th>Сложность</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>VPN</td>\n" +
                        "            <td>Шифрование интернет-трафика</td>\n" +
                        "            <td>Низкая</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Аппаратные ключи (YubiKey)</td>\n" +
                        "            <td>Физическая 2FA</td>\n" +
                        "            <td>Средняя</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Виртуальные машины</td>\n" +
                        "            <td>Изолированная среда для рискованных операций</td>\n" +
                        "            <td>Высокая</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Шифрование диска</td>\n" +
                        "            <td>Защита данных при потере устройства</td>\n" +
                        "            <td>Средняя</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h2>Заключение</h2>\n" +
                        "    <p>Защита личных данных — это не разовое действие, а постоянный процесс. Начните с базовых мер (менеджер паролей, 2FA), постепенно внедряйте более сложные методы. Помните: ваша цифровая безопасность на 90% зависит от привычек, а не от технологий.</p>\n" +
                        "\n" +
                        "    <p>Выделите 1 час в месяц на \"цифровую гигиену\" — проверку настроек, обновление ПО, аудит доступов. Эти небольшие инвестиции времени защитят вас от огромных проблем в будущем.</p>\n" +
                        "</body>\n" +
                        "</html>\n",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Man%20with%20shield%20protecting%20data%20in%20laptop.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL01hbiB3aXRoIHNoaWVsZCBwcm90ZWN0aW5nIGRhdGEgaW4gbGFwdG9wLnBuZyIsImlhdCI6MTc0OTIwNDU2OCwiZXhwIjoxNzgwNzQwNTY4fQ.x8O9Ywmr4EnngTWyM0Hf7RqiONhYF4M3bZ4giWp1Qic",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 14,
                title = "Гаджеты для здоровья: что действительно работает",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Гаджеты для здоровья: что действительно работает</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h3 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 18px;\n" +
                        "            margin: 18px 0 10px 0;\n" +
                        "            font-weight: 500;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        \n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        .gadget-review {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 15px;\n" +
                        "            border-radius: 8px;\n" +
                        "            margin-bottom: 15px;\n" +
                        "        }\n" +
                        "        .gadget-title {\n" +
                        "            font-weight: 500;\n" +
                        "            color: #25242E;\n" +
                        "            margin-bottom: 8px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    \n" +
                        "\n" +
                        "    <p>Рынок health-tech устройств переживает бум: новые гаджеты появляются ежемесячно, но лишь немногие действительно улучшают здоровье. Мы протестировали десятки устройств и отобрали только те, чья эффективность подтверждена исследованиями и реальными отзывами врачей.</p>\n" +
                        "\n" +
                        "    <h2>1. Проверенные временем гаджеты</h2>\n" +
                        "    <p>Устройства с доказанной эффективностью:</p>\n" +
                        "\n" +
                        "    <div class=\"gadget-review\">\n" +
                        "        <p class=\"gadget-title\">1. Умные весы с анализом состава тела</p>\n" +
                        "        <p>Лучшие модели (Withings, Tanita) измеряют не только вес, но и процент жира, мышечной массы, воды и даже костной ткани. Особенно полезны при:</p>\n" +
                        "        <ul>\n" +
                        "            <li>Контроле прогресса в фитнесе</li>\n" +
                        "            <li>Выявлении скрытых отёков</li>\n" +
                        "            <li>Мониторинге возрастных изменений</li>\n" +
                        "        </ul>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"gadget-review\">\n" +
                        "        <p class=\"gadget-title\">2. Пульсоксиметры</p>\n" +
                        "        <p>После пандемии эти устройства должны быть в каждой аптечке. Хорошие модели (Beurer, Wellue) показывают:</p>\n" +
                        "        <ul>\n" +
                        "            <li>Уровень кислорода в крови (SpO2)</li>\n" +
                        "            <li>Частоту пульса</li>\n" +
                        "            <li>Некоторые — вариабельность сердечного ритма</li>\n" +
                        "        </ul>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"gadget-review\">\n" +
                        "        <p class=\"gadget-title\">3. Датчики сна (Oura Ring, Withings Sleep)</p>\n" +
                        "        <p>Анализируют фазы сна, частоту дыхания и движения. Помогают выявить:</p>\n" +
                        "        <ul>\n" +
                        "            <li>Апноэ во сне</li>\n" +
                        "            <li>Нарушения циркадных ритмов</li>\n" +
                        "            <li>Влияние образа жизни на качество сна</li>\n" +
                        "        </ul>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Гаджет</th>\n" +
                        "            <th>Точность</th>\n" +
                        "            <th>Польза</th>\n" +
                        "            <th>Ценовой диапазон</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Умные весы</td>\n" +
                        "            <td>85-95%</td>\n" +
                        "            <td>Контроль состава тела</td>\n" +
                        "            <td>5-25 тыс. руб.</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Пульсоксиметр</td>\n" +
                        "            <td>95-98%</td>\n" +
                        "            <td>Мониторинг дыхания</td>\n" +
                        "            <td>2-10 тыс. руб.</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Трекер сна</td>\n" +
                        "            <td>80-90%</td>\n" +
                        "            <td>Анализ качества сна</td>\n" +
                        "            <td>10-40 тыс. руб.</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>ЭКГ-гаджеты</td>\n" +
                        "            <td>90-95%</td>\n" +
                        "            <td>Выявление аритмии</td>\n" +
                        "            <td>8-30 тыс. руб.</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h2>2. Новинки 2025 года с потенциалом</h2>\n" +
                        "    <p>Перспективные, но требующие дополнительных исследований:</p>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        <p>\"Технологии должны не заменять врачей, а помогать нам лучше понимать своё тело.\" — доктор Иван Петров, кардиолог</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <ol>\n" +
                        "        <li><strong>Носимые УЗИ-сканеры:</strong> Портативные устройства для самостоятельного обследования органов</li>\n" +
                        "        <li><strong>Глюкометры без прокола:</strong> Используют спектроскопию для измерения сахара</li>\n" +
                        "        <li><strong>ДНК-анализаторы:</strong> Мини-лаборатории для домашнего генетического тестирования</li>\n" +
                        "        <li><strong>Нейростимуляторы:</strong> Устройства для улучшения когнитивных функций</li>\n" +
                        "        <li><strong>ИИ-ассистенты здоровья:</strong> Анализируют все показатели и дают персональные рекомендации</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <h2>3. Как выбрать полезный гаджет</h2>\n" +
                        "    <p>Критерии выбора:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Наличие медицинской сертификации:</strong> FDA, CE или Росздравнадзора</li>\n" +
                        "        <li><strong>Подтверждённые исследования:</strong> Ищите ссылки на клинические испытания</li>\n" +
                        "        <li><strong>Совместимость:</strong> Возможность экспорта данных к врачу</li>\n" +
                        "        <li><strong>Открытость алгоритмов:</strong> Как именно рассчитываются показатели</li>\n" +
                        "        <li><strong>Реальные отзывы:</strong> Не маркетинговые истории, а опыт реальных пользователей</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Важно:</strong> Ни один гаджет не заменит консультацию врача. Используйте устройства для мониторинга и профилактики, но не для самодиагностики.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>4. ТОП-5 бюджетных гаджетов</h2>\n" +
                        "    <p>Эффективные устройства до 10 000 рублей:</p>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Гаджет</th>\n" +
                        "            <th>Цена</th>\n" +
                        "            <th>Что измеряет</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Xiaomi Mi Band 8</td>\n" +
                        "            <td>4 990 руб.</td>\n" +
                        "            <td>Пульс, сон, активность</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Beurer PO 30</td>\n" +
                        "            <td>3 490 руб.</td>\n" +
                        "            <td>Кислород в крови, пульс</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Medisana MTS</td>\n" +
                        "            <td>6 790 руб.</td>\n" +
                        "            <td>Температура, пульс, ЭКГ</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Omron M3 Expert</td>\n" +
                        "            <td>9 990 руб.</td>\n" +
                        "            <td>Давление, аритмия</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Yamay Smart Watch</td>\n" +
                        "            <td>5 590 руб.</td>\n" +
                        "            <td>Сон, SpO2, активность</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h2>Заключение</h2>\n" +
                        "    <p>Правильно подобранные health-гаджеты могут стать вашими надёжными помощниками в заботе о здоровье. Начните с базовых устройств (трекер активности, пульсоксиметр), постепенно добавляя более специализированные, если есть потребность.</p>\n" +
                        "\n" +
                        "    <p>Помните: главное не количество показателей, а регулярность их отслеживания и грамотная интерпретация. Лучший гаджет — тот, которым вы действительно пользуетесь.</p>\n" +
                        "</body>\n" +
                        "</html>\n",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Medical%20app%20on%20tablet.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL01lZGljYWwgYXBwIG9uIHRhYmxldC5wbmciLCJpYXQiOjE3NDkyMDQ2MTEsImV4cCI6MTc4MDc0MDYxMX0.L3OUrPDNuz1pzHq-TIcs8Vzo6tvdBQB5fTApnuDeZEE",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 15,
                title = "Как путешествовать дешево и комфортно",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Как путешествовать дешево и комфортно</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h3 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 18px;\n" +
                        "            margin: 18px 0 10px 0;\n" +
                        "            font-weight: 500;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        \n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        .travel-tip {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 15px;\n" +
                        "            border-radius: 8px;\n" +
                        "            margin-bottom: 15px;\n" +
                        "        }\n" +
                        "        .tip-title {\n" +
                        "            font-weight: 500;\n" +
                        "            color: #25242E;\n" +
                        "            margin-bottom: 8px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <p>Путешествия не должны быть роскошью — даже с ограниченным бюджетом можно увидеть мир без лишних жертв. В этой статье собраны проверенные лайфхаки, которые помогут вам сэкономить до 50% на поездках, сохранив при этом комфорт и впечатления.</p>\n" +
                        "\n" +
                        "    <h2>1. Планирование — основа экономии</h2>\n" +
                        "    <p>90% экономии закладывается на этапе подготовки:</p>\n" +
                        "\n" +
                        "    <div class=\"travel-tip\">\n" +
                        "        <p class=\"tip-title\">1. Гибкие даты</p>\n" +
                        "        <p>Используйте матрицы цен (Google Flights, Skyscanner) для поиска самых дешевых дней вылета. Разница между соседними датами может достигать 40%.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"travel-tip\">\n" +
                        "        <p class=\"tip-title\">2. Альтернативные аэропорты</p>\n" +
                        "        <p>Рейсы в соседние города часто значительно дешевле. Например, вместо Парижа рассмотрите Брюссель (всего 1,5 часа на поезде).</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"travel-tip\">\n" +
                        "        <p class=\"tip-title\">3. Сезонность</p>\n" +
                        "        <p>\"Плечевые сезоны\" (апрель-май, сентябрь-октябрь) предлагают лучшие цены при хорошей погоде.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Стратегия</th>\n" +
                        "            <th>Экономия</th>\n" +
                        "            <th>Комфорт</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Раннее бронирование</td>\n" +
                        "            <td>20-30%</td>\n" +
                        "            <td>★★★★★</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Last minute</td>\n" +
                        "            <td>40-60%</td>\n" +
                        "            <td>★★★☆☆</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Сложные маршруты</td>\n" +
                        "            <td>30-50%</td>\n" +
                        "            <td>★★☆☆☆</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Лоукостеры</td>\n" +
                        "            <td>50-70%</td>\n" +
                        "            <td>★★★☆☆</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h2>2. Транспорт: как сэкономить</h2>\n" +
                        "    <p>Самые эффективные способы:</p>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        <p>\"Путешествие на тысячу миль начинается с одного шага... и поиска дешевых билетов.\"</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <ol>\n" +
                        "        <li><strong>Авиабилеты:</strong> Подписки на ошибки тарифов (Secret Flying, Jack's Flight Club)</li>\n" +
                        "        <li><strong>Поезда:</strong> Межрегиональные проездные (Eurail, JR Pass)</li>\n" +
                        "        <li><strong>Автобусы:</strong> Ночные рейсы экономят на проживании (FlixBus, Megabus)</li>\n" +
                        "        <li><strong>Каршеринг:</strong> Локальные сервисы вместо международных брендов</li>\n" +
                        "        <li><strong>Велосипеды:</strong> Бесплатный городской транспорт в 200+ городах мира</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <h2>3. Проживание: комфорт за меньшие деньги</h2>\n" +
                        "    <p>Альтернативы отелям:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Гостевые дома:</strong> В 2-3 раза дешевле при том же уровне сервиса</li>\n" +
                        "        <li><strong>House sitting:</strong> Бесплатное проживание в обмен на присмотр за домом</li>\n" +
                        "        <li><strong>Монастыри:</strong> Уникальный опыт за символическую плату (Италия, Греция)</li>\n" +
                        "        <li><strong>Капсульные отели:</strong> Чисто и функционально (Япония, Сингапур)</li>\n" +
                        "        <li><strong>Workaway:</strong> Проживание в обмен на 4-5 часов работы в день</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Совет:</strong> Бронируя жилье, всегда проверяйте цены напрямую на сайте отеля/хостела — часто они ниже, чем на агрегаторах.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>4. Питание: вкусно и недорого</h2>\n" +
                        "    <p>Как питаться в путешествиях без ущерба для бюджета и желудка:</p>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Стратегия</th>\n" +
                        "            <th>Экономия</th>\n" +
                        "            <th>Примеры</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Уличная еда</td>\n" +
                        "            <td>70-80%</td>\n" +
                        "            <td>Тайские рынки, турецкие кебабы</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Ланчи бизнес-меню</td>\n" +
                        "            <td>50-60%</td>\n" +
                        "            <td>Италия, Испания, Япония</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Продуктовые рынки</td>\n" +
                        "            <td>60-70%</td>\n" +
                        "            <td>Франция, Турция, Марокко</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Готовим сами</td>\n" +
                        "            <td>80-90%</td>\n" +
                        "            <td>Аренда жилья с кухней</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h2>5. Бесплатные развлечения</h2>\n" +
                        "    <p>Как наполнить поездку впечатлениями без затрат:</p>\n" +
                        "\n" +
                        "    <div class=\"travel-tip\">\n" +
                        "        <p class=\"tip-title\">1. Бесплатные дни музеев</p>\n" +
                        "        <p>Большинство музеев мира предлагают бесплатный вход 1-2 дня в месяц.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"travel-tip\">\n" +
                        "        <p class=\"tip-title\">2. Пешие экскурсии с местными</p>\n" +
                        "        <p>Проекты Free Walking Tours работают в 500+ городах (чаевые по желанию).</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"travel-tip\">\n" +
                        "        <p class=\"tip-title\">3. Природа</p>\n" +
                        "        <p>Пляжи, парки, горные тропы — всегда бесплатны и часто прекраснее достопримечательностей.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>Заключение</h2>\n" +
                        "    <p>Путешествия с ограниченным бюджетом — это не про лишения, а про умение находить лучшие варианты. Начните с малого: выберите один из советов для своей следующей поездки и постепенно внедряйте остальные.</p>\n" +
                        "\n" +
                        "    <p>Помните: самые яркие воспоминания редко связаны с потраченными деньгами. Чаще всего это неожиданные встречи, красивые виды и чувство свободы — а это совершенно бесплатно.</p>\n" +
                        "</body>\n" +
                        "</html>\n",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Travel%20by%20plane.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL1RyYXZlbCBieSBwbGFuZS5wbmciLCJpYXQiOjE3NDkyMDQ2NTcsImV4cCI6MTc4MDc0MDY1N30.sfwhhLZ2J9BkBqih0tGPYxD6vqz4br7yFLizizIJU5c",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 16,
                title = "Экологичный образ жизни: с чего начать",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Экологичный образ жизни: с чего начать</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "        }\n" +
                        "        h1 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 24px;\n" +
                        "            margin: 0 0 10px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h3 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 18px;\n" +
                        "            margin: 18px 0 10px 0;\n" +
                        "            font-weight: 500;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        \n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        .eco-step {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 15px;\n" +
                        "            border-radius: 8px;\n" +
                        "            margin-bottom: 15px;\n" +
                        "        }\n" +
                        "        .step-title {\n" +
                        "            font-weight: 500;\n" +
                        "            color: #25242E;\n" +
                        "            margin-bottom: 8px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "    <p>Переход к экологичному образу жизни не требует радикальных изменений — это постепенный процесс осознанных выборов. В этой статье вы найдёте простые и эффективные шаги, которые помогут снизить ваш экологический след без ущерба для комфорта.</p>\n" +
                        "\n" +
                        "    <h2>1. Почему это важно?</h2>\n" +
                        "    <p>Каждый человек оставляет экологический след — площадь земли и водных ресурсов, необходимых для производства потребляемых товаров и утилизации отходов. В среднем:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Житель России оставляет след в 4,4 гектара</li>\n" +
                        "        <li>Для устойчивого развития планеты допустимо не более 1,8 гектара на человека</li>\n" +
                        "        <li>Простые изменения могут сократить ваш след на 30-40% за год</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Факт:</strong> Замена одноразовых предметов на многоразовые альтернативы сокращает количество отходов одной семьи на 500-700 кг в год.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>2. Первые шаги к экологичности</h2>\n" +
                        "\n" +
                        "    <div class=\"eco-step\">\n" +
                        "        <p class=\"step-title\">1. Аудит привычек</p>\n" +
                        "        <p>Проанализируйте свою ежедневную рутину и выделите 3 самых \"неэкологичных\" аспекта:</p>\n" +
                        "        <ul>\n" +
                        "            <li>Использование пластиковых пакетов</li>\n" +
                        "            <li>Покупка бутилированной воды</li>\n" +
                        "            <li>Чрезмерное потребление электроэнергии</li>\n" +
                        "        </ul>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"eco-step\">\n" +
                        "        <p class=\"step-title\">2. Многоразовые альтернативы</p>\n" +
                        "        <p>Начните с замены одноразовых предметов:</p>\n" +
                        "        <ul>\n" +
                        "            <li>Тканевая сумка вместо пакетов</li>\n" +
                        "            <li>Многоразовая бутылка для воды</li>\n" +
                        "            <li>Стеклянные контейнеры для еды</li>\n" +
                        "            <li>Металлическая соломинка для напитков</li>\n" +
                        "        </ul>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"eco-step\">\n" +
                        "        <p class=\"step-title\">3. Осознанное потребление</p>\n" +
                        "        <p>Перед покупкой задавайте себе вопросы:</p>\n" +
                        "        <ol>\n" +
                        "            <li>Действительно ли мне это нужно?</li>\n" +
                        "            <li>Как долго я буду это использовать?</li>\n" +
                        "            <li>Как это утилизировать после использования?</li>\n" +
                        "        </ol>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Область жизни</th>\n" +
                        "            <th>Простое изменение</th>\n" +
                        "            <th>Эффект за год</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Питание</td>\n" +
                        "            <td>1 день без мяса в неделю</td>\n" +
                        "            <td>-200 кг CO₂</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Транспорт</td>\n" +
                        "            <td>10 км на велосипеде вместо авто</td>\n" +
                        "            <td>-300 кг CO₂</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Энергия</td>\n" +
                        "            <td>LED-лампы вместо ламп накаливания</td>\n" +
                        "            <td>-150 кг CO₂</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Вода</td>\n" +
                        "            <td>Сокращение душа на 2 минуты</td>\n" +
                        "            <td>10 000 литров воды</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h2>3. Как не бросить на полпути</h2>\n" +
                        "    <p>Психологические приёмы для формирования устойчивых привычек:</p>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        <p>\"Мы не нуждаемся в горстке людей, идеально следующих принципам zero waste. Нам нужно миллионы людей, делающих это неидеально.\" — Энн Мари Боню</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <ol>\n" +
                        "        <li><strong>Метод \"маленьких шагов\":</strong> Начинайте с 1-2 простых изменений</li>\n" +
                        "        <li><strong>Визуализация результата:</strong> Считайте сэкономленные ресурсы</li>\n" +
                        "        <li><strong>Экодрузья:</strong> Найдите единомышленников для поддержки</li>\n" +
                        "        <li><strong>Честность с собой:</strong> Разрешите себе ошибки и постепенный прогресс</li>\n" +
                        "        <li><strong>Позитивный фокус:</strong> Отмечайте то, что уже делаете, а не только недочёты</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <h2>4. Продвинутые практики</h2>\n" +
                        "    <p>Когда базовые привычки сформированы, можно добавить:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Компостирование:</strong> Даже в городской квартире с помощью вермикомпостеров</li>\n" +
                        "        <li><strong>Совместное потребление:</strong> Обмен вещами, каршеринг, совместные закупки</li>\n" +
                        "        <li><strong>\"Зелёные\" финансы:</strong> Банки с экологическими программами, ответственные инвестиции</li>\n" +
                        "        <li><strong>Экологичный гардероб:</strong> Покупка б/у одежды, отказ от fast fashion</li>\n" +
                        "        <li><strong>Экотуризм:</strong> Путешествия с минимальным углеродным следом</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Важно:</strong> Не стремитесь к идеалу сразу. Лучше постепенно внедрять изменения, которые станут частью вашего образа жизни, чем пытаться сделать всё сразу и быстро выгореть.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>5. Полезные ресурсы</h2>\n" +
                        "    <p>Для дальнейшего изучения:</p>\n" +
                        "    <ul>\n" +
                        "        <li>Приложения: JouleBug (экологические челленджи), Think Dirty (анализ косметики)</li>\n" +
                        "        <li>Книги: \"Дом без отходов\" Беа Джонсон, \"Как отказаться от пластика\" Уилл МакКаллум</li>\n" +
                        "        <li>Сообщества: \"Zero Waste Россия\", \"РазДельный Сбор\"</li>\n" +
                        "        <li>Подкасты: \"Зелёный подкаст\", \"Экологика\"</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <h2>Заключение</h2>\n" +
                        "    <p>Экологичный образ жизни — это не ограничения, а новые возможности для творчества и осознанности. Начните с того, что вам действительно близко — будь то сокращение отходов, экономия воды или \"зелёные\" покупки.</p>\n" +
                        "\n" +
                        "    <p>Помните: каждый ваш осознанный выбор имеет значение. Даже небольшие изменения, умноженные на миллионы людей, способны изменить ситуацию к лучшему.</p>\n" +
                        "</body>\n" +
                        "</html>\n",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Man%20and%20woman%20team%20up%20for%20searching.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL01hbiBhbmQgd29tYW4gdGVhbSB1cCBmb3Igc2VhcmNoaW5nLnBuZyIsImlhdCI6MTc0OTIwNTM0MywiZXhwIjoxNzgwNzQxMzQzfQ.C8JlOZ8FIaBQ8eNfL4ixG6kFZZAetOvuIIxPvAb8ww0",
                createdAt = Date().toString()
            ),
            LocalArticle(
                id = 17,
                title = "Как перестать тревожиться из-за мелочей",
                content = "<!DOCTYPE html>\n" +
                        "<html lang=\"ru\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Как перестать тревожиться из-за мелочей</title>\n" +
                        "    <style>\n" +
                        "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');\n" +
                        "        \n" +
                        "        body {\n" +
                        "            font-family: 'Roboto', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #25242E;\n" +
                        "            margin: 0;\n" +
                        "            padding: 15px;\n" +
                        "            width: 100%;\n" +
                        "            box-sizing: border-box;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 20px;\n" +
                        "            margin: 20px 0 12px 0;\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        h3 {\n" +
                        "            color: #25242E;\n" +
                        "            font-size: 18px;\n" +
                        "            margin: 18px 0 10px 0;\n" +
                        "            font-weight: 500;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            margin-bottom: 12px;\n" +
                        "            font-weight: 400;\n" +
                        "            font-size: 16px;\n" +
                        "        }\n" +
                        "        ul, ol {\n" +
                        "            margin-bottom: 15px;\n" +
                        "            padding-left: 20px;\n" +
                        "        }\n" +
                        "        li {\n" +
                        "            margin-bottom: 8px;\n" +
                        "            line-height: 1.5;\n" +
                        "            font-size: 15px;\n" +
                        "        }\n" +
                        "        strong {\n" +
                        "            font-weight: 700;\n" +
                        "        }\n" +
                        "        \n" +
                        "        .highlight {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 16px;\n" +
                        "            border-left: 4px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            width: 100%;\n" +
                        "            border-collapse: collapse;\n" +
                        "            margin: 20px 0;\n" +
                        "        }\n" +
                        "        th, td {\n" +
                        "            padding: 10px;\n" +
                        "            text-align: left;\n" +
                        "            border: 1px solid #ddd;\n" +
                        "        }\n" +
                        "        th {\n" +
                        "            background-color: #f2f2f2;\n" +
                        "        }\n" +
                        "        .quote {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            font-style: italic;\n" +
                        "            border-left: 3px solid #DDA6FF;\n" +
                        "            margin: 20px 0;\n" +
                        "            padding: 16px;\n" +
                        "            border-radius: 0 4px 4px 0;\n" +
                        "        }\n" +
                        "        .technique {\n" +
                        "            background-color: #EBD4F7;\n" +
                        "            padding: 15px;\n" +
                        "            border-radius: 8px;\n" +
                        "            margin-bottom: 15px;\n" +
                        "        }\n" +
                        "        .technique-title {\n" +
                        "            font-weight: 500;\n" +
                        "            color: #25242E;\n" +
                        "            margin-bottom: 8px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "    <p>Постоянное беспокойство по незначительным поводам истощает психику и снижает качество жизни. В этой статье вы найдёте проверенные психологические техники и упражнения, которые помогут снизить уровень тревожности и обрести душевное равновесие.</p>\n" +
                        "\n" +
                        "    <h2>1. Почему мы тревожимся из-за мелочей?</h2>\n" +
                        "    <p>Корни повышенной тревожности:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Когнитивные искажения:</strong> Преувеличение значимости событий</li>\n" +
                        "        <li><strong>Перфекционизм:</strong> Страх совершить ошибку</li>\n" +
                        "        <li><strong>Гиперконтроль:</strong> Желание предсказать всё заранее</li>\n" +
                        "        <li><strong>Эмоциональное выгорание:</strong> Истощение нервной системы</li>\n" +
                        "        <li><strong>Гормональный дисбаланс:</strong> Повышенный уровень кортизола</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Факт:</strong> 85% того, о чем мы беспокоимся, никогда не происходит, а из оставшихся 15% в 80% случаев мы справляемся лучше, чем ожидали.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>2. Практические техники для снижения тревожности</h2>\n" +
                        "\n" +
                        "    <div class=\"technique\">\n" +
                        "        <p class=\"technique-title\">1. Метод \"5-5-5\"</p>\n" +
                        "        <p>Когда чувствуете тревогу, спросите себя:</p>\n" +
                        "        <ol>\n" +
                        "            <li>Будет ли это важно через 5 дней?</li>\n" +
                        "            <li>Будет ли это важно через 5 месяцев?</li>\n" +
                        "            <li>Будет ли это важно через 5 лет?</li>\n" +
                        "        </ol>\n" +
                        "        <p>Это помогает оценить реальную значимость ситуации.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"technique\">\n" +
                        "        <p class=\"technique-title\">2. Техника \"Заземление\"</p>\n" +
                        "        <p>В момент тревоги:</p>\n" +
                        "        <ul>\n" +
                        "            <li>Назовите 5 вещей, которые видите</li>\n" +
                        "            <li>4 вещи, которые ощущаете тактильно</li>\n" +
                        "            <li>3 звука, которые слышите</li>\n" +
                        "            <li>2 запаха, которые чувствуете</li>\n" +
                        "            <li>1 вкус во рту</li>\n" +
                        "        </ul>\n" +
                        "        <p>Это возвращает в момент \"здесь и сейчас\".</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"technique\">\n" +
                        "        <p class=\"technique-title\">3. \"Дневник тревог\"</p>\n" +
                        "        <p>Записывайте:</p>\n" +
                        "        <ol>\n" +
                        "            <li>Ситуацию, вызвавшую тревогу</li>\n" +
                        "            <li>Ваши прогнозы (что плохого случится)</li>\n" +
                        "            <li>Реальный исход</li>\n" +
                        "        </ol>\n" +
                        "        <p>Через месяц вы увидите, как часто тревога была беспочвенной.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <table>\n" +
                        "        <tr>\n" +
                        "            <th>Ситуация</th>\n" +
                        "            <th>Уровень тревоги (до)</th>\n" +
                        "            <th>Реальный исход</th>\n" +
                        "            <th>Уровень тревоги (после)</th>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Опоздание на встречу</td>\n" +
                        "            <td>8/10</td>\n" +
                        "            <td>Коллеги поняли, проблем не было</td>\n" +
                        "            <td>2/10</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Ошибка в отчёте</td>\n" +
                        "            <td>7/10</td>\n" +
                        "            <td>Исправили за 10 минут</td>\n" +
                        "            <td>1/10</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td>Забытый телефон</td>\n" +
                        "            <td>6/10</td>\n" +
                        "            <td>Вернулись за ним без последствий</td>\n" +
                        "            <td>1/10</td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "\n" +
                        "    <h2>3. Физиологические методы снижения тревоги</h2>\n" +
                        "    <p>Тело и психика связаны — эти методы работают через физиологию:</p>\n" +
                        "\n" +
                        "    <div class=\"quote\">\n" +
                        "        <p>\"Тревога — это как кресло-качалка: ты постоянно в движении, но никуда не продвигаешься.\" — Джоди Пиколт</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <ol>\n" +
                        "        <li><strong>Дыхание 4-7-8:</strong> Вдох на 4 счета, задержка на 7, выдох на 8</li>\n" +
                        "        <li><strong>Прогрессивная релаксация:</strong> Поочерёдное напряжение и расслабление мышц</li>\n" +
                        "        <li><strong>Холодный душ:</strong> 30 секунд холодной воды останавливает панические атаки</li>\n" +
                        "        <li><strong>Физическая активность:</strong> 20 минут кардио снижают уровень кортизола</li>\n" +
                        "        <li><strong>Массаж ладоней:</strong> Надавливание на точку между большим и указательным пальцем</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <h2>4. Как изменить мышление</h2>\n" +
                        "    <p>Когнитивно-поведенческие техники:</p>\n" +
                        "    <ul>\n" +
                        "        <li><strong>Переформулирование:</strong> \"Это не катастрофа, а небольшая неприятность\"</li>\n" +
                        "        <li><strong>Предельный вопрос:</strong> \"Что самое страшное может случиться? И как я с этим справлюсь?\"</li>\n" +
                        "        <li><strong>Статистический подход:</strong> \"Как часто это действительно случалось?\"</li>\n" +
                        "        <li><strong>Перспектива:</strong> \"Что я скажу об этом через год?\"</li>\n" +
                        "        <li><strong>Разделение ответственности:</strong> \"Что зависит лично от меня в этой ситуации?\"</li>\n" +
                        "    </ul>\n" +
                        "\n" +
                        "    <div class=\"highlight\">\n" +
                        "        <p><strong>Важно:</strong> Тревога часто возникает от неопределённости. Составление конкретного плана действий снижает беспокойство на 60-70%.</p>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <h2>5. Профилактика тревожности</h2>\n" +
                        "    <p>Что включить в ежедневную рутину:</p>\n" +
                        "    <ol>\n" +
                        "        <li><strong>Режим сна:</strong> 7-9 часов в одно и то же время</li>\n" +
                        "        <li><strong>Цифровой детокс:</strong> Хотя бы 1 час без гаджетов перед сном</li>\n" +
                        "        <li><strong>Практика благодарности:</strong> 3 хорошие вещи за день</li>\n" +
                        "        <li><strong>Природа:</strong> 20 минут в день на свежем воздухе</li>\n" +
                        "        <li><strong>Творчество:</strong> Рисование, лепка, музыка без оценки результата</li>\n" +
                        "    </ol>\n" +
                        "\n" +
                        "    <h2>Заключение</h2>\n" +
                        "    <p>Тревога из-за мелочей — это привычка ума, а не неизбежность. Начните с малого — выберите одну технику из статьи и применяйте её в течение недели. Постепенно вы перестроите свои реакции и обретёте больше спокойствия в повседневной жизни.</p>\n" +
                        "\n" +
                        "    <p>Помните: ваша тревога — это преувеличенная забота о будущем. Попробуйте перенести часть этой энергии на заботу о себе в настоящем моменте.</p>\n" +
                        "</body>\n" +
                        "</html>\n" +
                        "...",
                articleImage = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Happy%20woman%20on%20a%20shopping%20spree.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL0hhcHB5IHdvbWFuIG9uIGEgc2hvcHBpbmcgc3ByZWUucG5nIiwiaWF0IjoxNzQ5MjA1NDE5LCJleHAiOjE3ODA3NDE0MTl9.DnyyyWZSGVneqKVJvnhLyUxT7320JMhssfwkBs-Sl0s",
                createdAt = Date().toString()
            )
        )
        viewModelScope.launch {
            // Удаляем старые статьи перед добавлением новых
            repository.deleteAll()

            // Создаем список всех справочных статей

            // Добавляем все статьи в базу данных
            defaultArticles.forEach { article ->
                repository.upsert(article)
            }
        }
    }

    // Функция для удаления всех статей при выходе
    fun clearAllArticles() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }


}