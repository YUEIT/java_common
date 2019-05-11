package cn.yue.base.common.activity;

import com.trello.rxlifecycle2.LifecycleProvider;

import io.reactivex.SingleTransformer;
import io.reactivex.annotations.CheckReturnValue;

/**
 * Description :
 * Created by yue on 2019/3/25
 */
public interface ILifecycleProvider<E> extends LifecycleProvider<E> {

    @CheckReturnValue
    <T> SingleTransformer<T, T> toBindLifecycle();

    @CheckReturnValue
    <T> SingleTransformer<T, T> toBindLifecycle(E e);
}
