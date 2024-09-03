import android.content.Context
import com.tomtom.sdk.search.SearchCallback
import com.tomtom.sdk.search.SearchOptions
import com.tomtom.sdk.search.SearchResponse
import com.tomtom.sdk.location.Place
import com.tomtom.sdk.search.common.error.SearchFailure
import com.tomtom.sdk.search.online.OnlineSearch

class SearchRepository(private val context: Context) {
    private val searchApi = OnlineSearch.create(context, "zsyHUGt60Fwrh2iSA5IZkUqArQGte2As")

    fun search(query: String, callback: (List<String>) -> Unit, onFailure: () -> Unit) {
        val options = SearchOptions(
            query = query,
            limit = 5
        )

        searchApi.search(
            options,
            object : SearchCallback {
                override fun onSuccess(result: SearchResponse) {
                    val suggestions = result.results
                        .filter { it.place != null }
                        .map { it.place.name } // Extract name from Place object
                    callback(suggestions)
                }

                override fun onFailure(failure: SearchFailure) {
                    onFailure()
                }
            }
        )
    }
}
