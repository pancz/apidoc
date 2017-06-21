/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.web.controller;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhaoyun.docmanager.biz.schedule.MaintainAllAppTask;
import com.zhaoyun.docmanager.biz.util.DocAssert;
import com.zhaoyun.docmanager.common.knife.result.Result;
import com.zhaoyun.docmanager.common.knife.result.Results;
import com.zhaoyun.docmanager.web.controller.base.BaseController;

/**
 * 
 * @author user
 * @version $Id: AdminController.java, v 0.1 2016年9月1日 下午5:07:27 user Exp $
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    protected static Logger    LOGGER   = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private MaintainAllAppTask maintainAllAppTask;

    private final AtomicBoolean syncLock = new AtomicBoolean(true);

    private final String        secret   = "CYB";

    @RequestMapping("/syncNexus")
    @ResponseBody
    public Result<String> syncNexus(String appKey) {
        DocAssert.notNull(appKey, "appKey can not be null");
        if (!appKey.equals(secret)) {
            return Results.newSuccessResult("失败");
        }
        if (!syncLock.get()) {
            return Results.newSuccessResult("已经在执行了!!");
        }
        synchronized (syncLock) {
            if (syncLock.get()) {
                syncLock.set(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            maintainAllAppTask.syncNexus();
                        } catch (Throwable e) {
                            LOGGER.error("syncNexus error", e);
                        } finally {
                            syncLock.set(true);
                        }

                    }
                }).start();
                return Results.newSuccessResult("成功");
            }
        }

        return Results.newSuccessResult("失败");

    }
}
