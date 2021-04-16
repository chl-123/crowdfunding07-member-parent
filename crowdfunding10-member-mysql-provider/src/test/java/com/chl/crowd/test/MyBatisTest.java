package com.chl.crowd.test;

import com.chl.crowd.entity.po.Member;
import com.chl.crowd.mapper.MemberMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBatisTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    MemberMapper memberMapper;
    private Logger logger= LoggerFactory.getLogger(MyBatisTest.class);
    @Test
    public void testConnection() throws SQLException {
        Connection connection=dataSource.getConnection();
        logger.debug(connection.toString());
    }
    @Test
    public void testMapper(){
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String source="123456789";
        String encode=passwordEncoder.encode(source);
        memberMapper.insert(new Member(null,"jack",encode,"接口","jack@qq.com",1,1,"接口","123456789",2));
    }

}
