package cn.yue.base.middle.net.wrapper;


import java.util.List;

public class BaseListJavaBean<T> {
    private List<T> list;       // 瀑布式
    private List<T> page_list;  //分页式
    private List<T> dataList;
    private int total;
    private int page_count;
    private int pageCount;
    private int page_size;
    private int pageSize;
    private int page_no;
    private int pageNo;
    private int count;//
    private String nt;	//版本Id，用作下一页版本号，null表示没有下一页
    private String pt;	//版本Id，用作上一页版本号

    protected List<T> getRealList() {
        if (list != null){
            return list;
        }else if (dataList != null){
            return dataList;
        }else if (page_list != null){
            return page_list;
        }
        return null;
    }

    protected int getRealTotal() {
        return count == 0 ? total : count;
    }

    protected int getRealPageCount() {
        if (pageCount > 0) {
            return pageCount;
        } else if (page_count > 0) {
            return page_count;
        }
        return 0;
    }

    protected int getRealPageSize() {
        if (pageSize > 0) {
            return pageSize;
        } else if (page_size > 0) {
            return page_size;
        }
        return 0;
    }

    protected int getRealPageNo() {
        if (pageNo > 0) {
            return pageNo;
        } else if (page_no > 0) {
            return page_no;
        }
        return 0;
    }

    protected int getRealCount() {
        return count == 0 ? total : count;
    }

    protected String getNt() {
        return nt;
    }

    protected String getPt() {
        return pt;
    }


    public boolean isDataEmpty(){
        return getRealList()==null||getRealList().size()==0;
    }
}
