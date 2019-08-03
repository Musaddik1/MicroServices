package com.bridgelabz.micro.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.micro.model.Label;

import java.lang.String;
import java.util.List;
import java.util.Optional;

public interface LabelRespository extends MongoRepository<Label, String> {

	List<Label> findByUserId(String userid);

	Optional<Label> findByLabelIdAndUserId(String labelId, String userId);
	Optional<Label> findByLabelName(String labelname);
}
