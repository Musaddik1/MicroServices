package com.bridgelabz.micro.repository;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.support.PageableExecutionUtils;

import com.bridgelabz.micro.model.Note;

import java.lang.String;
import java.util.List;
import java.util.Optional;

public interface NoteRepository extends MongoRepository<Note, String> {

	Optional<Note> findByNoteId(String noteId);


	List<Note> findByUserId(String token);

	List<Note> findByUserIdAndIsArchive(String userId, boolean isArchive);

	Optional<Note> findByNoteIdAndUserId(String noteId, String userId);
	List<Note> findByUserIdAndIsTrash(String userId,boolean boolean1);
	List<Note> findByOrderByTitleAsc();
	List<Note> findByOrderByTitleDesc();
	

	

}
