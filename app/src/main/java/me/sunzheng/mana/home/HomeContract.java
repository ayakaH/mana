package me.sunzheng.mana.home;

import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;

import com.google.android.exoplayer2.SimpleExoPlayer;

import me.sunzheng.mana.core.Episode;
import me.sunzheng.mana.utils.IPresenter;
import me.sunzheng.mana.utils.IView;

/**
 * Created by Sun on 2017/5/23.
 */

public interface HomeContract {
    interface OnAir {
        interface View extends IView<Presenter> {
            void showToast(String errorMessage);

            void showProgressIntractor(boolean active);

            void setAdapter(RecyclerView.Adapter adapter);
        }

        interface Presenter extends IPresenter {
            void load(int type);
        }
    }

    interface Bangumi {

        interface View extends IView<Presenter> {
            void showProgressIntractor(boolean active);

            void setWeekDay(long updateDate);

            void setAirDate(String startDate);

            void setSummary(CharSequence descript);

            void setOriginName(CharSequence originName);

            void setAdapter(RecyclerView.Adapter adapter);

            void setFavouriteStatus(long status);

            void setName(CharSequence name);

            void setEpisode(int eps_now, int eps);
        }

        interface Presenter extends IPresenter {
            void load();

            void changeBangumiFavoriteState(int status);
        }
    }

    interface Search {
        interface View extends IView<Presenter> {
            void loadMoreable(boolean loadMoreable);

            void showProgressIntractor(boolean active);

            void empty();

            void setAdapter(RecyclerView.Adapter adapter);

            void notifyDataSetChanged();

            void showLoadMoreProgressIntractor(boolean active);
        }

        interface Presenter extends IPresenter {
            int INT_DEFAULT_PAGE = 1;
            int INT_DEFAULT_PAGESIZE = 10;

            void query(String key);

            void loadMore();
        }
    }

    interface MyBangumi {
        interface View extends IView<Presenter> {
            void onFilter(int status);

            void showEmpty();

            void showProgressIntractor(boolean active);

            void setAdapter(RecyclerView.Adapter adapter);
        }

        interface Presenter extends IPresenter {
            void load();

            void setFilter(int status);
        }
    }

    interface VideoPlayer {
        interface View extends IView<Presenter> {
            void setPlayItemChecked(int position, boolean isChecked);

            void setAdapter(BaseAdapter adapter);

            void setMediaTitle(CharSequence title);

            void showVolumeVal(int val);

            void showProgressDetaVal(int detaVal);

            void showLightVal(int val);
        }

        interface Presenter extends IPresenter {
            void doubleClick();

            void addPlayQueue(Episode episode);

            SimpleExoPlayer getPlayer();

            void syncWatchLog(String episodeId, long position, long duration);

            boolean isEndOfList();

            void play();

            void pause();

            void release();

            void tryPlayItem(int position);

            void setVolumeVal(int val);

            void setLightVal(int val);

            void seekTo(long detaVal);
        }
    }
}
