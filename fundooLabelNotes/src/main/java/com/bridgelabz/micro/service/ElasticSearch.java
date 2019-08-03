package com.bridgelabz.micro.service;


import java.util.List;

import com.bridgelabz.micro.model.Note;



public interface ElasticSearch {

	String createNote(Note note) ;
	String updateNote(String noteId);
	String deleteNote(String noteId);
	List<Note> searchByText(String title);
	
}
