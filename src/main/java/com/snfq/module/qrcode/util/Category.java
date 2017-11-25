/**
 * sinafenqi.com
 * Copyright (c) 2017 All Rights Reserved.
 */
package com.snfq.module.qrcode.util;

import java.util.EnumMap;

import static com.snfq.module.qrcode.util.Input.*;

/**
 * 对自动售货机的状态分类
 *
 * @author fujin
 * @version $Id: Category.java, v 0.1 2017-11-24 13:58 boyuan Exp $$
 */
public enum Category {
    /**放入钞票**/
    MONEY(NICKEL,DIME,QUARTER,DOLLAR),

    /**选择商品**/
    ITEM_SELECTION(TOOTHPASTE,CHIPS,SODA,SOAP),

    /**退出**/
    QUIT_TRANSACTION(ABORT_TRANSACTION),

    /**关机**/
    SHUT_DOWN(STOP);

    private Input[] values;


    Category(Input... types){values = types;}

    public static EnumMap<Input, Category> categories = new EnumMap<>(Input.class);

    public Input[] getValues(){
        return values;
    }
    //初始化自动售货机状态集合
    static {
        for (Category  c : Category.class.getEnumConstants()) {
            for(Input input : c.values){
                categories.put(input, c);
            }
        }
    }

    /**返回该操作项所属状态**/
    public static Category categorize(Input input){
        return categories.get(input);
    }
}

