package org.example.schedule;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
public class SchedulerExampleServiceImp implements SchedulerService {

    @Override
    public void handle() {
        //todo
        log.debug("定时器要执行的业务");
    }

    @Override
    public int getRate() {
        //返回定时器的频率，单位是秒
        return 10;
    }
}