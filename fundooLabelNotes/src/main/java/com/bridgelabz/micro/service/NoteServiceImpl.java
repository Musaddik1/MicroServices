package com.bridgelabz.micro.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.micro.Dto.NoteDto;
import com.bridgelabz.micro.exception.NoteException;
import com.bridgelabz.micro.exception.UserException;
import com.bridgelabz.micro.model.Label;
import com.bridgelabz.micro.model.Note;
import com.bridgelabz.micro.repository.NoteRepository;
import com.bridgelabz.micro.response.Response;
import com.bridgelabz.micro.utility.JWTTokenGenerator;


@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	private JWTTokenGenerator tokenGenerator;
	/*
	 * @Autowired private UserRepository userRepository;
	 */
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private ElasticSearch elasticSearch;

	@Override
	public String createNote(NoteDto noteDto, String token) {
		String userId = tokenGenerator.verifyToken(token);
	//	Optional<User> optUser = userRepository.findByUserId(userId);
		//if (optUser.isPresent()) {
			Note note = modelMapper.map(noteDto, Note.class);
			note.setCreationtTime(LocalDateTime.now());
			note.setUpdateTime(LocalDateTime.now());
			note.setUserId(userId);
			elasticSearch.createNote(note);
			noteRepository.save(note);
		
			// return new Response(200, "note created ", null);
			return "note created";

		/*
		 * } else { throw new NoteException("note not created"); }
		 */
	}

	@Override
	public String updateNote(NoteDto noteDto, String noteId, String token) {
		String userId = tokenGenerator.verifyToken(token);
		Optional<Note> optNote = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (optNote.isPresent()) {
			Note note = optNote.get();

			note.setUpdateTime(LocalDateTime.now());

			note.setTitle(noteDto.getTitle());
			note.setDescription(noteDto.getDescription());
			elasticSearch.createNote(note);
			noteRepository.save(note);

			return "note updated";

		} else {
			// return new Response(202, "note doesnt exist", null);
			throw new NoteException("note doesnt exist");
		}

	}

	@Override
	public String deleteNote(String token, String noteId) {
		String userId = tokenGenerator.verifyToken(token);
		/*Optional<User> optUser = userRepository.findByUserId(userId);
		return optUser.filter(user -> user != null).map(user -> {
			Optional<Note> optionalNote = noteRepository.findById(noteId);
			optionalNote.filter(note -> {
				return note.isTrash();
			}).map(note -> {
				
				elasticSearch.deleteNote(noteId);
				noteRepository.delete(note);
				// return new Response(200, "deleted note", null);
				return "deleted note";
			}).orElseThrow(() -> new UserException("note not found"));
			// return new Response(200, "deleted note", null);
			return "deleted note";
		}).orElseThrow(() -> new UserException("note not found"));
	}*/
		Optional<Note> optNote=noteRepository.findById(noteId);
		if(optNote.isPresent())
		{
			Note note=optNote.get();
			if(note.isTrash())
			{
				noteRepository.delete(note);
				return "note deleted successfully..";
			}
			else
			{
				return "note not present in trash";
			}
		}
		else
		{
			throw new NoteException("note not present");
		}
		/*
		 * //if (optUser.isPresent()) { Optional<Note> optNote =
		 * noteRepository.findById(noteId); if (optNote.isPresent()) { Note note =
		 * optNote.get(); if(note.isTrash()) noteRepository.delete(note); else return
		 * new Response(202, "unsuccess", null); return new Response(200,
		 * "deleted note", null); } else { return new Response(202,
		 * "note doesn't exist", null);
		 * 
		 * } } else { return new Response(202, "unsuccess", null); }
		 */
	}

	@Override
	public Note getNote(String noteId, String token) {

		String userId = tokenGenerator.verifyToken(token);
		Optional<Note> optNote = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (optNote.isPresent()) {
			Note note = optNote.get();
			return note;
		} else {
			throw new NoteException("noteId or user not match");
		}

	}

	@Override
	public List<Note> getAllNote(String token) {

		/*
		 * String userId = tokenGenerator.verifyToken(token); List<Note> notes =
		 * noteRepository.findAll(); List<Note> filteredNotes =
		 * notes.stream().filter(note -> { return note.getUserId().equals(userId);
		 * }).collect(Collectors.toList());
		 */
		/*
		 * List<NoteDto> noteslist = new ArrayList<NoteDto>(); for (Note userNotes :
		 * note) { NoteDto noteDto = modelMapper.map(userNotes, NoteDto.class);
		 * noteslist.add(noteDto);
		 * 
		 * } return noteslist;
		 */
		 String userId=tokenGenerator.verifyToken(token);
		// Optional<User> optUser=userRepository.findById(userId);
		 List<Note> notesList=new ArrayList<Note>();
		// if(optUser.isPresent())
		 {
			 List<Note> tempList=noteRepository.findByUserId(userId);
			 for(Note note:tempList)
			 {
				 if(!note.isArchive()&&!note.isTrash())
				 {
					 notesList.add(note);
				 }
			 }
		 }
		return notesList;
	}

	@Override
	public List<Note> getTrash(String token) {
		String userId = tokenGenerator.verifyToken(token);
		List<Note> notes = noteRepository.findByUserId(userId);
		List<Note> noteList = noteRepository.findByUserIdAndIsTrash(userId, true);
		//List<Note> noteslist = notes.stream().filter(data -> data.isTrash()).collect(Collectors.toList());
		/*
		 * List<NoteDto> noteslist = new ArrayList<NoteDto>(); for (Note note : notes) {
		 * NoteDto noteDto = modelMapper.map(note, NoteDto.class);
		 * 
		 * if (note.isTrash() == true) { noteslist.add(noteDto); } } return noteslist;
		 */
		return noteList;
	}

	@Override
	public List<Note> getArchive(String token) {
		String userId = tokenGenerator.verifyToken(token);
		return noteRepository.findByUserIdAndIsArchive(userId, true);
	}

	@Override
	public String archiveAndUnarchive(String token, String noteId) {

		String userId = tokenGenerator.verifyToken(token);
		Optional<Note> optNote = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (optNote.isPresent()) {
			Note note = optNote.get();
			if (note.isArchive()) {
				note.setArchive(false);
				noteRepository.save(note);
				return "note is unarchived";
			} else {

				note.setArchive(true);
				noteRepository.save(note);
				return "note is archived";
			}
		} else {
			throw new NoteException("Note or User not present");
		}

	}

	@Override
	public String trashAndUntrash(String token, String noteId) {
		String userId = tokenGenerator.verifyToken(token);
		Optional<Note> optNote = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (optNote.isPresent()) {
			Note note = optNote.get();
			if (note.isTrash()) {
				note.setTrash(false);
				noteRepository.save(note);
				return "note is untrash";
			} else {
				note.setTrash(true);
				noteRepository.save(note);
				return "note in trash";
			}
		} else {
			throw new NoteException("note or user not present");
		}

	}

	@Override
	public String pinAndUnpin(String token, String noteId) {
		String userId = tokenGenerator.verifyToken(token);
		Optional<Note> optNote = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (optNote.isPresent()) {
			Note note = optNote.get();
			if (note.isPin()) {
				note.setPin(false);
				noteRepository.save(note);
				return "note is unpin";
			} else {
				note.setPin(true);
				noteRepository.save(note);
				return "note is pin";

			}
		} else {
			throw new NoteException("note or User dont exist");
		}

	}

	@Override
	public List<Note> sortByName(String token) {
		String userId = tokenGenerator.verifyToken(token);
		//Optional<User> optUser = userRepository.findById(userId);
		//if (optUser.isPresent()) {
			List<Note> noteList = noteRepository.findAll();
			noteList.sort(Comparator.comparing(Note::getTitle).reversed());
			return noteList;
		/*
		 * } else { throw new UserException("User not present"); }
		 */
	}

	@Override
	public List<Note> sortByDate(String token) {

		String userId = tokenGenerator.verifyToken(token);
		//Optional<User> optUser = userRepository.findById(userId);
		//if (optUser.isPresent()) {
			List<Note> noteList = noteRepository.findAll();
			noteList.sort(Comparator.comparing(Note::getCreationtTime).reversed());
			return noteList;
		/*
		 * } else { throw new UserException("User not present "); }
		 */
	}

	@Override
	public List<Note> sortByType(String token) {
		return null;
	}

	@Override
	public List<Note> sortById(String token) {
		String userId = tokenGenerator.verifyToken(token);
		//Optional<User> optionaUser = userRepository.findById(userId);
		//if (optionaUser.isPresent()) {
			List<Note> noteList = noteRepository.findAll();
			noteList.sort(Comparator.comparing(Note::getNoteId));
			return noteList;

		/*
		 * } else { throw new UserException("User not found"); }
		 */
	}

	@Override
	public List<Note> search(String text) {
	
		List<Note> noteList = elasticSearch.searchByText(text);
		return noteList;
	}

	
	@Override
	public List<Note> getAllUserNote() {
		/*
		 * List<Note> sortedNote = new ArrayList<Note>(); List<User>
		 * userlist=userRepository.findAll();
		 * 
		 * 
		 * 
		 * for (User user : userlist) {
		 * 
		 * List<Note> notesList=noteRepository.findByUserId(user.getUserId());
		 * 
		 * for(Note note:notesList) { sortedNote.add(note); }
		 * 
		 * 
		 * 
		 * } sortedNote.sort(Comparator.comparing(Note::getTitle).reversed()); return
		 * sortedNote;
		 */
		List<Note> notesList=noteRepository.findByOrderByTitleDesc();
		return notesList;
}

	@Override
	public List<Label> getLabelOfNotes(String noteId, String token) {
		
		String userId=tokenGenerator.verifyToken(token);
	//	Optional<User> optUser=userRepository.findById(userId);
	//	if(optUser.isPresent())
		{
			Optional<Note> optNote=noteRepository.findById(noteId);
			if(optNote.isPresent())
			{
				Note note=optNote.get();
				List<Label> labelList=note.getLabels();
				return labelList;
			}
		}
		return null;
	}

	@Override
	public String setColor(String token, String noteId,String colorcode) {
	
		String userId=tokenGenerator.verifyToken(token);
		//Optional<Note> optNote=noteRepository.findByNoteIdAndUserId(noteId, userId);
		//Optional<User> optUser=userRepository.findById(userId);
		//if(optUser.isPresent())
		//{
			Optional<Note> optNote=noteRepository.findById(noteId);
		if(optNote.isPresent())
		{
			Note note=optNote.get();
			note.setNoteColor(colorcode);
			elasticSearch.createNote(note);
			noteRepository.save(note);
			return "color set on note";
		}
		else
		{
			throw new NoteException("note not present");
		}
		/*
		 * }else { throw new UserException("user not present"); }
		 */
	}
}
