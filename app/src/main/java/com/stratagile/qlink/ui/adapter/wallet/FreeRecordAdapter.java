package com.stratagile.qlink.ui.adapter.wallet;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.FreeRecord;

import java.util.List;

public class FreeRecordAdapter extends BaseQuickAdapter<FreeRecord.DataBean, BaseViewHolder> {

    public FreeRecordAdapter(@Nullable List<FreeRecord.DataBean> data) {
        super(R.layout.item_free_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FreeRecord.DataBean item) {
        helper.setText(R.id.tv_asset_name, item.getAssetName())
                .setText(R.id.tv_reward_time, item.getTime());
        helper.setText(R.id.tv_free_count, item.getNum());
        if (helper.getLayoutPosition() == 0) {
            helper.setVisible(R.id.tv_line, false);
        } else {
            helper.setVisible(R.id.tv_line, true);
        }
        if (item.getFreeType() == 1) {
            //加次数
            helper.setTextColor(R.id.tv_free_count, mContext.getResources().getColor(R.color.mainColor));
        } else {
            helper.setTextColor(R.id.tv_free_count, mContext.getResources().getColor(R.color.color_ababab));
        }
    }
}
