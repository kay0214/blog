package com.sandman.blog.entity.common;

public class SortParam {
    private Integer pageNumber;
    private Integer startRow;
    private Integer size;
    private String sortType;
    private String order;
    private String cause;

    public SortParam() {
    }
    public SortParam(Integer pageNumber, Integer size,String cause) {
        this.setCause(cause);
        this.setPageNumber(pageNumber);
        this.setSize(size);
        this.setStartRow();
    }
    public SortParam(Integer pageNumber, Integer size) {
        this.setPageNumber(pageNumber);
        this.setSize(size);
        this.setStartRow();
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        pageNumber = (pageNumber==null || pageNumber<1)?1:pageNumber;
        this.pageNumber = pageNumber;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        size = (size==null || size<0)?10:size;
        this.size = size;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        sortType = (sortType==null || "".equals(sortType))?"desc":sortType;
        this.sortType = sortType;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        order = (order==null || "".equals(order))?"createTime":order;
        this.order = order;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow() {
        this.startRow = (this.pageNumber - 1) * this.size;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "SortParam{" +
                "pageNumber=" + pageNumber +
                ", startRow=" + startRow +
                ", size=" + size +
                ", sortType='" + sortType + '\'' +
                ", order='" + order + '\'' +
                ", cause='" + cause + '\'' +
                '}';
    }
}
