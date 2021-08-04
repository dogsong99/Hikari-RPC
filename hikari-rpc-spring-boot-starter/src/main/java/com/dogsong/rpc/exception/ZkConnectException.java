package com.dogsong.rpc.exception;

/**
 * zookeeper 相关异常
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/8/3
 */
public class ZkConnectException extends Exception {

    public ZkConnectException(String message, Throwable cause) {
        super(message, cause);
    }

}
