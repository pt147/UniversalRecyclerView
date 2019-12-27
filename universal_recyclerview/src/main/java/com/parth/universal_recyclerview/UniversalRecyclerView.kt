package com.parth.universal_recyclerview

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.recyclerview.view.*


class UniversalRecyclerView : FrameLayout {
    private var recyclerView: RecyclerView? = null
    private var swipeToRefresh: SwipeRefreshLayout? = null
    private var progressBar: ProgressBar? = null
    private var shimmerView: ShimmerLayout? = null
    private var tvNoData: TextView? = null
    private var isLoading = false
    private var isPaginationWorking = false
    private var isShowNoData = false
    private var isPaginationEnabled = false
    private var isReachToTheEnd = false
    private var isSwipeToRefreshEnable = false
    private var isShowNoDataMessage = ""
    private var loadingStyle = LoadingStyle.Progressbar
    private var pagination: Pagination? = null
    private var pullToRefresh: PullToRefresh? = null
    private var noDataTextSize: Int = 0
    private var noDataTextColor: Int = -1

    private enum class LoadingStyle {
        Shimmer, Progressbar, None
    }

    constructor(context: Context) : super(context) {
        init(context, null, -1)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs != null && defStyleAttr != -1) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.UniversalRecyclerView, defStyleAttr, 0)
            isLoading = a.getBoolean(R.styleable.UniversalRecyclerView_showProgress, true)
            isShowNoData = a.getBoolean(R.styleable.UniversalRecyclerView_showNoData, false)
            isPaginationEnabled = a.getBoolean(R.styleable.UniversalRecyclerView_pagination, false)
            noDataTextSize = a.getDimensionPixelSize(R.styleable.UniversalRecyclerView_noDataTextSize, 70)
            noDataTextColor = a.getResourceId(R.styleable.UniversalRecyclerView_noDataTextColor, R.color.black)
            isSwipeToRefreshEnable = a.getBoolean(R.styleable.UniversalRecyclerView_enableSwipeToRefresh, false)
            isShowNoDataMessage = a.getString(
                R.styleable.UniversalRecyclerView_noDataText
            ) ?: context.getString(R.string.no_record_founds)
            loadingStyle = LoadingStyle.values()[a.getInt(R.styleable.UniversalRecyclerView_loadingStyle, 1)]
            a.recycle()
        }

        View.inflate(context, R.layout.recyclerview, null).apply {
            recyclerView = recycler_view
            progressBar = progress_bar
            shimmerView = shimmer
            tvNoData = tv_no_data
            this@UniversalRecyclerView.swipeToRefresh = swipeToRefresh
            tvNoData?.text = isShowNoDataMessage
            swipeToRefresh?.isEnabled = isSwipeToRefreshEnable
            tvNoData?.setTextSize(TypedValue.COMPLEX_UNIT_PX, noDataTextSize.toFloat())
            tvNoData?.setTextColor(ContextCompat.getColor(context, noDataTextColor))
            addView(this)
        }

        if (isLoading) {
            showLoading()
        }

        if (isSwipeToRefreshEnable) {
            setPullToRefresh()
        }
    }

    private fun setPullToRefresh() {
        swipeToRefresh?.setOnRefreshListener {
            pullToRefresh?.onPageRefresh()
            isPaginationWorking = true
        }
    }


    private fun dismissLoading() {
        when (loadingStyle) {
            LoadingStyle.Shimmer -> {
                shimmerView?.visibility = View.GONE
                isLoading = false
            }
            LoadingStyle.Progressbar -> {
                progressBar?.visibility = View.GONE
                isLoading = false
            }
            else -> {
            }
        }
    }

    private fun showLoading() {
        when (loadingStyle) {
            LoadingStyle.Shimmer -> {
                shimmerView?.visibility = View.VISIBLE
                progressBar?.visibility = View.GONE
                isLoading = true
            }
            LoadingStyle.Progressbar -> {
                shimmerView?.visibility = View.GONE
                progressBar?.visibility = View.VISIBLE
                isLoading = true
            }
            LoadingStyle.None -> {
                isLoading = false
            }
        }
    }

    private fun setPagination() {

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = recyclerView.layoutManager?.childCount ?: 0
                val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
                val firstVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >=
                    totalItemCount && firstVisibleItemPosition >= 0
                ) {
                    if (!isReachToTheEnd && !isPaginationWorking) {
                        callLoadMore()
                    }
                }
            }

        })

    }

    private fun callLoadMore() {
        progress_bar_pagination.visibility = View.VISIBLE
        pagination?.OnLoadMore()
        isPaginationWorking = true
    }


    /*
    * Call this function when you have no more data in pagination.
    */
    fun noMoreDataInPagination() {
        isReachToTheEnd = true
    }

    /*
    * Call this function when your data is ready to be loaded.
    * ex 1). Call this function after getting response from your webservice.
    */
    fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter
        if (isShowNoData) {
            if (adapter.itemCount == 0) tvNoData?.visibility = View.VISIBLE else tvNoData?.visibility = View.GONE
        }
        dismissLoading()

        if (isPaginationEnabled) {
            setPagination()
        }


    }

    /*
    * setter for pagination callback.
   */
    fun setOnPagination(pagination: Pagination) {
        this.pagination = pagination
    }

    /*
    * setter for Pull to refresh callback.
   */
    fun setOnPullToRefresh(pullToRefresh: PullToRefresh) {
        this.pullToRefresh = pullToRefresh
    }

    /*
   * Use this method after getting response from pagination & pull to refresh.
  */
    fun loadComplete() {
        recyclerView?.adapter?.notifyDataSetChanged()
        if (isShowNoData) {
            if (recyclerView?.adapter?.itemCount == 0) tvNoData?.visibility = View.VISIBLE else tvNoData?.visibility =
                View.GONE
        }
        progress_bar_pagination.visibility = View.GONE
        isPaginationWorking = false

        if (isSwipeToRefreshEnable) {
            swipeToRefresh?.isRefreshing = false
        }
    }

    /*
    * Return the instance of your recyclerView.
    * */
    fun getRecyclerView() = recyclerView


}