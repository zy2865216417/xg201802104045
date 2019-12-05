package cn.edu.sdjzu.xg.bysj.service;


import cn.edu.sdjzu.xg.bysj.dao.DegreeDao;
import cn.edu.sdjzu.xg.bysj.domain.Degree;

import java.sql.SQLException;
import java.util.Collection;

public final class DegreeService {
    private static DegreeDao degreeDao
            = DegreeDao.getInstance();
    private static DegreeService degreeService
            =new DegreeService();
    private DegreeService(){}

    public static DegreeService getInstance(){
        return degreeService;
    }

    public Collection<Degree> findAll() throws SQLException,ClassNotFoundException{
        return degreeDao.findAll();
    }

    public Degree find(Integer id) throws SQLException,ClassNotFoundException{
        return degreeDao.find(id);
    }

    public boolean update(Degree degree) throws SQLException,ClassNotFoundException{
        return degreeDao.update(degree);
    }

    public void add(Degree degree) throws SQLException ,ClassNotFoundException{
        degreeDao.add(degree);
    }

    public boolean delete(Integer id) throws SQLException,ClassNotFoundException{
        Degree degree = this.find(id);
        return degreeDao.delete(degree);
    }

    public boolean delete(Degree degree) throws ClassNotFoundException,SQLException{
        return degreeDao.delete(degree);
    }
}

