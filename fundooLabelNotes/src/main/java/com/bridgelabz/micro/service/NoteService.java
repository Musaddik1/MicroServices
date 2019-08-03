package com.bridgelabz.micro.service;

import java.io.IOException;
import java.util.List;

import com.bridgelabz.micro.Dto.NoteDto;
import com.bridgelabz.micro.model.Label;
import com.bridgelabz.micro.model.Note;



public interface NoteService {

	String createNote(NoteDto noteDto, String token) throws IOException;

	String updateNote(NoteDto noteDto, String noteId, String token);

	String deleteNote(String token, String noteId);

	Note getNote(String noteId, String token);

	List<Note> getAllNote(String token);

	List<Note> getTrash(String token);

	List<Note> getArchive(String token);

	String archiveAndUnarchive(String token, String noteId);

	String trashAndUntrash(String token, String noteId);

	String pinAndUnpin(String token, String noteId);

	List<Note> sortByName(String token);

	List<Note> sortByDate(String token);

	List<Note> sortByType(String token);

	List<Note> sortById(String token);
	
	List<Note> search(String text);
	List<Note> getAllUserNote();
	List<Label> getLabelOfNotes(String noteId,String token);
	String setColor(String token,String noteId,String colorCode);
}
