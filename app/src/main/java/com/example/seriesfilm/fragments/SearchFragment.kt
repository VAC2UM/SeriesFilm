package com.example.seriesfilm.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesfilm.MoviesRepository
import com.example.seriesfilm.R
import com.example.seriesfilm.adapters.MoviesAdapter

class SearchFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var updateBtn: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var historyContainer: LinearLayout
    private lateinit var progressBar: ProgressBar

    private val historyKey = "search_history"
    private val maxHistoryItems = 10
    private val searchDelayMillis = 2000L // 2 секунды
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchView = view.findViewById(R.id.searchView)
        recyclerView = view.findViewById(R.id.resultFilmList)
        updateBtn = view.findViewById(R.id.updateBtn)
        historyContainer = view.findViewById(R.id.historyContainer)
        progressBar = view.findViewById(R.id.progressBar)

        sharedPreferences =
            requireContext().getSharedPreferences("SearchPrefs", Context.MODE_PRIVATE)

        moviesAdapter = MoviesAdapter(emptyList())
        recyclerView.adapter = moviesAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Устанавливаем обработчик клика по поисковой строке
        searchView.setOnSearchClickListener {
            showSearchHistory()
        }

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // Отменяем отложенный поиск, если он был
                    cancelDelayedSearch()

                    if (!query.isNullOrEmpty()) {
                        addToSearchHistory(query)
                        performSearch(query)
                    } else {
                        Toast.makeText(
                            context,
                            "Пожалуйста, введите корректный запрос",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // Отменяем предыдущий отложенный поиск
                    cancelDelayedSearch()

                    val closeButton =
                        searchView.findViewById<View>(androidx.appcompat.R.id.search_close_btn)
                    closeButton.visibility = View.GONE

                    if (newText.isNullOrEmpty()) {
                        closeButton.visibility = View.GONE
                    } else {
                        closeButton.visibility = View.VISIBLE

                        // Запускаем отложенный поиск через 2 секунды
                        if (newText.length >= 3) { // Ищем только если введено хотя бы 3 символа
                            searchRunnable =
                                Runnable {
                                    if (!newText.isNullOrEmpty()) {
                                        addToSearchHistory(newText)
                                        performSearch(newText)
                                    }
                                }
                            handler.postDelayed(searchRunnable!!, searchDelayMillis)
                        }
                    }
                    return true
                }
            }
        )

        updateBtn.setOnClickListener {
            val query = searchView.query.toString()
            if (query.isNotEmpty()) {
                performSearch(query)
            } else {
                Toast.makeText(context, "Пожалуйста, введите корректный запрос", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return view
    }

    private fun cancelDelayedSearch() {
        searchRunnable?.let {
            handler.removeCallbacks(it)
            searchRunnable = null
        }
    }

    private fun performSearch(query: String) {
        hideHistory()
        showProgress(true)

        MoviesRepository.fetchMovies(query, 1) { results, error ->
            showProgress(false)

            if (results != null) {
                if (results.isNotEmpty()) {
                    moviesAdapter.updateMovies(results)
                    updateBtn.visibility = View.INVISIBLE
                } else {
                    Toast.makeText(context, "Результаты не найдены", Toast.LENGTH_SHORT).show()
                    updateBtn.visibility = View.VISIBLE
                }
            } else {
                Log.e("SearchFragment", "Error occurred: $error")
                Toast.makeText(context, error ?: "Unknown error", Toast.LENGTH_LONG).show()
                updateBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun showProgress(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.INVISIBLE else View.VISIBLE
    }

    private fun addToSearchHistory(query: String) {
        val history = getSearchHistory().toMutableList()
        history.removeAll { it.equals(query, ignoreCase = true) }
        history.add(0, query)

        if (history.size > maxHistoryItems) {
            history.subList(maxHistoryItems, history.size).clear()
        }

        sharedPreferences.edit()
            .putStringSet(historyKey, history.toSet())
            .apply()
    }

    private fun getSearchHistory(): List<String> {
        return sharedPreferences.getStringSet(historyKey, emptySet())?.toList() ?: emptyList()
    }

    private fun clearSearchHistory() {
        sharedPreferences.edit()
            .remove(historyKey)
            .apply()
        hideHistory()
        Toast.makeText(context, "История поиска очищена", Toast.LENGTH_SHORT).show()
    }

    private fun showSearchHistory() {
        val history = getSearchHistory()
        if (history.isEmpty()) {
            hideHistory()
            return
        }

        historyContainer.removeAllViews()
        historyContainer.visibility = View.VISIBLE

        val title =
            TextView(context).apply {
                text = "История поиска"
                textSize = 18f
                setTextColor(resources.getColor(android.R.color.white, null))
                setTypeface(null, Typeface.BOLD)
                setPadding(16, 16, 16, 8)
            }
        historyContainer.addView(title) // Добавляем первым

        for (query in history.reversed()) {
            val historyItem =
                TextView(context).apply {
                    text = query
                    textSize = 16f
                    setTextColor(resources.getColor(android.R.color.white, null))
                    setPadding(32, 16, 16, 16)
                    setOnClickListener {
                        searchView.setQuery(query, true)
                        hideHistory()
                    }
                }
            historyContainer.addView(historyItem)
        }
        val clearButton =
            Button(context).apply {
                text = "Очистить историю"
                setOnClickListener { clearSearchHistory() }
                setBackgroundColor(resources.getColor(R.color.red, null))
                setTextColor(resources.getColor(android.R.color.white, null))
                setPadding(16, 16, 16, 16)
            }
        historyContainer.addView(clearButton)
    }

    private fun hideHistory() {
        historyContainer.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Отменяем все отложенные задачи при уничтожении View
        cancelDelayedSearch()
    }
}
