package me.sunzheng.mana;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import me.sunzheng.mana.home.HomeApiService;
import me.sunzheng.mana.home.HomeContract;
import me.sunzheng.mana.home.search.HistorySuggestionProvider;
import me.sunzheng.mana.home.search.SearchPresenterImpl;
import me.sunzheng.mana.utils.App;

public class SearchResultActivity extends AppCompatActivity implements HomeContract.Search.View {

    RecyclerView mRecyclerView;
    HomeContract.Search.Presenter mPresenter;
    ContentLoadingProgressBar progressBar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    boolean isLoading = false;
    View emptyView;
    boolean loadMoreable = true;
    SearchView mSearchView;

    @Override
    public void loadMoreable(boolean loadMoreable) {
        this.loadMoreable = loadMoreable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressbar);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && layoutManager.findFirstCompletelyVisibleItemPosition() + layoutManager.getChildCount() >= layoutManager.getItemCount() && loadMoreable) {
                    mPresenter.loadMore();
                }
            }
        });
        emptyView = findViewById(R.id.empty_content_textview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.search_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setEnabled(false);
        mPresenter = new SearchPresenterImpl(this, ((App) getApplicationContext()).getRetrofit().create(HomeApiService.Bangumi.class));
        setPresenter(mPresenter);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String key = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this, HistorySuggestionProvider.AUTHORITY, HistorySuggestionProvider.MODE);
            searchRecentSuggestions.saveRecentQuery(key, null);
            query(key);
        }
    }

    private void query(String key) {
        mPresenter.query(key);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            mSearchView = (SearchView) searchItem.getActionView();
            if (mSearchView != null) {
                mSearchView.setMaxWidth(Integer.MAX_VALUE);
                mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        MenuItemCompat.collapseActionView(searchItem);
                        mSearchView.setQuery(query, false);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
                mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                    @Override
                    public boolean onSuggestionSelect(int position) {
                        return false;
                    }

                    @Override
                    public boolean onSuggestionClick(int position) {
                        Cursor c = mSearchView.getSuggestionsAdapter().getCursor();
                        if (c == null || !c.moveToPosition(position))
                            return false;
                        int index = c.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
                        if (index == -1)
                            return false;
                        mSearchView.setQuery(c.getString(index), false);
                        return false;
                    }
                });
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(HomeContract.Search.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void empty() {
        showEmptyView(true);
        AppCompatTextView messageTextView = (AppCompatTextView) findViewById(R.id.empty_content_textview);
        if (messageTextView != null)
            messageTextView.setText("No result");
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        showEmptyView(false);
        if (mRecyclerView.getLayoutManager() == null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }
        if (mRecyclerView.getAdapter() == null)
            mRecyclerView.setAdapter(adapter);
        else
            mRecyclerView.swapAdapter(adapter, false);
    }

    @Override
    public void notifyDataSetChanged() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
        isLoading = false;
    }

    @Override
    public void showProgressIntractor(boolean active) {
        mSwipeRefreshLayout.setRefreshing(active);
    }

    @Override
    public void showLoadMoreProgressIntractor(boolean active) {
        if (mSwipeRefreshLayout == null)
            return;
        isLoading = active;
        mSwipeRefreshLayout.setRefreshing(active);
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void showEmptyView(boolean active) {
        emptyView.setVisibility(active ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(active ? View.GONE : View.VISIBLE);
    }
}
