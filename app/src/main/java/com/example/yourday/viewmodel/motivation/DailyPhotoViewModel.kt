package com.example.yourday.viewmodel.motivation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.model.LocalDailyPhoto
import com.example.yourday.repository.motivation.DailyPhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DailyPhotoViewModel(
    application: Application,
    private val repository: DailyPhotoRepository
) : AndroidViewModel(application) {
    val photos: MutableStateFlow<List<LocalDailyPhoto>> = MutableStateFlow(emptyList())

    fun loadPhotosByDate(date: String) {
        viewModelScope.launch {
            repository.getByDate(date).collect { photosList ->
                photos.value = photosList
            }
        }
    }

    suspend fun getById(id: Int): LocalDailyPhoto? {
        return repository.getById(id)
    }

    fun upsertPhoto(photo: LocalDailyPhoto) {
        viewModelScope.launch {
            repository.upsert(photo)
        }
    }

    fun deletePhoto(photo: LocalDailyPhoto) {
        viewModelScope.launch {
            repository.delete(photo)
        }
    }


    fun insertAllReferencePhotos() {
        viewModelScope.launch {
            val referencePhotos = listOf(
                LocalDailyPhoto(1, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2010.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(2, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2011.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(3, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2012.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(4, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2013.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(5, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2014.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(6, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2015.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(7, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2016.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(8, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2017.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(9, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2018.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(10, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2019.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(11, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%201.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(12, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2020.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(13, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2021.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(14, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2022.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(15, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2023.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(16, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2024.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(17, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2025.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(18, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2026.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(19, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2027.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(20, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2028.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(21, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2029.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(22, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%202.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(23, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2030.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(24, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2031.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(25, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2032.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(26, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2033.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(27, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2034.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(28, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2035.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(29, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2036.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(30, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2037.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(31, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2038.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(32, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2039.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(33, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%203.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(34, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2040.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(35, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2041.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(36, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%2042.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(37, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%204.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(38, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%205.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(39, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%206.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(40, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%207.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(41, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%208.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(42, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//a-photograph-of-a-solitary-stone-lightho_--NdrLprTtubS4t7Q7Vaag_rYib58kTQzafp91jugRZ6A%209.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(43, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//kandinsky-download-1746459173974%201.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(44, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//kandinsky-download-1746459295517%201.png", "2025-06-06 15:59:09.409448+00"),
                LocalDailyPhoto(45, "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/daily-photos//kandinsky-download-1746459396785%201.png", "2025-06-06 15:59:09.409448+00")
            )
            repository.insertAllReferencePhotos(referencePhotos)
        }
    }

    // Функция для удаления всех справочных фото
    fun deleteAllReferencePhotos() {
        viewModelScope.launch {
            repository.deleteAllReferencePhotos()
        }
    }


}