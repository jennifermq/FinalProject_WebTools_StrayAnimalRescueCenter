package com.qing.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

@Entity
@Table(name="treatmentrecord_table")

public class TreatmentRecord {
	public TreatmentRecord() {
		
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="treatmentid", unique= true, nullable=false)
	private long id;
	
	@Transient
	private Animal animal;

	@Column(name="animalid")
	private long animalid;
	
	@Transient
	private Employee veterinarian;

	@Column(name="veterinarianid")
	private long veterinarianid;
	
	@Column(name="date")
	private String date;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Animal getAnimal() {
		return animal;
	}
	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	
	public Employee getVeterinarian() {
		return veterinarian;
	}
	
	public void setVeterinarian(Employee veterinarian) {
		this.veterinarian = veterinarian;
	}
	
	public long getAnimalid() {
		return animalid;
	}
	public void setAnimalid(long animalid) {
		this.animalid = animalid;
	}
	public long getVeterinarianid() {
		return veterinarianid;
	}
	public void setVeterinarianid(long veterinarianid) {
		this.veterinarianid = veterinarianid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(int month, int date, int year) {
		this.date = month + "/" + date + "/" + year;
	}
}
