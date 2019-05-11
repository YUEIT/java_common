package cn.yue.base.middle.mvp.photo;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

import cn.yue.base.middle.mvp.IWaitView;

/**
 * Description :
 * Created by yue on 2019/3/13
 */
public interface IPhotoView extends IWaitView, LifecycleProvider<FragmentEvent> {

    void selectImageResult(List<String> selectList);

    void cropImageResult(String image);

    void uploadImageResult(List<String> serverList);
}
