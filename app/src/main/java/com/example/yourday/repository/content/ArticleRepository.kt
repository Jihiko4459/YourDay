package com.example.yourday.repository.content

import com.example.yourday.api.SupabaseHelper
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.Article
import com.example.yourday.model.LocalArticle
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow

class ArticleRepository(
    private val db: YourDayDatabase,
    private val supabaseHelper: SupabaseHelper
) {
    fun getAll(): Flow<List<LocalArticle>> {
        return db.articleDao().getAll()
    }

    suspend fun insert(article: LocalArticle) {
        db.articleDao().insert(article)
        syncArticles()
    }

    private suspend fun syncArticles() {
        val unsynced = db.articleDao().getUnsynced()
        unsynced.forEach { article ->
            try {
                val result = supabaseHelper.withAuth {
                    supabaseHelper.client.from("articles").upsert(
                        Article(
                            id = article.serverId ?: 0,
                            title = article.title,
                            content = article.content,
                            articleImage = article.articleImage,
                            createdAt = article.createdAt
                        )
                    )
                }

                if (result.isSuccess) {
                    db.articleDao().update(article.copy(isSynced = true))
                }
            } catch (e: Exception) {
                // Error handling
            }
        }
    }
}