package me.sunzheng.mana.utils;

import me.sunzheng.mana.R;

/**
 * Created by Sun on 2017/5/21.
 */

public interface PreferenceManager {
    interface Global{
        String STR_SP_NAME="global";
        String STR_KEY_HOST="host";
        String INT_KEY_DEFAULT_TIMEOUT="timeout";
        String BOOL_IS_REMEMBERD="rememberd";
        String STR_USERNAME="username";
        String STR_PASSWORD="password";

        int RES_JA_FIRST_BOOL = R.string.pref_key_ja_first_bool;
    }

    interface PlayerPolicy {
        String STR_SP_NAME = "policy";
        String BOOL_KEY_AUTOPLAY = "is_autoplay_next";
    }
}
