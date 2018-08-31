package com.stratagile.qlink.ui.activity.eth;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.FullWallet;
import com.stratagile.qlink.interfaces.StorableWallet;
import com.stratagile.qlink.ui.activity.eth.component.DaggerEthWalletComponent;
import com.stratagile.qlink.ui.activity.eth.contract.EthWalletContract;
import com.stratagile.qlink.ui.activity.eth.module.EthWalletModule;
import com.stratagile.qlink.ui.activity.eth.presenter.EthWalletPresenter;
import com.stratagile.qlink.ui.adapter.eth.EthWalletsAdapter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.OwnWalletUtils;
import com.stratagile.qlink.utils.eth.Settings;
import com.stratagile.qlink.utils.eth.WalletStorage;

import org.web3j.crypto.CipherException;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: $description
 * @date 2018/05/24 10:30:26
 * 以太坊钱包
 */

public class EthWalletActivity extends BaseActivity implements EthWalletContract.View {

    @Inject
    EthWalletPresenter mPresenter;
    EthWalletsAdapter ethWalletsAdapter;
    @BindView(R.id.tv_new_wallet)
    TextView tvNewWallet;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_import)
    TextView tvImport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_eth_wallet);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.eth_wallet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {

        ArrayList<StorableWallet> wallets = WalletStorage.getInstance(this).get();
//        ToastUtil.displayShortToast(wallets.size() + "");
        ethWalletsAdapter = new EthWalletsAdapter(wallets);
        recyclerView.setAdapter(ethWalletsAdapter);
        ethWalletsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(EthWalletActivity.this, BnbToQlcActivity.class);
                intent.putExtra("walletAddress", ethWalletsAdapter.getItem(position).getPubKey());
                SpUtil.putInt(EthWalletActivity.this, ConstantValue.currentEthWallet, position);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEthWalletComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .ethWalletModule(new EthWalletModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EthWalletContract.EthWalletContractPresenter presenter) {
        mPresenter = (EthWalletPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.tv_new_wallet, R.id.tv_import})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_new_wallet:
                KLog.i("点击新建钱包");
                showProgressDialog();
                new AsyncTask<String, String, String>() {

                    @Override
                    protected String doInBackground(String... objects) {
                        String walletAddress = "";
                        try {
                            walletAddress = OwnWalletUtils.generateNewWalletFile("11111111", new File(EthWalletActivity.this.getFilesDir(), ""), true);
                        } catch (CipherException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InvalidAlgorithmParameterException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchProviderException e) {
                            e.printStackTrace();
                        }
                        WalletStorage.getInstance(EthWalletActivity.this).add(new FullWallet("0x" + walletAddress, walletAddress), EthWalletActivity.this);
//                AddressNameConverter.getInstance(this).put("0x" + walletAddress, "Wallet " + ("0x" + walletAddress).substring(0, 6), this);
                        Settings.walletBeingGenerated = false;
                        KLog.i("创建钱包成功: " + walletAddress);
                        return walletAddress;
                    }

                    @Override
                    protected void onPostExecute(String o) {
//                        ToastUtil.displayShortToast(o);
                        closeProgressDialog();
                        ArrayList<StorableWallet> wallets = WalletStorage.getInstance(EthWalletActivity.this).get();
                        ToastUtil.displayShortToast(wallets.size() + "");
                        ethWalletsAdapter.setNewData(wallets);
                    }
                }.execute();
                break;
            case R.id.tv_import:
                startActivity(new Intent(this, ImportEthWalletActivity.class));
                break;
            default:
                break;
        }
    }
}