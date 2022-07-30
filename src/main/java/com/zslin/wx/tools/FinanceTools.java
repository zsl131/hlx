package com.zslin.wx.tools;

public class FinanceTools {

    /**
     * 判断是否有权限
     * @param storeSns
     * @param storeSn
     * @return
     */
    public static boolean hasAuth(String storeSns, String storeSn) {
        if(isNull(storeSn) || isNull(storeSn)) {return false;}
        return storeSns.contains(storeSn);
    }

    /**
     * 有权限返回null 无权限返回redirect
     * @param storeSns
     * @param storeSn
     * @return
     */
    public static String authUrl(String storeSns, String storeSn) {
        if(hasAuth(storeSns, storeSn)) {return null;}
        else return "redirect:/wx/business/error?errCode=-1";
    }

    /** 获取第一个StoreSn */
    public static String getFirstStoreSn(String storeSns) {
        if(isNull(storeSns)) {return "";}
        String [] array = storeSns.split(",");
        if(array.length>=1) {return array[0];}
        else return "";
    }

    public static boolean isNull(String value) {
        return (value==null || "".equals(value.trim()));
    }

    public static boolean isNotNull(String value) {
        return !isNull(value);
    }
}
