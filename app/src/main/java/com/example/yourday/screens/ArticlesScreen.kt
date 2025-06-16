package com.example.yourday.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.yourday.R
import com.example.yourday.api.SupabaseHelper
import com.example.yourday.components.CategoryChip
import com.example.yourday.components.GlobalToast
import com.example.yourday.components.ToastDuration
import com.example.yourday.components.ToastManager
import com.example.yourday.components.ToastType
import com.example.yourday.model.Article
import com.example.yourday.model.ArticleCategory
import com.example.yourday.model.ArticleInCategory
import com.example.yourday.ui.theme.DarkBlue
import com.example.yourday.ui.theme.Primary
import com.example.yourday.ui.theme.Purple1

// Mock data for offline mode
val mockArticles = listOf(
    Article(
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
        createdAt = "2025-06-05 18:32:53.637792+00"
    ),
    Article(
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
        articleImage ="https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/illustrations-of-articles/Woman%20watches%20a%20movie%20with%20popcorn.png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJpbGx1c3RyYXRpb25zLW9mLWFydGljbGVzL1dvbWFuIHdhdGNoZXMgYSBtb3ZpZSB3aXRoIHBvcGNvcm4ucG5nIiwiaWF0IjoxNzQ5MjA0OTYwLCJleHAiOjE3ODA3NDA5NjB9.IkrV5rmBYNoPnWnPpTSJ9do3o0pxHZeOi0WJf0EoAIs",
        createdAt = "2025-06-05 18:32:53.637792+00"
    ),
    Article(
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
        createdAt = "2025-06-05 18:32:53.637792+00"
    ),
    Article(
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
        createdAt = "2025-06-05 18:32:53.637792+00"
    ),
    Article(
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
        createdAt = "2025-06-05 18:32:53.637792+00"
    )

)


val mockCategories = listOf(
    ArticleCategory(1, "Планирование и продуктивность"),
    ArticleCategory(2, "Здоровье и спорт"),
    ArticleCategory(3, "Психология и мотивация"),
    ArticleCategory(4, "Финансы"),
    ArticleCategory(5, "Саморазвитие"),
    ArticleCategory(6, "Творчество и хобби"),
    ArticleCategory(7, "Отношения и коммуникация"),
    ArticleCategory(8, "Технологии и гаджеты"),
    ArticleCategory(9, "Путешествия и образ жизни"),
    ArticleCategory(10, "Психология и мотивация"),
    ArticleCategory(11, "Специальная категория"),
)

val mockArticleInCategories = listOf(
    ArticleInCategory(1, 1, 1),
    ArticleInCategory(2, 2, 1),
    ArticleInCategory(3, 2, 10),
    ArticleInCategory(4, 2, 5),
    ArticleInCategory(5, 3, 1),
    ArticleInCategory(6, 3, 10),
    ArticleInCategory(7, 4, 2),
    ArticleInCategory(8, 4, 5),
    ArticleInCategory(9, 5, 2),
    ArticleInCategory(10, 5, 10)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesScreen(
    navController: NavController,
    onIntentToDetails: (articleId: Int?) -> Unit // Change to accept nullable Int
) {
    val supabaseHelper = remember { SupabaseHelper() }

    // State for articles, categories, and filtered articles
    var articles by remember { mutableStateOf(emptyList<Article>()) }
    var categories by remember { mutableStateOf(emptyList<ArticleCategory>()) }
    var articleInCategories by remember { mutableStateOf(emptyList<ArticleInCategory>()) }
    var selectedCategory by remember { mutableStateOf<ArticleCategory?>(null) }
    var filteredArticles by remember { mutableStateOf(emptyList<Article>()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }




    // Fetch data from Supabase or use mock data
    LaunchedEffect(Unit) {
        try {
            // Try to fetch real data first
            categories = supabaseHelper.getArticleCategories()
            articleInCategories = supabaseHelper.getArticleInCategories()
            articles = supabaseHelper.getArticles()
            isError = false
        } catch (e: Exception) {
            isError = true
            ToastManager.show(
                message = "Загрузка данных временно недоступна.",
                type = ToastType.ERROR,
                duration = ToastDuration.LONG
            )
            // Используем мок-данные
            categories = mockCategories
            articleInCategories = mockArticleInCategories
            articles = mockArticles
        } finally {
            isLoading = false
            ToastManager.show(
                message = "Загрузка данных временно недоступна.",
                type = ToastType.ERROR,
                duration = ToastDuration.LONG
            )
            // Используем мок-данные
            categories = mockCategories
            articleInCategories = mockArticleInCategories
            articles = mockArticles
        }
    }

    // Update filtered articles when category changes
    LaunchedEffect(selectedCategory, articles, articleInCategories) {
        filteredArticles = if (selectedCategory == null) {
            articles // Show all articles
        } else {
            val articleIdsInCategory = articleInCategories
                .filter { it.categoryId == selectedCategory?.id }
                .map { it.articleId }

            articles.filter { article -> articleIdsInCategory.contains(article.id) }
        }
    }

    Box(modifier = Modifier.fillMaxSize()
        .padding(horizontal = 22.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Статьи",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                        color = Primary
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                CategoryChip(
                    text = "Все категории",
                    isSelected = selectedCategory == null,
                    onClick = { selectedCategory = null }
                )

                categories.forEach { category ->
                    CategoryChip(
                        text = category.categoryName,
                        isSelected = selectedCategory?.id == category.id,
                        onClick = { selectedCategory = category }
                    )
                }
            }

            // Articles list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredArticles) { article ->
                    ArticleCard(
                        article = article,
                        categories = categories,
                        articleInCategories = articleInCategories,
                        onClick = { onIntentToDetails(article.id) } // Pass the article id
                    )
                }
            }



        }
        GlobalToast()

    }

}


@Composable
fun ArticleCard(
    article: Article,
    categories: List<ArticleCategory>,
    articleInCategories: List<ArticleInCategory>,
    onClick: () -> Unit
) {
    // Get tags for this article
    val articleTags = articleInCategories
        .filter { it.articleId == article.id }
        .mapNotNull { aic ->
            categories.firstOrNull { it.id == aic.categoryId }?.categoryName
        } // Show only one main category like in the design

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Purple1)
        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image at the top
            AsyncImage(
                model = article.articleImage,
                contentDescription = article.title,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .padding(6.dp),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(R.drawable.image),
                error = painterResource(R.drawable.image)
            )

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {

                // Article title
                Text(
                    text = article.title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                        color = DarkBlue
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom=2.dp)
                )
                Text(
                    text = "7 мин чтения",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_light)),
                        color = DarkBlue
                    )
                )

                Column(
                    modifier = Modifier.padding(top=6.dp)) {
                    if (articleTags.isNotEmpty()) {
                        articleTags.forEach { articleTag ->
                            Text(
                                text = "#${articleTag}",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto_light)),
                                    color = DarkBlue
                                ),
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }

                    }
                }
            }
        }
    }
}


