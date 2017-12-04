package com.qing.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.qing.exception.AnimalException;
import com.qing.pojo.Animal;

public class AnimalDAO extends DAO {
	public void addAnimal(Animal animal) throws AnimalException{
		try{
			begin();
			Animal a = animal;
			Session session = getSession();
			session.save(a);
			commit();
			close();
		}
		catch(HibernateException he){
			rollback();
			throw new AnimalException("Error:"+he.getMessage());
		}
	}
	
	public List<Animal> getAnimalList() throws AnimalException{
		try{
			begin();
			Query q = getSession().createQuery("from Animal");
			//q.setString("title", '%'+title+'%');
			List<Animal> result = q.list();
			commit();
			close();
			System.out.println("size: " + result.size());
			return result;
		}
		catch(HibernateException he)	{
			rollback();
			throw new AnimalException("Cannot list animals: " + he.getMessage());
		}
	}
	
	public Animal getAnimalById(long id) throws AnimalException{
		try{
			begin();
			Query q = getSession().createQuery("from Animal where animalid = :id");
			q.setLong("id", id);
			Animal a = (Animal) q.uniqueResult();
			commit();
			close();
			return a;
			
		}
		catch(HibernateException he){
			rollback();
			throw new AnimalException("getAnimalById: "+he.getMessage());
		}
	}
	
//	public List<Animal> getFullAnimalAdoptionList() throws AnimalException{
//		try{
//			begin();
//			Query q = getSession().createQuery("from animal_table where status != :status");
//			q.setString("status", "untreated");
//			List<Animal> result = q.list();
//			commit();
//			return result;
//		}
//		catch(HibernateException he)	{
//			rollback();
//			throw new AnimalException("Cannot list animals: " + he.getMessage());
//		}
//	}
	
	public List<Animal> getAdopterAnimalAdoptionList() throws AnimalException{
		try{
			begin();
			Query q = getSession().createQuery("from Animal where status = :status");
			q.setString("status", "treated");
			List<Animal> result = q.list();
			commit();
			close();
			return result;
		}
		catch(HibernateException he)	{
			rollback();
			throw new AnimalException("Cannot list animals: " + he.getMessage());
		}
	}
	
	public void updateAnimal(long animalId, String status) throws AnimalException {
		try{
			begin();
			Session session = getSession();
			
			Query q = getSession().createQuery("update Animal a set a.status = :status where a.animalId = :animalId");
			q.setLong("animalId", animalId);
			q.setString("status", status);
			q.executeUpdate();
			commit();
			close();
		}
		catch(HibernateException he)	{
			rollback();
			throw new AnimalException("Cannot update animal: " + he.getMessage());
		}
	}
	
	public float getTotalCount() throws AnimalException{
		begin();
		Session session = getSession();
		Query q = getSession().createQuery("from Animal");
		List<Animal> result = q.list();
		commit();
		close();
		return result.size();
	}
	
	public float getDogPercentage() throws AnimalException{
		begin();
		Session session = getSession();
		Query q = getSession().createQuery("from Animal where type=:type");
		q.setString("type","dog");
		List<Animal> result = q.list();
		commit();
		close();
		float dogCount= result.size();
		float total = this.getTotalCount();
		if(total == 0) {
			return 0;
		}
		else {
			return dogCount/total;
		}
	}
	
	public float getCatPercentage() throws AnimalException{
		begin();
		Session session = getSession();
		Query q = getSession().createQuery("from Animal where type=:type");
		q.setString("type","cat");
		List<Animal> result = q.list();
		commit();
		close();
		float catCount= result.size();
		float total = this.getTotalCount();
		if(total == 0) {
			return 0;
		}
		else {
			return catCount/total;
		}
	}
	
	public float getOtherPercentage() throws AnimalException{
		begin();
		Session session = getSession();
		Query q = getSession().createQuery("from Animal where type=:type");
		q.setString("type","cat");
		List<Animal> result1 = q.list();
		commit();
		close();
		
		
		begin();
		session = getSession();
		q = getSession().createQuery("from Animal where type=:type");
		q.setString("type","dog");
		List<Animal> result2 = q.list();
		commit();
		close();
		
		float catCount= result1.size();
		float dogCount= result2.size();
		float total = this.getTotalCount();
		return (total - catCount - dogCount) / total;
	}
	
	public float getMalePercentage() throws AnimalException{
		begin();
		Session session = getSession();
		Criteria crit = session.createCriteria(Animal.class);
		crit.add(Restrictions.ilike("gender", "m", MatchMode.START));
		List<Animal> result = crit.list();
		commit();
		close();
		return result.size() / this.getTotalCount();
	}
	
	public float getFemalePercentage() throws AnimalException{
		begin();
		Session session = getSession();
		Criteria crit = session.createCriteria(Animal.class);
		crit.add(Restrictions.ilike("gender", "f", MatchMode.START));
		List<Animal> result = crit.list();
		commit();
		close();
		return result.size() / this.getTotalCount();
	}
}
