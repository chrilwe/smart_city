package com.iot.smart_city.message.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 任务线程池
 * @author chrilwe
 *
 */
@Component
public class ScMessageTaskPool {
	
	@Value("${message.poolSize}")
	private int poolSize;
	
	/**
	 * 初始化
	 */
	private ScMessageTaskPool() {
		ExecutorService executorService = Executors.newFixedThreadPool(poolSize * 2);
		for(int i = 0; i < poolSize; i++) {
			ScMessageTask1 task1 = new ScMessageTask1();
			ScMessageTask2 task2 = new ScMessageTask2();
			executorService.submit(task1);
			executorService.submit(task2);
		}
	}
}
