package com.stratagile.qlink.entity.qlink;

/**
 * Created by huzhipeng on 2018/3/7.
 */

public class RecordSaveRsp {
    private String txid;
    private String success;

    @Override
    public String toString() {
        return "RecordSaveRsp{" +
                "txid='" + txid + '\'' +
                ", success=" + success +
                '}';
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String isSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
