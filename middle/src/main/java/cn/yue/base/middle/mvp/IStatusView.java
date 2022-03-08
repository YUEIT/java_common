package cn.yue.base.middle.mvp;

import cn.yue.base.middle.view.load.LoadStatus;
import cn.yue.base.middle.view.load.PageStatus;

/**
 * Description :
 * Created by yue on 2018/11/13
 */
public interface IStatusView {
    void changePageStatus(PageStatus status);
    void changeLoadStatus(LoadStatus status);
}
