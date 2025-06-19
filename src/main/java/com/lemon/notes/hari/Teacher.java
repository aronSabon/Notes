package com.lemon.notes.hari;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
@Data
public class Teacher {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
		private int id;
		private String name;
		@ManyToMany
		private List<Subject> subjects;
		@CollectionTable
		@ElementCollection
		private List<String> classNumbers;
		@CollectionTable
		@ElementCollection
		private List<String> sections;
		}
