package com.stratagile.qlink.guideview.compnonet;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.stratagile.qlink.R;
import com.stratagile.qlink.guideview.Component;

/**
 * Created by binIoter on 16/6/17.
 */
public class ChooseCountryComponent implements Component {

  @Override
  public View getView(LayoutInflater inflater) {

    LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.layout_componoent_choose_country, null);
    return ll;
  }

  @Override
  public int getAnchor() {
    return Component.ANCHOR_OVER;
  }

  @Override
  public int getFitPosition() {
    return Component.FIT_START;
  }

  @Override
  public int getXOffset() {
    return 0;
  }

  @Override
  public int getYOffset() {
    return 20;
  }
}
