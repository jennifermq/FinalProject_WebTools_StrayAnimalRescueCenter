package com.qing.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.qing.exception.AdoptionRecordException;
import com.qing.exception.AnimalException;
import com.qing.pojo.Adopter;
import com.qing.pojo.AdoptionRecord;

public class AdoptionRecordDAO extends DAO {
	public void addAdoptionRecord(AdoptionRecord record) throws AdoptionRecordException{
		try{
			begin();
			AdoptionRecord r = record;
			Session session = getSession();
			session.save(r);
			commit();
			close();
		}
		catch(HibernateException he){
			rollback();
			throw new AdoptionRecordException("Cannot add adoption record: "+he.getMessage());
		}
	}
	
	public List<AdoptionRecord> getAdoptionRecordList() throws AdoptionRecordException{
		try{
			begin();
			Query q = getSession().createQuery("from AdoptionRecord");
			//q.setString("title", '%'+title+'%');
			List<AdoptionRecord> result = q.list();
			commit();
			close();
			return result;
		}
		catch(HibernateException he)	{
			rollback();
			throw new AdoptionRecordException("Cannot list adoption records: " + he.getMessage());
		}
	}
	
	public AdoptionRecord getAdoptionRecordByID(long id) throws AdoptionRecordException {
		try{
			begin();
			Query q = getSession().createQuery("from AdoptionRecord where recordid = :id");
			q.setLong("id", id);
			AdoptionRecord result = (AdoptionRecord) q.uniqueResult();
			commit();
			close();
			return result;
		}
		catch(HibernateException he)	{
			rollback();
			throw new AdoptionRecordException("Cannot list adoption records by id: " + he.getMessage());
		}
	}
	
	public List<AdoptionRecord> getAdoptionRecordListByAdopter(Adopter adopter) throws AdoptionRecordException {
		String email = adopter.getEmail();
		try{
			begin();
			Query q = getSession().createQuery("from AdoptionRecord where adopteremail = :email");
			q.setString("email", email);
			List<AdoptionRecord> result = q.list();
			commit();
			close();
			return result;
		}
		catch(HibernateException he)	{
			rollback();
			throw new AdoptionRecordException("Cannot list adoption records by adopter: " + he.getMessage());
		}
	}
	
	public List<AdoptionRecord> getAdoptionRecordListPending() throws AdoptionRecordException {
		try{
			begin();
			Query q = getSession().createQuery("from AdoptionRecord where status = :status");
			q.setString("status", "pending");
			List<AdoptionRecord> result = q.list();
			commit();
			close();
			return result;
		}
		catch(HibernateException he)	{
			rollback();
			throw new AdoptionRecordException("Cannot list adoption records by adopter: " + he.getMessage());
		}
	}
	
	public void updateAdoptionRecord(long recordId, String status) throws AnimalException {
		try{
			begin();
			Session session = getSession();
			Query q = getSession().createQuery("update AdoptionRecord ar set ar.status = :status where ar.id = :recordId");
			q.setLong("recordId", recordId);
			q.setString("status", status);
			q.executeUpdate();
//			Query q = getSession().createQuery("from animal_table where animalId = :animalId");
//			q.setInteger("animalId", animalId);
//			Animal animal = (Animal) q.uniqueResult();
//			getSession().delete(animal);
//			animal.setStatus(status);
//			session.save(animal);
			commit();
			close();
		}
		catch(HibernateException he)	{
			rollback();
			throw new AnimalException("Cannot update rescue record: " + he.getMessage());
		}
	}

	
}
