package com.vv.core.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.Semaphore;

/**
 * @author vv
 * @Description 限流
 * @date 2023/7/28-16:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerServiceSemaphoreWrapper {
    private Semaphore semaphore;

    private int maxNums;

    public ServerServiceSemaphoreWrapper(int maxNums) {
        this.maxNums = maxNums;
    }
}
