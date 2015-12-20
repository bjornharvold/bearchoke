/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.dto;

import lombok.Getter;

/**
 * Created by Bjorn Harvold
 * Date: 12/2/15
 * Time: 11:14
 * Responsibility:
 */
public class RemoveRecordResponse {
    @Getter
    private String id;

    public RemoveRecordResponse(String id) {
        this.id = id;
    }
}
