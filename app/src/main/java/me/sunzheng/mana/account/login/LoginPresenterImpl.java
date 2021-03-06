package me.sunzheng.mana.account.login;

import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.sunzheng.mana.account.AccountApiService;
import me.sunzheng.mana.account.AccountContrant;
import me.sunzheng.mana.account.wrapper.LoginRequest;
import me.sunzheng.mana.account.wrapper.LoginResponse;

/**
 * Created by Sun on 2017/5/22.
 */

public class LoginPresenterImpl implements AccountContrant.Login.Presenter {
    AccountContrant.Login.View mView;
    AccountApiService.Login service;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LoginPresenterImpl(AccountContrant.Login.View loginView, AccountApiService.Login service) {
        this.mView = loginView;
        this.service = service;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void login(String userName, String passWord, boolean isRemembered) {
        mView.showProgressIntractor(true);
        LoginRequest request = new LoginRequest();
        request.name = userName;
        request.password = passWord;
        request.remember = isRemembered;
        Disposable disposable = service.login(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse loginSuccessResponse) throws Exception {
                        mView.showToast(loginSuccessResponse.msg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable != null)
                            Log.e("login e", throwable.getLocalizedMessage());
                        mView.showProgressIntractor(false);
                        mView.showToast(throwable.getLocalizedMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.showProgressIntractor(false);
                        mView.onLoginSuccess();
                    }
                });
        compositeDisposable.add(disposable);
    }
}
