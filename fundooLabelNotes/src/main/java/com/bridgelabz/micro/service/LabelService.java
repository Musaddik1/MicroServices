package com.bridgelabz.micro.service;

import java.util.List;

import com.bridgelabz.micro.Dto.LabelDto;
import com.bridgelabz.micro.model.Label;



public interface LabelService {

	String createLabel(String token, LabelDto labelDto);

	String updateLabel(String token, String labeId, LabelDto labelDto);

	String deleteLabel(String token, String labelId);

	String addLabelToNote(String token, String labelId, String noteId);

	String removeLabelFromNote(String token, String labelId, String noteId);

	Label getLabel(String token, String labelId);

	List<Label> getAllLabel(String token);
}
