package com.stratagile.qlink.ui.activity.vpn.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.RegisterVpn;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.entity.UpdateVpn;
import com.stratagile.qlink.entity.VertifyVpn;
import com.stratagile.qlink.ui.activity.vpn.RegisteVpnActivity;
import com.stratagile.qlink.ui.activity.vpn.contract.RegisteVpnContract;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: presenter of RegisteVpnActivity
 * @date 2018/02/06 15:41:02
 */
public class RegisteVpnPresenter implements RegisteVpnContract.RegisteVpnContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final RegisteVpnContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private RegisteVpnActivity mActivity;
    private VpnEntity addVpnEntity;

    @Inject
    public RegisteVpnPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull RegisteVpnContract.View view, RegisteVpnActivity activity) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
        this.mActivity = activity;
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (!mCompositeDisposable.isDisposed()) {
             mCompositeDisposable.dispose();
        }
    }

    @Override
    public void registerVpn(Map map, String vpnName, VpnEntity vpnEntity) {

//        mView.showProgressDialog();
        Disposable disposable = httpAPIWrapper.vpnRegisterV2(map)
                .subscribe(new Consumer<RegisterVpn>() {
                    @Override
                    public void accept(RegisterVpn result) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.closeProgressDialog();
                        mView.registVpnSuccess();
                        TransactionRecord recordSave = new TransactionRecord();
                        recordSave.setTxid(result.getRecordId());
                        recordSave.setExChangeId(result.getRecordId());
                        recordSave.setAssetName(vpnName);
                        recordSave.setTransactiomType(5);
                        recordSave.setTimestamp(Calendar.getInstance().getTimeInMillis());
                        recordSave.setIsMainNet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
                        AppConfig.getInstance().getDaoSession().getTransactionRecordDao().insert(recordSave);
                        vpnEntity.setVpnName(result.getVpnName());
                        vpnEntity.setCountry(result.getCountry());
                        vpnEntity.setIsConnected(false);
                        vpnEntity.setP2pId(result.getP2pId());
                        vpnEntity.setProfileLocalPath(result.getProfileLocalPath());
                        if(ConstantValue.windowsVpnName.equals(vpnName))
                        {
                            vpnEntity.setAvatar(SpUtil.getString(AppConfig.getInstance(), ConstantValue.myAvatarPath, ""));
                            ConstantValue.windowsVpnName ="";
                        }else{
                            vpnEntity.setAvatar(result.getImgUrl());
                        }
                        vpnEntity.setAddress(result.getAddress());
                        vpnEntity.setIsInMainWallet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
                        vpnEntity.setOnline(true);
                        vpnEntity.setIsMainNet(SpUtil.getBoolean(AppConfig.getInstance(),ConstantValue.isMainNet,false));
                        AppConfig.getInstance().getDaoSession().getVpnEntityDao().insert(vpnEntity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        mView.closeProgressDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                        mView.closeProgressDialog();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void vertifyVpnName(Map map) {
        Disposable disposable = httpAPIWrapper.vertifyVpnName(map)
                .subscribe(new Consumer<VertifyVpn>() {
                    @Override
                    public void accept(VertifyVpn vertifyVpn) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.vertifyVpnBack(vertifyVpn);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        mView.closeProgressDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                        mView.closeProgressDialog();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getBalance(Map map) {
        Disposable disposable = httpAPIWrapper.getBalance(map)
                .subscribe(new Consumer<Balance>() {
                    @Override
                    public void accept(Balance balance) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.onGetBalancelSuccess(balance);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        //mView.closeProgressDialog();
                        //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void updateVpnInfo(Map map) {
        mView.showProgressDialog();
        Disposable disposable = httpAPIWrapper.updateVpnInfo(map)
                .subscribe(new Consumer<UpdateVpn>() {
                    @Override
                    public void accept(UpdateVpn updateVpn) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.updateVpnInfoSuccess(updateVpn.getData().getCountry());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        mView.closeProgressDialog();
                        //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        mView.closeProgressDialog();
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }
    @Override
    public void upLoadImg(String p2pIdPc) {
        String myAvater = SpUtil.getString(mActivity, ConstantValue.myAvaterUpdateTime, "");
        if(myAvater.equals(""))
        {
            return;
        }
        File upLoadFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/" + myAvater + ".jpg");
        if(!upLoadFile.exists())
        {
            return;
        }
        RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), upLoadFile);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("head", SpUtil.getString(mActivity, ConstantValue.myAvaterUpdateTime, "") + ".jpg", image);
        Disposable disposable = httpAPIWrapper.updateMyAvatar(photo, RequestBody.create(MediaType.parse("text/plain"), p2pIdPc))     //userId, nickName
                .subscribe(new Consumer<UpLoadAvatar>() {
                    @Override
                    public void accept(UpLoadAvatar upLoadAvatar) throws Exception {
                        KLog.i("onSucess");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                    }
                });
    }
    @Override
    public void getScanPermission() {
        AndPermission.with(((Activity) mView))
                .requestCode(101)
                .permission(
                        Manifest.permission.CAMERA
                )
                .rationale((requestCode, rationale) -> {
                            AndPermission
                                    .rationaleDialog((((Activity) mView)), rationale)
                                    .setTitle(AppConfig.getInstance().getResources().getString(R.string.Permission_Requeset))
                                    .setMessage(AppConfig.getInstance().getResources().getString(R.string.We_Need_Some_Permission_to_continue))
                                    .setNegativeButton(AppConfig.getInstance().getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.permission_denied));
                                        }
                                    })
                                    .show();
                        }
                )
                .callback(permission)
                .start();
    }
    private PermissionListener permission = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if (requestCode == 101) {
                mView.getScanPermissionSuccess();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 101) {
                KLog.i("权限申请失败");
                ToastUtil.show(AppConfig.getInstance(), AppConfig.getInstance().getResources().getString(R.string.permission_denied));
            }
        }
    };
    @Override
    public void preAddVpn(VpnEntity vpnEntity) {
        addVpnEntity = vpnEntity;
        Map<String, String> map = new HashMap<>();
        map.put("address", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(AppConfig.getInstance(), ConstantValue.currentWallet, 0)).getAddress());
        getBalance(map);
    }
}