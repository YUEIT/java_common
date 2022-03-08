package cn.yue.base.middle.mvp;

import java.util.List;

import cn.yue.base.middle.mvp.components.data.Loader;

/**
 * Description :
 * Created by yue on 2022/3/8
 */
 
public interface IListView<S> extends IBaseView {

    Loader getLoader();

    void setData(List<S> list);
}
