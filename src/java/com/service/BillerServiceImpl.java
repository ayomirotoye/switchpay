/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.model.Billers;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.Property;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Supersoft Technology
 */
@Service
public class BillerServiceImpl implements BillerService{
    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;
    
     @Override
    public List Billers() {
        // List<String> types = new ArrayList
        List<Billers> types;
        String sql = "SELECT biller_name FROM tbl_billers";
        System.out.println("sqql:" + sql);

        try {
            types = jdbcTemplate.query(sql, (ResultSet rs, int i) -> {
                Billers biller = new Billers();
                biller.setBiller_name(rs.getString("biller_name"));
                return biller;
            });
        } catch (DataAccessException e) {
            types = Collections.EMPTY_LIST;
        }
        return types;
    }  
    
}
