package com.ruoyi.common.utils;

import java.security.SecureRandom;

/**
 * 邀请码生成工具类
 * 
 * @author ruoyi
 * @date 2026-03-13
 */
public class InvitationCodeUtil {
    
    /**
     * 字符集：排除容易混淆的字符（0、O、1、I、L等）
     * 使用数字2-9和大写字母（排除I、O）
     */
    private static final String CHAR_SET = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    
    /**
     * 邀请码长度
     */
    private static final int CODE_LENGTH = 6;
    
    /**
     * 安全随机数生成器
     */
    private static final SecureRandom RANDOM = new SecureRandom();
    
    /**
     * 生成邀请码
     * 
     * @return 6位邀请码（大写字母+数字组合）
     */
    public static String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = RANDOM.nextInt(CHAR_SET.length());
            code.append(CHAR_SET.charAt(index));
        }
        return code.toString();
    }
    
    /**
     * 验证邀请码格式是否有效
     * 
     * @param code 邀请码
     * @return true-有效，false-无效
     */
    public static boolean isValidCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return false;
        }
        // 检查长度
        if (code.length() != CODE_LENGTH) {
            return false;
        }
        // 检查字符是否都在字符集中
        for (char c : code.toCharArray()) {
            if (CHAR_SET.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }
}
