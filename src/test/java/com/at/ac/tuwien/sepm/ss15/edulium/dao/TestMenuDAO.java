package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by - on 11.05.2015.
 */
public class TestMenuDAO extends AbstractDAOTest {
    @Autowired
    private DAO<Menu> menuDAO;
}
