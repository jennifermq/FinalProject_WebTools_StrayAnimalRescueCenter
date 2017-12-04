package com.qing.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.qing.exception.RescueRecordException;
import com.qing.pojo.RescueRecord;

public class RescueRecordDAO extends DAO {
	public void addRescueRecord(RescueRecord record) throws RescueRecordException{
		try{
			begin();
			Session session = getSession();
			session.save(record);
			commit();
		}
		catch(HibernateException he){
			rollback();
			throw new RescueRecordException("Cannot add rescue record: "+he.getMessage());
		}
	}
	
	public List<RescueRecord> getRescueRecordList() throws RescueRecordException{
		try{
			begin();
			Query q = getSession().createQuery("from RescueRecord");
			List<RescueRecord> result = q.list();
			commit();
			close();
			System.out.println("count:"  + result.size());
			return result;
		}
		catch(HibernateException he)	{
			rollback();
			throw new RescueRecordException("Cannot list rescue records: " + he.getMessage());
		}
	}
	
//	public Animal getAnimalById(String id)	throws RescueRecordException{
//		try{
//			begin();
//			Query q = getSession().createQuery("from aniaml_table where animalid = :id");
//			q.setString("id", id);
//			Animal a = (Animal) q.uniqueResult();
//			commit();
//			return a;
//			
//		}
//		catch(HibernateException he){
//			rollback();
//			throw new AnimalException("Error "+he.getMessage());
//		}
//	}
}
