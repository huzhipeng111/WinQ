package com.stratagile.qlink.ui.activity.test.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for TestActivity
 * @Description: $description
 * @date 2018/08/31 17:23:42
 */
public interface TestContract {
    interface View extends BaseView<TestContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface TestContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}