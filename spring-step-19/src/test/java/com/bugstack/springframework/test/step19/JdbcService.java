package com.bugstack.springframework.test.step19;

import com.bugstack.springframework.jdbc.core.JdbcTemplate;
import com.bugstack.springframework.tx.transaction.annoation.Transactional;

public class JdbcService {

    @Transactional(rollbackFor = Exception.class)
    public void saveData(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("insert into account (name, balance) values ('sxxx','1000') ");
        jdbcTemplate.execute("insert into account (name, balance) values ('184172133','3000') ");


    }
}
