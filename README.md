# UniversalRecyclerView

[![GitHub license](https://img.shields.io/github/license/pt147/UniversalRecyclerView?style=plastic)](https://github.com/pt147/UniversalRecyclerView/blob/master/LICENSE)

[![GitHub issues](https://img.shields.io/github/issues/pt147/UniversalRecyclerView?style=plastic)](https://github.com/pt147/UniversalRecyclerView/issues)


## Description
UniversalRecyclerView is a lightweight RecyclerView(advanced and flexible version of ListView) with pulling to refresh, pagination ,shimmer and progresbbar loading style and many other features.You can use it just like RecyclerView. Good thing is, it's in androidX.

Notice that UniversalRecyclerView is a project under development.

## Features:
 * Swipe to refresh.
 * Load More.
 * Loading styles includes Progressbar & Shimmer. You can also choose None. :)
 * "No Data Layout" if your list is empty. 
 * Provided customization of Text-Color, Text-Size in "No data Layout".
 
 ## Quick Setup (Basic Usage)
 ### 1) Add it in your root build.gradle at the end of repositories:
 ```
 allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 
 ```
 ### 2) Add the dependency
 ```
 dependencies {
         implementation 'com.github.pt147:UniversalRecyclerView:1.0.8'
	}
 ```
 
 ### 3) Now, This is how you can use it.
 ```xml
	<com.parth.universal_recyclerview.UniversalRecyclerView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:id="@+id/recycler_view"
            app:showNoData="true"
            app:noDataText="No Data Found."
            app:loadingStyle="Progressbar"/>
 
 ```

 ### Customizing the UI
 * `showProgress`     -- For displaying progress `default:true`
 * `loadingStyle`     -- For displaying various progress type ex. shimmer, progressBar, None. `default:Progressbar` 
 * `showNoData`       -- For displaying "No data layout" `default:false`
 * `noDataText`       -- For displaying "No data layout" text `default:No Record founds!`
 * `noDataTextColor`  -- For displaying "No data layout" text color `default:#000000`
 * `noDataTextSize`   -- For displaying "No data layout" text size. `default:16sp`
 * `enableSwipeToRefresh` -- For include "Pull to refresh layout". `default:false` 
 * `pagination` -- For include pagination. `default:false`
 
 ### For pagination.
 
 set `app:pagination:true` in your xml and in your Kotlin class.
 
 ```java
 your_view.setOnPagination(object : Pagination {
            override fun OnLoadMore() {

                //Call This method once your data is populated in your list.
                recycler_view.loadComplete()

                //Call this method when there is no more data in pagination.
                recycler_view.noMoreDataInPagination()

            }
        })
 ```
 
 ### For Pull to refresh.
 
  set `app:enableSwipeToRefresh="true"` in your xml and in your Kotlin class.
  
  ```java
   recycler_view.setOnPullToRefresh(object : PullToRefresh {
            override fun onPageRefresh() {
                
                //Call This method once your data is populated in your list.
                recycler_view.loadComplete()
            }
        })
  
  ```
