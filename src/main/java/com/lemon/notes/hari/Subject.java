package com.lemon.notes.hari;

import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
@Data
public class Subject {
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Id
	private int id;
	private String name;
	private String classNumber;
    @ManyToMany(mappedBy = "subjects")
    private List<Teacher> teachers;
	
}
