package com.codepath.apps.simpletwitter.interfaces;

import android.widget.AbsListView;

public abstract class EndlessScrollListener
implements AbsListView.OnScrollListener {
    private static int DEFAULT_VISIBLE_THRESHOLD = 5;

    private int visibleThreshold = DEFAULT_VISIBLE_THRESHOLD;
    private int currentPage = -1;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = -1;

    public EndlessScrollListener() {
    }

    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public EndlessScrollListener(int visibleThreshold, int startPage) {
        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = startPage;
        this.currentPage = startPage;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (totalItemCount < previousTotalItemCount) {
            previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                loading = true;
                currentPage = startingPageIndex;
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        if (!loading &&
            (firstVisibleItem + visibleItemCount + visibleThreshold) >=
            totalItemCount) {
            loading = onLoadMore(currentPage + 1, totalItemCount);
        }
    }

    public abstract boolean onLoadMore(int page, int totalItemsCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
