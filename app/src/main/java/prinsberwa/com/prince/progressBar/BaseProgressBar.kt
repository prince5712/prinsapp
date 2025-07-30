package prinsberwa.com.prince.progressBar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import prinsberwa.com.prince.IGRefreshLayout

abstract class BaseProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    lateinit var mParent: IGRefreshLayout
    var mPercent = 0f
    var isLoading = false

    abstract fun setPercent(percent: Float)
    abstract fun setParent(parent: IGRefreshLayout)
    abstract fun start()
    abstract fun stop()

    fun dp2px(dp: Int): Int{
        return dp*context.resources.displayMetrics.density.toInt()
    }
}