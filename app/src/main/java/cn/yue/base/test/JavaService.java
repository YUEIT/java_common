package cn.yue.base.test;

import cn.yue.base.middle.net.wrapper.BaseListBean;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public interface JavaService {

    @GET("/supplier/todo/order/list")
    Single<BaseListBean<OrderBean>> getOrderList(@Query("token")String token,
                                                 @Query("deliveryDate") String deliveryDate,
                                                 @Query("pageNo")String pageNo,
                                                 @Query("pageSize")int pageSize);

}
