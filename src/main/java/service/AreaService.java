package service;

import java.util.List;

import dao.AreaDao;
import dao.impl.AreaDaoImpl;
import member.bean.Area;

public class AreaService {
    private AreaDao dao;
    
    public AreaService() {
        dao = new AreaDaoImpl();
    }
    
    public List<Area> selectAll() {
        return dao.selectAll();
    }
}