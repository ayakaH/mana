package me.sunzheng.mana.home.mybangumi;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.SparseArray;

import androidx.annotation.Nullable;
import me.sunzheng.mana.core.BangumiModel;

/**
 * Created by Sun on 2017/11/13.
 */

public class FavoritesService extends Service {
    SparseArray<BangumiModel> dataRespository;

    public FavoritesService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
