package io.xone.chain.onenft.common.utils;

import java.math.BigDecimal;

/**
 * 链相关工具类
 */
public class ChainUtils {

    /**
     * 10^9
     */
    private static final BigDecimal DIVISOR = BigDecimal.valueOf(1000000000);

    /**
     * 解析价格，除以10^9
     * @param priceObj 原始价格
     * @return 转换后的价格
     */
    public static BigDecimal parsePrice(Object priceObj) {
        if (priceObj == null) {
            return null;
        }
        try {
            return new BigDecimal(priceObj.toString()).divide(DIVISOR);
        } catch (Exception e) {
            return null;
        }
    }
}
