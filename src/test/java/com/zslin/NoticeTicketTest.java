package com.zslin;

import com.zslin.basic.tools.NormalTools;
import com.zslin.dto.QwzwTicketDto;
import com.zslin.jdbc.JDBCConfig;
import com.zslin.wx.tools.EventTools;
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
public class NoticeTicketTest {

    @Autowired
    private JDBCConfig jdbcConfig;

    @Autowired
    private EventTools eventTools;

    @Test
    public void testSendSingle() {
        String openid = "o_TZkwbz0pzuCTmrWqMGNHriMHTo";
        eventTools.eventRemind(openid, "卡券通知", "卡券使用提醒", "2021-3-21", "亲爱哒，您在【签王之王】的【3】张全额券今天开始可以使用了哦!，请在2021年4月20号以前使用，过期将自动作废哦！", "/wx/account/ticket");
    }

    private void sendNotice(String openid, String date, Integer amount) {
        eventTools.eventRemind(openid, "卡券通知", "卡券使用提醒", date, "亲爱哒，您在【签王之王】的【"+amount+"】张全额券今天开始可以使用了哦!，请在2021年4月20号以前使用，过期将自动作废哦！", "/wx/account/ticket");
    }

    @Test
    public void testRun() {
        List<QwzwTicketDto> data = queryData();
        System.out.println(data);
        String date = NormalTools.curDate("yyyy-MM-dd");
        for(QwzwTicketDto dto : data) {
            sendNotice(dto.getOpenid(), date, dto.getAmount());
        }
    }

    private List<QwzwTicketDto> queryData() {
        String driverClassName = "com.mysql.cj.jdbc.Driver";	//启动驱动
        Connection con = null;
        PreparedStatement pstmt = null;	//使用预编译语句
        ResultSet rs = null;	//获取的结果集
        List<QwzwTicketDto> result = new ArrayList<>();
        try {
            Class.forName(driverClassName); //执行驱动
            con = DriverManager.getConnection(jdbcConfig.getDbUrl(), jdbcConfig.getDbname(), jdbcConfig.getPassword()); //获取连接

            String sql = "select openid,nickname,count(id) from wx_hlx_ticket where store_sn='qwzw-auto' and type='10' and ticket_worth=69 and status='0' group by openid";
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while(rs.next()) {
                //System.out.println(rs.getString(1)+", "+rs.getString(2)+", "+rs.getString(3));
                result.add(new QwzwTicketDto(rs.getString(1), rs.getString(2), rs.getInt(3)));
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
}
