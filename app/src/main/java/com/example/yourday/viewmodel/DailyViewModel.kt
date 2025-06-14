//package com.example.yourday.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.yourday.model.Task
//import com.example.yourday.repository.TaskRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//class DailyViewModel(
//    private val taskRepository: TaskRepository,
//    // другие репозитории
//) : ViewModel() {
//    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
//    val tasks: StateFlow<List<Task>> = _tasks
//
//    fun loadDailyData(date: String, userId: String) {
//        viewModelScope.launch {
//            _tasks.value = taskRepository.getTasks(date, userId)
//            // Загрузка других данных
//        }
//    }
//
//    fun addTask(task: Task, userId: String) {
//        viewModelScope.launch {
//            taskRepository.addTask(task, userId)
//            loadDailyData(task.dueDate ?: getCurrentDate(), userId)
//        }
//    }
//
//    // Остальные методы
//}