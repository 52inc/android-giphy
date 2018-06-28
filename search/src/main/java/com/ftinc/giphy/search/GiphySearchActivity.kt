package com.ftinc.giphy.search


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL
import android.view.View
import com.ftinc.giphy.api.Giphy
import com.ftinc.giphy.api.model.Gif
import com.ftinc.giphy.api.model.Rendition
import com.ftinc.giphy.search.adapter.GifRecyclerAdapter
import com.ftinc.giphy.search.model.GiphyGif
import com.ftinc.kit.kotlin.adapter.ListRecyclerAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_giphy_search.*
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.support.design.widget.CollapsingToolbarLayout
import android.util.Log
import com.ftinc.giphy.api.model.GifResponse
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.ftinc.kit.kotlin.utils.*
import com.ftinc.kit.util.UIUtils
import okhttp3.logging.HttpLoggingInterceptor.Level.*


class GiphySearchActivity : AppCompatActivity(), ListRecyclerAdapter.OnItemClickListener<Gif>,
        SearchView.OnQueryTextListener {

    private val apiKey by bindString(EXTRA_API_KEY)
    private val url by bindString(EXTRA_URL, Giphy.DEFAULT_URL)
    private val rendition by bindOptionalEnum<Rendition>(EXTRA_RENDITION)
    private val limit by bindInt(EXTRA_LIMIT, -1)
    private val lang by bindOptionalString(EXTRA_LANG)

    private val searchCardMargin by lazy { dpToPx(8f) }
    private val searchCardHeight by lazy { dpToPx(48f) }
    private val appBarHeight by lazy { UIUtils.getActionBarSize(this) }

    private lateinit var giphy: Giphy
    private lateinit var adapter: GifRecyclerAdapter

    private var searchDisposable: Disposable? = null
    private var state = State()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giphy_search)

        setSupportActionBar(appbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
        appbar.setNavigationOnClickListener { finish() }

        appbar_layout.addOnOffsetChangedListener { _, verticalOffset ->
            val percent = (appbar.height + verticalOffset).toFloat() / appbar.height.toFloat()
            val margin = percent * searchCardMargin
            val height = ((1f - percent) * (appBarHeight - searchCardHeight)) + searchCardHeight

            val lp = searchCardView.layoutParams as CollapsingToolbarLayout.LayoutParams
            if (lp.marginStart != margin.toInt() || lp.marginEnd != margin.toInt() || lp.bottomMargin != margin.toInt()) {
                lp.height = height.toInt()
                lp.marginStart = margin.toInt()
                lp.marginEnd = margin.toInt()
                lp.bottomMargin = margin.toInt()
                searchCardView.layoutParams = lp
            }
        }

        adapter = GifRecyclerAdapter(this, rendition)
        adapter.setEmptyView(emptyView)
        adapter.setOnItemClickListener(this)

        recycler.adapter = adapter
        recycler.layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        recycler.addOnScrollListener(PagingScrollListener {
            nextPage()
        })

        searchView.setOnQueryTextListener(this)

        giphy = Giphy(apiKey, url, if (BuildConfig.DEBUG) HEADERS else NONE)

        // Delay the trending load
        recycler.postDelayed({
            loadTrending()
        }, 250)
    }


    override fun onDestroy() {
        searchDisposable?.dispose()
        super.onDestroy()
    }


    override fun onItemClick(v: View, item: Gif, position: Int) {
        val data = Intent()
        data.putExtra(EXTRA_GIF, GiphyGif(item))
        setResult(RESULT_OK, data)
        finish()
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            search(query)
            return true
        } ?: clearResults()
        return false
    }


    override fun onQueryTextChange(query: String?): Boolean {
        query?.let {
            search(query)
            return true
        } ?: clearResults()
        return false
    }


    private fun updateState(change: State.Change) {
        state = state.reduce(change)
        renderState()
    }


    private fun renderState() {
        adapter.setGifs(state.results)
        emptyView.emptyMessage = state.error ?: getString(R.string.empty_message_search_results)
        emptyView.setLoading(state.isLoading)
    }


    private fun loadTrending() {
        updateState(State.Change.IsLoading)
        searchDisposable?.dispose()
        searchDisposable = giphy.trending(if (limit == -1) null else limit, state.offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::processResponse) {
                    it.printStackTrace()
                    updateState(State.Change.Error(getString(R.string.error_gifs_loading)))
                }
    }


    private fun search(text: String) {
        updateState(State.Change.Query(text))
        updateState(State.Change.IsLoading)
        searchDisposable?.dispose()
        searchDisposable =  giphy.search(text, if (limit == -1) null else limit, state.offset, lang = lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::processResponse) {
                    it.printStackTrace()
                    updateState(State.Change.Error(getString(R.string.error_gifs_loading)))
                }
    }


    private fun nextPage() {
        if (state.query != null && state.offset != -1) {
            updateState(State.Change.IsLoading)
            state.query?.let {
                searchDisposable?.dispose()
                searchDisposable = giphy.search(it, if (limit == -1) null else limit, state.offset)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::processResponse) {
                            it.printStackTrace()
                            updateState(State.Change.Error(it.localizedMessage
                                    ?: "Was unable to find GIFs for this search term"))
                        }
            }
        } else if (state.offset != -1) {
            updateState(State.Change.IsLoading)
            searchDisposable?.dispose()
            searchDisposable = giphy.trending(if (limit == -1) null else limit, state.offset)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::processResponse) {
                        it.printStackTrace()
                        updateState(State.Change.Error(it.localizedMessage ?: "Was unable to find GIFs for this search term"))
                    }
        }
    }


    private fun processResponse(response: GifResponse) {
        var offset = 0
        response.pagination?.let {
            offset = if (it.count == it.total_count) -1 else it.offset + it.count
        }

        updateState(State.Change.PagedResults(response.data))
        updateState(State.Change.Offset(offset))
    }


    private fun clearResults() {
        adapter.clear()
    }


    data class State(
            val query: String? = null,
            val offset: Int = 0,
            val results: List<Gif> = emptyList(),
            val error: String? = null,
            val isLoading: Boolean = false
    ) {

        fun reduce(change: Change): State = when(change) {
            Change.IsLoading -> this.copy(isLoading = true)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.Offset -> this.copy(offset = change.offset)
            is Change.PagedResults -> this.copy(results = this.results.plus(change.results), isLoading = false)
            is Change.Query -> this.copy(query = change.query, offset = 0, results = emptyList())
        }


        sealed class Change(val logText: String) {
            object IsLoading : Change("network -> loading search results")
            class Error(val description: String) : Change("error -> $description")
            class Offset(val offset: Int) : Change("network -> Offset($offset) updated")
            class PagedResults(val results: List<Gif>) : Change("network -> ${results.size} paged results added")
            class Query(val query: String?) : Change("user -> query: $query")
        }

    }


    inner class PagingScrollListener(private val callback: (Unit) -> Unit) : RecyclerView.OnScrollListener() {

        private val layoutManager = recycler.layoutManager as StaggeredGridLayoutManager

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPositions = layoutManager.findFirstVisibleItemPositions(null)
            val firstVisibleItemPosition = firstVisibleItemPositions.first()

            if (!state.isLoading && state.offset != -1 && !state.results.isEmpty()) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    callback.invoke(Unit)
                }
            }
        }
    }


    companion object {
        internal const val EXTRA_GIF = "GiphySearchActivity.Gif"
        private const val EXTRA_API_KEY = "GiphySearchActivity.ApiKey"
        private const val EXTRA_URL = "GiphySearchActivity.Url"
        private const val EXTRA_RENDITION = "GiphySearchActivity.Rendition"
        private const val EXTRA_LIMIT = "GiphySearchActivity.Limit"
        private const val EXTRA_LANG = "GiphySearchActivity.Lang"


        @JvmStatic
        internal fun createIntent(context: Context,
                                  apiKey: String,
                                  url: String? = null,
                                  rendition: Rendition? = null,
                                  limit: Int? = null,
                                  lang: String? = null): Intent {
            val intent = Intent(context, GiphySearchActivity::class.java)
            intent.putExtras(bundle {
                EXTRA_API_KEY to apiKey
                url?.also { EXTRA_URL to url }
                rendition?.also { EXTRA_RENDITION to rendition }
                limit?.also { EXTRA_LIMIT to limit }
                lang?.also { EXTRA_LANG to lang }
            })

            intent.putExtra(EXTRA_API_KEY, apiKey)
            return intent
        }
    }
}

private operator fun CompositeDisposable.plusAssign(subscribe: Disposable) {
    this.add(subscribe)
}
