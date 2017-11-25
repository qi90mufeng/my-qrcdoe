/**
 * sinafenqi.com
 * Copyright (c) 2017 All Rights Reserved.
 */
package com.snfq.module.qrcode.util;

import java.util.Random;

/**
 * @author fujin
 * @version $Id: Enums.java, v 0.1 2017-11-24 14:04 boyuan Exp $$
 */
public class Enums {
    private static int random=(int)(Math.random()*10);// 生成种子
    private static Random rand = new Random(random);

    public static <T extends Enum<T>> T random(Class<T> ec) {
        return random(ec.getEnumConstants());
    }

    public static <T> T random(T[] values) {
        return values[rand.nextInt(values.length)];
    }
}
