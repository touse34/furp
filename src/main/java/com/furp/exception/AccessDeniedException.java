package com.furp.exception;

public class AccessDeniedException extends ServiceException {

    //可提供预设信息
//    public AccessDeniedException() {
//        super(DEFAULT_MESSAGE, 403);
//    }



    public AccessDeniedException(String message) {
        super(message, 403);
    }


    //    /**
//     * 3. 提供一個用於「異常鏈」的建構函式
//     * (用於包裹另一個底層異常，提供最完整的除錯資訊)
//     */
//    public AccessDeniedException(String message, Throwable cause) {
//        super(message, 403, cause); // 假設 ServiceException 已支援接收 cause
//    }

}
