package cn.yue.base.middle.net.upload;

import java.util.List;

/**
 * 介绍：批量上传图片返回结果
 * 作者：yangguodong
 * 邮箱：yangguodong@imcoming.cn
 * 时间: 16/6/29 下午7:30
 */
public class ImageResultListData {
    private int error;
    private List<ImageResult> data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<ImageResult> getData() {
        return data;
    }

    public void setData(List<ImageResult> data) {
        this.data = data;
    }
}
