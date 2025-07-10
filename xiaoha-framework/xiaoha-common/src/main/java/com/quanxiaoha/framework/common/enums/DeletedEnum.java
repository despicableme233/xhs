package com.quanxiaoha.framework.common.enums;

import lombok.Getter;

@Getter
public enum DeletedEnum {
    DELETED(true, "deleted"),
    NORMAL(false, "normal");

    DeletedEnum(Boolean code, String description) {
        this.code = code;
        this.description = description;
    }

    private final Boolean code;
    private final String description;
}
