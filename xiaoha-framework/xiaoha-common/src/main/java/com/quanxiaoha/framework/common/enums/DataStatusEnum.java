package com.quanxiaoha.framework.common.enums;

import lombok.Getter;

@Getter
public enum DataStatusEnum {
    ENABLE(0, "enable"),
    DISABLE(1, "disable");

    DataStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;
}
