package com.qing.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.qing.exception.TreatmentRecordException;
import com.qing.pojo.TreatmentRecord;


public class TreatmentRecordDAO extends DAO {
	public void addTreatmentRecord(TreatmentRecord record) throws TreatmentRecordException{
		try{
			begin();
			TreatmentRecord r = record;
			Session session = getSession();
			session.save(r);
			commit();
		}
		catch(HibernateException he){
			rollback();
			throw new TreatmentRecordException("Cannot add treatment record: "+he.getMessage());
		}
	}
	
	public List<TreatmentRecord> getTreatmentRecordList() throws TreatmentRecordException{
		try{
			begin();
			Query q = getSession().createQuery("from TreatmentRecord");
			//q.setString("title", '%'+title+'%');
			List<TreatmentRecord> result = q.list();
			commit();
			return result;
		}
		catch(HibernateException he)	{
			rollback();
			throw new TreatmentRecordException("Cannot list treatment records: " + he.getMessage());
		}
	}
	
}
