package com.zslin.meituan.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zslin.meituan.dto.ReturnDto;
import com.zslin.wx.tools.JsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/7/5 16:53.
 */
@Component
public class MeituanHandlerTools {

    @Autowired
    private MeituanParamsTools meituanParamsTools;

    @Value("${eid}")
    private Long eid;

    @Value("${ename}")
    private String ename;

    /**
     * 验券准备
     * @param code 12位纯数字券码
     * @return
     */
    public ReturnDto handlerReady1(String code) {
        try {
            /*RequestSysParams requestSysParams = meituanParamsTools.getSysParams();
            CipCaterCouponConsumptionPrepareRequest request = new CipCaterCouponConsumptionPrepareRequest();
            request.setRequestSysParams(requestSysParams);
            request.setCouponCode(code);
            String str = request.doRequest();

            Object data = JsonTools.getJsonParam(str, "data");
            Integer resCode = 1;
            String resMsg = "操作成功";
            if(data==null || "".equalsIgnoreCase(data.toString())) {
                resCode = 0; resMsg = JsonTools.getJsonParam(JsonTools.getJsonParam(str, "error").toString(), "message").toString();
                return new ReturnDto(resCode, resMsg, null);
            } else {
                ReadyDto dto = JSON.toJavaObject(JSON.parseObject(data.toString()), ReadyDto.class);
                return new ReturnDto(resCode, resMsg, dto);
            }*/
            return new ReturnDto(0, "", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnDto(0, e.getMessage(), null);
        }
    }

    /**
     * 执行验证
     * @param code 美团券编号
     * @param orderNo 收银系统订单编号
     * @return
     */
    public ReturnDto handlerCheck1(String code, Integer count, String orderNo) {
        if(code==null || "".equals(code.trim())) {return null;}
        try {
            /*RequestSysParams requestSysParams = meituanParamsTools.getSysParams();
            CipCaterCouponConsumeRequest request = new CipCaterCouponConsumeRequest();
            request.setRequestSysParams(requestSysParams);
            request.seteId(eid);
            request.seteName(ename);
            request.seteOrderId(Long.parseLong(orderNo));
            request.setCouponCode(code);
            request.setCount(count);
            String str = request.doRequest();

//            System.out.println("========="+str);

            Object data = JsonTools.getJsonParam(str, "data");
            Integer resCode = 1;
            String resMsg = "操作成功";
            if(data==null || "".equalsIgnoreCase(data.toString())) {
                resCode = 0; resMsg = JsonTools.getJsonParam(JsonTools.getJsonParam(str, "error").toString(), "message").toString();
                return new ReturnDto(resCode, resMsg, null);
            } else {
//                ReadyDto dto = JSON.toJavaObject(JSON.parseObject(data.toString()), ReadyDto.class);
                return new ReturnDto(resCode, resMsg, buildCheckNos(data.toString()));
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String buildCheckNos1(String data) {
        JSONArray array = JSON.parseArray(JsonTools.getJsonParam(data, "couponCodes").toString());
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<array.size(); i++) {
//            System.out.println("==="+array.get(i).toString());
            sb.append(array.get(i).toString()).append(",");
        }
        return sb.toString();
    }

    /**
     * 撤消验证
     * @param code 美团券验证码
     * @return
     */
    public ReturnDto handlerUndo1(String code) {
        try {
            /*RequestSysParams requestSysParams = meituanParamsTools.getSysParams();
            CipCaterCouponConsumptionCancelRequest request = new CipCaterCouponConsumptionCancelRequest();
            request.setRequestSysParams(requestSysParams);
            request.seteId(eid);
            request.seteName(ename);
            request.setType(1);
            request.setCouponCode(code);
            String str = request.doRequest();
            System.out.println("++++++++++"+str);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
