package com.krista.spring.boot.dto.enumeration;

/**
 * 菜单类型
 *
 * @author jeikerxiao
 * @date 2018/6/27 下午5:23
 */
public enum MenuType {
    /**
     * 目录
     */
    CATALOG(0),
    /**
     * 菜单
     */
    MENU(1),
    /**
     * 按钮
     */
    BUTTON(2);

    private int value;

    MenuType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
