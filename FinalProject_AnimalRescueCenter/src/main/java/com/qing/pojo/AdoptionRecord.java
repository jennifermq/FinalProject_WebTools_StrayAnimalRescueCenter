package com.qing.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="adoptionrecord_table")

public class AdoptionRecord {
	public AdoptionRecord() {

	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="recordid", unique= true, nullable=false)
	private int id;
	
	@Transient
	private Animal animal;
	
	@Column(name="animalid")
	private long animalId;
	
	@Transient
	private Adopter adopter;
	
	@Column(name="adopteremail")
	private String adopterEmail;
	
	@Column(name="date")
	private String date;
	
	@Column(name="status")
	private String status;
	
	public Animal getAnimal() {
		return animal;
	}
	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	public Adopter getAdopter() {
		return adopter;
	}
	public void setAdopter(Adopter adopter) {
		this.adopter = adopter;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public long getAnimalId() {
		return animalId;
	}
	public void setAnimalId(long animalId) {
		this.animalId = animalId;
	}
	public String getAdopterEmail() {
		return adopterEmail;
	}
	public void setAdopterEmail(String adopterEmail) {
		this.adopterEmail = adopterEmail;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public void setDate(int month, int date, int year) {
		this.date = month + "/" + date + "/" + year;
	}
	public String getDate() {
		return date;
	}
	
}
