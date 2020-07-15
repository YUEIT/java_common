package cn.yue.base.common.activity.rx;


import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;

import io.reactivex.Observable;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.CheckReturnValue;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public interface ILifecycleProvider<E> extends LifecycleObserver {

    @CheckReturnValue
    Observable<E> lifecycle();

    @CheckReturnValue
    <T> LifecycleTransformer<T> bindUntilEvent(@NonNull E event);

    @CheckReturnValue
    <T> LifecycleTransformer<T> bindToLifecycle();

    @CheckReturnValue
    <T> SingleTransformer<T, T> toBindLifecycle();

    @CheckReturnValue
    <T> SingleTransformer<T, T> toBindLifecycle(E e);
}
