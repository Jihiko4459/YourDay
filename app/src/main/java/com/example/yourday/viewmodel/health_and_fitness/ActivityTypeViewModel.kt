package com.example.yourday.viewmodel.health_and_fitness

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalActivityType
import com.example.yourday.repository.health_and_fitness.ActivityTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ActivityTypeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ActivityTypeRepository
    val activityTypes: MutableStateFlow<List<LocalActivityType>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = ActivityTypeRepository(db)
        loadActivityTypes()
    }

    private fun loadActivityTypes() {
        viewModelScope.launch {
            repository.getAll().collect { types ->
                activityTypes.value = types
            }
        }
    }

    fun upsertActivityType(type: LocalActivityType) {
        viewModelScope.launch {
            repository.upsert(type)
        }
    }

    fun deleteActivityType(type: LocalActivityType) {
        viewModelScope.launch {
            repository.delete(type)
        }
    }

    // Функция для добавления справочных данных при регистрации/входе
    fun addDefaultActivityTypes() {
        viewModelScope.launch {
            val defaultTypes = listOf(
                LocalActivityType(
                    id = 1,
                    name = "Бег",
                    caloriesPerMin = 15.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//running_vgwo4ekd04xl%201.svg"
                ),
                LocalActivityType(
                    id = 2,
                    name = "Ходьба",
                    caloriesPerMin = 6.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//walk_h6da4yqbend6%201.svg"
                ),
                LocalActivityType(
                    id = 3,
                    name = "Велоспорт",
                    caloriesPerMin = 10.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//cyclist_22i4htgezad9%201.svg"
                ),
                LocalActivityType(
                    id = 4,
                    name = "Плавание",
                    caloriesPerMin = 9.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//swimming_dsp856fod376%201.svg"
                ),
                LocalActivityType(
                    id = 5,
                    name = "Йога",
                    caloriesPerMin = 5.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//Group.svg"
                ),
                LocalActivityType(
                    id = 6,
                    name = "Тренировка",
                    caloriesPerMin = 10.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//dumbbells_0rljf1lyt3gw%201.svg"
                ),
                LocalActivityType(
                    id = 7,
                    name = "Поход в магазин",
                    caloriesPerMin = 4.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//shopping_cart_kn65d3gmkbvj%201.svg"
                ),
                LocalActivityType(
                    id = 8,
                    name = "Выгул собаки",
                    caloriesPerMin = 5.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//dog_fals6qp13l3h%201.svg"
                ),
                LocalActivityType(
                    id = 9,
                    name = "Уборка дома",
                    caloriesPerMin = 5.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//broom_2ebf73plnojg%201.svg"
                ),
                LocalActivityType(
                    id = 10,
                    name = "Поход",
                    caloriesPerMin = 7.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//camping_09eo57zvdr80%201.svg"
                ),
                LocalActivityType(
                    id = 11,
                    name = "Рыбалка",
                    caloriesPerMin = 4.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//fishing_ohanh408p7ac%201.svg"
                ),
                LocalActivityType(
                    id = 12,
                    name = "Лыжи",
                    caloriesPerMin = 12.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//skier_j1pwxf2vhlsa%201.svg"
                ),
                LocalActivityType(
                    id = 13,
                    name = "Танцы",
                    caloriesPerMin = 8.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//dancing_bwj9463nfjr9%201.svg"
                ),
                LocalActivityType(
                    id = 14,
                    name = "Актерское мастерство",
                    caloriesPerMin = 4.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//theatre_xhpbtryeeih0%201.svg"
                ),
                LocalActivityType(
                    id = 15,
                    name = "Игра на инструменте",
                    caloriesPerMin = 3.0,
                    iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//guitar_1n96kd6ki4lg%201.svg"
                )
            )

            // Удаляем старые данные перед добавлением новых (если нужно)
            repository.deleteAll()

            // Добавляем все справочные данные
            defaultTypes.forEach { type ->
                repository.upsert(type)
            }
        }
    }

    // Функция для удаления всех справочных данных при выходе
    fun clearActivityTypes() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }




}