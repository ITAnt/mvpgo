package com.miekir.mt.ui.java;

import com.miekir.mvp.presenter.BasePresenter;

/**
 * @author : 詹子聪
 * @date : 2021-7-9 21:57
 */
public class JavaPresenter extends BasePresenter<IJavaView> {

    public void goLogin() {
//        NetObserver<String> observer = new NetObserver
//                .Builder<String>(this)
//                .loadType(LoadingType.STICKY)
//                .onResult((success, result, code, message) -> {
//                    if (getView() != null) {
//                        getView().onLoginResult();
//                    }
//                    return null;
//                }).create();
//        CommonObserver<String> commonObserver = new CommonObserver.Builder<String>(this)
//                .onCancel(() -> {
//
//                }).onResult((success, result, code, message) -> {
//
//                }).create();
//        /*NetObserver<String> observer = new NetObserver
//                .Builder<String>(this)
//                .loadType(LoadingType.STICKY)
//                .onResult(getView()::onLoginResult).create();*/
//        RxTask.launchRxTask(this, ApiHelper.INSTANCE.getDefaultNetRx().testNetBean());
    }
}
