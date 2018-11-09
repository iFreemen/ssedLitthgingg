package com.heqichao.springBootDemo.module.control;

import com.heqichao.springBootDemo.base.control.BaseController;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.module.service.LightningLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by heqichao on 2018-7-15.
 */
@RestController
@RequestMapping(value = "/service")
public class LightningLogController extends BaseController{

    @Autowired
    private LightningLogService lightningLogService;

    @RequestMapping(value = "/queryLightLogs")
    ResponeResult queryLightLogs() {
        return new ResponeResult(lightningLogService.queryAll());
    }

    @RequestMapping(value = "/queryLightCountByYear")
    ResponeResult queryLightCountByYear() {
        return new ResponeResult(lightningLogService.queryLightCountByYear());
    }

    @RequestMapping(value = "/queryLightChart")
    ResponeResult queryLightChart() {
        return new ResponeResult(lightningLogService.queryLightChart());
    }

    @RequestMapping(value = "/deleteLightLog")
    ResponeResult deleteAll() {
        lightningLogService.deleteAll();
        return new ResponeResult();
    }
}
