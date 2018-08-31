package com.stratagile.qlink.data.api;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;
import com.stratagile.qlink.entity.AssetsWarpper;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.BetResult;
import com.stratagile.qlink.entity.BuyQlc;
import com.stratagile.qlink.entity.ChainVpn;
import com.stratagile.qlink.entity.ConnectedWifiRecord;
import com.stratagile.qlink.entity.CreateWallet;
import com.stratagile.qlink.entity.EthWalletDetail;
import com.stratagile.qlink.entity.GoogleResult;
import com.stratagile.qlink.entity.ImportWalletResult;
import com.stratagile.qlink.entity.MainAddress;
import com.stratagile.qlink.entity.RaceTimes;
import com.stratagile.qlink.entity.Raw;
import com.stratagile.qlink.entity.Record;
import com.stratagile.qlink.entity.RecordVpn;
import com.stratagile.qlink.entity.RegisterWiFi;
import com.stratagile.qlink.entity.Reward;
import com.stratagile.qlink.entity.ServerTime;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.entity.VertifyVpn;
import com.stratagile.qlink.entity.WifiRegisteResult;
import com.stratagile.qlink.utils.DigestUtils;
import com.stratagile.qlink.utils.ToastUtil;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.HttpException;

/**
 * @author zl
 * @desc 对Request实体(不执行)在执行时所调度的线程，以及得到ResponseBody后根据retCode对Result进行进一步处理
 * @date 2018/06/13.
 */
public class MainHttpAPIWrapper {

    private MainHttpApi mainHttpAPI;

    @Inject
    public MainHttpAPIWrapper(MainHttpApi mHttpAPI) {
        this.mainHttpAPI = mHttpAPI;
    }

    public Observable<WifiRegisteResult> getRegistedSsid(Map map) {
        return wrapper(mainHttpAPI.getRegistedSsid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<RegisterWiFi> registeWWifi(Map map) {
        return wrapper(mainHttpAPI.registeWWifi(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<RegisterWiFi> registeWWifiV3(Map map) {
        return wrapper(mainHttpAPI.registeWWifiV3(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<BaseBack> createWallet(Map map) {
        return wrapper(mainHttpAPI.createWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<CreateWallet> importWallet(Map map) {
        return wrapper(mainHttpAPI.importWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<Balance> getBalance(Map map) {
        return wrapper(mainHttpAPI.getBalance(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<Reward> reward(Map map) {
        return wrapper(mainHttpAPI.reward(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<Raw> getRaw(Map map) {
        return wrapper(mainHttpAPI.getRaw(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<Record> recordQuerys(Map map) {
        return wrapper(mainHttpAPI.recordQuerys(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<ConnectedWifiRecord> recordSave(Map map) {
        return wrapper(mainHttpAPI.recordSave(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<BuyQlc> buyQlc(Map map) {
        return wrapper(mainHttpAPI.buyQlc(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<BuyQlc> trasaction(Map map) {
        return wrapper(mainHttpAPI.trasaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<RegisterWiFi> vpnRegister(Map map) {
        return wrapper(mainHttpAPI.vpnRegister(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<RegisterWiFi> vpnRegisterV2(Map map) {
        return wrapper(mainHttpAPI.vpnRegisterV2(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<ChainVpn> vpnQuery(Map map) {
        return wrapper(mainHttpAPI.vpnQuery(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<VertifyVpn> vertifyVpnName(Map map) {
        return wrapper(mainHttpAPI.vertifyVpnName(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<RecordVpn> vpnRecordSave(Map map) {
        return wrapper(mainHttpAPI.vpnRecordSave(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<String> getNodes() {
        return wrapperGetToxJson(mainHttpAPI.getNodes()).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<GoogleResult> latlngParseCountry(Map map) {
        return wrapperObject(mainHttpAPI.latlngParseCountry(map)).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<ImportWalletResult> batchImportWallet(Map map) {
        return wrapper(mainHttpAPI.batchImportWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<UpLoadAvatar> updateMyAvatar(MultipartBody.Part photo, RequestBody p2pId) {     //String userId, String nickName   userId, nickName
        return wrapper(mainHttpAPI.updateMyAvatar(p2pId, photo)).compose(SCHEDULERS_TRANSFORMER);
    }
    public Observable<UpLoadAvatar> userHeadView(Map map) {
        return wrapper(mainHttpAPI.userHeadView(map)).compose(SCHEDULERS_TRANSFORMER);
    }
    public Observable<BaseBack> heartBeat(Map map) {
        return wrapper(mainHttpAPI.heartBeat(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<BaseBack> updateVpnInfo(Map map) {
        return wrapper(mainHttpAPI.updateVpnInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<BaseBack> updateWiFiInfo(Map map) {
        return wrapper(mainHttpAPI.updateWiFiInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }


    public Observable<AssetsWarpper> getUnspentAsset(Map map) {
        return wrapper(mainHttpAPI.getUnspentAsset(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<BaseBack> sendRawTransaction(Map map) {
        return wrapper(mainHttpAPI.sendRawTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<BaseBack> v2Transaction(Map map) {
        return wrapper(mainHttpAPI.v2Transaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }
    public Observable<MainAddress> getMainAddress(Map map) {
        return wrapper(mainHttpAPI.getMainAddress(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }
    public Observable<BuyQlc> bnb2qlc(Map map) {
        return wrapper(mainHttpAPI.bnb2qlc(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<EthWalletDetail> getEthAddressDetail(String address, Map map) {
        return wrapperETH(mainHttpAPI.getEthAddressDetail(address, map)).compose(SCHEDULERS_TRANSFORMER);
    }
//    public Observable<RaceTimes> getRaceTimes(Map map) {
//        return wrapper(mainHttpAPI.getRaceTimes(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
//    }
//
//    public Observable<ServerTime> getServerTime(Map map) {
//        return wrapper(mainHttpAPI.getServerTime(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
//    }
//    public Observable<BetResult> betRace(Map map) {
//        return wrapper(mainHttpAPI.betRace(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
//    }

    /**
     * 给任何Http的Observable加上通用的线程调度器
     */
    private static final ObservableTransformer SCHEDULERS_TRANSFORMER = new ObservableTransformer() {
        @Override
        public ObservableSource apply(@NonNull Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    private <T extends BaseBack> Observable<T> wrapper(Observable<T> resourceObservable) {
        return resourceObservable
                .flatMap(new Function<T, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(@NonNull T baseResponse) throws Exception {
                        return Observable.create(
                                new ObservableOnSubscribe<T>() {
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                                        if (!baseResponse.getCode().equals("0")) {
                                            String errorTips = baseResponse.getMsg();
                                            if (errorTips.contains("|")) {
                                                errorTips = errorTips.substring(errorTips.indexOf("|") + 1, errorTips.length());
                                            }
                                            ToastUtil.displayShortToast(errorTips);
                                            e.onComplete();
                                        } else {
                                            e.onNext(baseResponse);
                                        }
                                    }
                                });
                    }
                })
                /**
                 * 网络错误： You've encountered a network error!
                 请打开网络：Please open your network.
                 请求超时：The request has timed out.
                 连接失败: Connection failed.
                 请求失败： The request has failed.
                 */
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        e.printStackTrace();
                        String errorText = "";
                        if (e instanceof HttpException) {
//                            errorText = "You've encountered a network error!";
                            return;
                        } else if (e instanceof UnknownHostException) {
                            KLog.i("请打开网络");
                            errorText = "Please open your network.";
                        } else if (e instanceof SocketTimeoutException) {
                            KLog.i("请求超时");
                            errorText = "The request has timed out. ";
                        } else if (e instanceof ConnectException) {
                            KLog.i("连接失败");
                            errorText = "Connection failed.";
                        } else {
                            KLog.i("请求失败");
                            errorText = "The request has failed.";
                        }
                        ToastUtil.displayShortToast(errorText);
                    }
                });
    }

    /**
     * 根据Http的response中关于状态码的描述，自定义生成本地的Exception
     *
     * @param resourceObservable
     * @param <T>
     * @return
     */
    private <T extends GoogleResult> Observable<T> wrapperObject(Observable<T> resourceObservable) {
        return resourceObservable
                .flatMap(new Function<T, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(@NonNull T baseResponse) throws Exception {
                        return Observable.create(
                                new ObservableOnSubscribe<T>() {
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                                        if (baseResponse == null) {

                                        } else {
                                            e.onNext(baseResponse);
                                        }
                                    }
                                });
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 根据Http的response中关于状态码的描述，自定义生成本地的Exception
     *
     * @param resourceObservable
     * @param <T>
     * @return
     */
    private <T extends EthWalletDetail> Observable<T> wrapperETH(Observable<T> resourceObservable) {
        return resourceObservable
                .flatMap(new Function<T, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(@NonNull T baseResponse) throws Exception {
                        return Observable.create(
                                new ObservableOnSubscribe<T>() {
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                                        if (baseResponse == null) {

                                        } else {
                                            e.onNext(baseResponse);
                                        }
                                    }
                                });
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 根据Http的response中关于状态码的描述，自定义生成本地的Exception
     *
     * @param resourceObservable
     * @param <T>
     * @return
     */
    private <T extends String> Observable<T> wrapperGetToxJson(Observable<T> resourceObservable) {
        return resourceObservable
                .flatMap(new Function<T, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(@NonNull T baseResponse) throws Exception {
                        return Observable.create(
                                new ObservableOnSubscribe<T>() {
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                                        if (baseResponse == null) {

                                        } else {
                                            e.onNext(baseResponse);
                                        }
                                    }
                                });
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        e.printStackTrace();
                    }
                });
    }
    //需要额外的添加其他的参数进去，所以把原有的参数和额外的参数通过这个方法一起添加进去.
    public static RequestBody addParams(Map<String, String> data) {
        Map<String, Object> map = new HashMap<>();
        map.put("appid", "WINQ");
        map.put("timestamp", Calendar.getInstance().getTimeInMillis() + "");
        map.put("params", JSONObject.toJSON(data));
        map.put("sign", DigestUtils.getSignature((JSONObject) JSONObject.toJSON(map), "ca799da6e13cbf4732b41f050e6574d2eb17f75107da4f53e82c5e25870a91e7", "UTF-8"));
        KLog.i("传的参数为:" + map);
        MediaType textType = MediaType.parse("text/plain");
        String bodyStr = JSONObject.toJSON(map).toString();
        KLog.i("加密前的:" + bodyStr);
        try {
            bodyStr = URLEncoder.encode(bodyStr, "UTF-8");
            KLog.i("加密后的:" + bodyStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return RequestBody.create(textType, bodyStr);
    }
    public static RequestBody createBody(String jsonBody) {
        MediaType textType = MediaType.parse("text/plain");
        return RequestBody.create(textType, jsonBody);
    }
}
