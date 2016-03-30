package com.panda.zh.erp.common;

/**
 * @author Panda.Z
 */
public interface ErrorCode {

    enum Success {

        /**
         * 成功状态
         */
        SUCCESS(200);

        private final int code;

        Success(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
