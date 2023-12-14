package com.beauty.aide.common.result;


import com.beauty.aide.common.errors.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class PageResultDO<T> {

    private ContentData<T> content;

    private String stackTrace;

    private Boolean success = false;

    private String errorCode;

    private String errorMsg;

    public PageResultDO() {
        super();
    }

    /**
     * 推荐使用：因为EnumCode可以扩展
     */
    public static <T> PageResultDO<T> errorOf(IErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> PageResultDO<T> errorOf(String code, String message) {
        PageResultDO<T> cr = new PageResultDO<>();
        cr.setSuccess(false);
        cr.setErrorCode(code);
        cr.setErrorMsg(message);
        return cr;
    }

    public static <T> PageResultDO<T> succOf(T data) {
        PageResultDO<T> cr = new PageResultDO<T>();
        cr.setContent(new ContentData<>(data));
        cr.setSuccess(true);
        return cr;
    }

    public static PageResultDO succOf() {
        PageResultDO cr = new PageResultDO();
        cr.setSuccess(true);
        return cr;
    }

    public static PageResultDO succOf(int currentPage , int pageSize , int totalCount) {
        PageResultDO cr = new PageResultDO();
        cr.setSuccess(true);
        cr.content = new ContentData<>();
        cr.content.setCurrentPage(currentPage);
        cr.content.setPageSize(pageSize);
        cr.content.setTotalCount(totalCount);
        return cr;
    }

    public static <T>PageResultDO<T> succOf(T data ,int currentPage , int pageSize , int totalCount) {
        PageResultDO<T> cr = new PageResultDO<T>();
        cr.setSuccess(true);
        cr.content = new ContentData<>(data);
        cr.content.setCurrentPage(currentPage);
        cr.content.setPageSize(pageSize);
        cr.content.setTotalCount(totalCount);
        return cr;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ContentData<T> {
        private T data;
        private Integer totalCount;
        private Integer currentPage;
        private Integer pageSize;

        private ContentData(T data) {
            this.data = data;
        }
    }

}
