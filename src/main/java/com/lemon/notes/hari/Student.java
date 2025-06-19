package com.lemon.notes.hari;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Data
public class Student {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
		private int id;
		private String name;
		private String classNumber;
		private String section;

}
