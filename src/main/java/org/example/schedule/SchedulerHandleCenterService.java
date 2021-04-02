package org.example.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 定时器控制中心
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerHandleCenterService {
    private final ApplicationContext applicationContext;
    private Map<String, LocalDateTime> cacheLastExecuteTime;
    private Map<String, SchedulerService> beansOfType;

    @PostConstruct
    public void init() {
        beansOfType = applicationContext.getBeansOfType(SchedulerService.class);
        cacheLastExecuteTime = new HashMap<>(beansOfType.size() * 4 / 3 + 1);
        LocalDateTime now = LocalDateTime.now();
        beansOfType.forEach((k, v) -> cacheLastExecuteTime.put(k, now));
    }

    /*定时分发器(1s一次)，使用异步调度，节省资源*/
    @Scheduled(cron = "0/1 * * * * *")
    public void handle() {
        beansOfType.forEach((key, value) -> {
            try {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime lastExecuteTime = cacheLastExecuteTime.get(key);
                if (lastExecuteTime.plusSeconds(value.getRate()).isBefore(now)) {
                    cacheLastExecuteTime.put(key, now);
                    log.debug("timer is executing {}", key);
                    value.handleAsync();
                }
            } catch (Exception e) {
                log.error("OperationHandleCenterService error: {} , The Operate service is {}", e.getMessage(), key, e);
            }
        });
    }
}