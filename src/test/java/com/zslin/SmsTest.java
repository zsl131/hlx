package com.zslin;

import com.zslin.dto.MoneyBagDto;
import com.zslin.jdbc.JDBCConfig;
import com.zslin.sms.tools.SmsTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class SmsTest {

    @Autowired
    private SmsTools smsTools;

    @Autowired
    private JDBCConfig jdbcConfig;

    @Test
    public void testSendBatch() {
        List<MoneyBagDto> datas = queryRemoteData();
        for(MoneyBagDto dto : datas) {
            smsTools.sendMsg(5, dto.getPhone(), "money", dto.getMoney()+"");
        }
    }

    @Test
    public void testSendSingle() {
        smsTools.sendMsg(5, "13508709330", "money", "1234");
    }

    @Test
    public void testQueryMoneyBag() {
        //System.out.println(jdbcConfig);
        List<MoneyBagDto> datas = queryRemoteData();
        for(MoneyBagDto dto : datas) {
            System.out.println(dto);
        }
    }

    /**
     * 获取远程钱包
     * @return
     */
    private List<MoneyBagDto> queryRemoteData() {
        String driverClassName = "com.mysql.cj.jdbc.Driver";	//启动驱动
        Connection con = null;
        PreparedStatement pstmt = null;	//使用预编译语句
        ResultSet rs = null;	//获取的结果集
        List<MoneyBagDto> result = new ArrayList<>();
        try {
            Class.forName(driverClassName); //执行驱动
            con = DriverManager.getConnection(jdbcConfig.getDbUrl(), jdbcConfig.getDbname(), jdbcConfig.getPassword()); //获取连接

            String sql = "select store_name, name, phone, money from m_moneybag where money>0 order by store_name, money desc";
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while(rs.next()) {
                System.out.println(rs.getString(1)+", "+rs.getString(2)+", "+rs.getString(3));
                result.add(new MoneyBagDto(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getInt(4)));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Test
    public void test01() {
        Integer surplus = smsTools.surplus();
        System.out.println(surplus);
    }
}
