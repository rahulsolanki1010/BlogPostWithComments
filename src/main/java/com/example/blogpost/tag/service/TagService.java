package com.example.blogpost.tag.service;

import com.example.blogpost.response.SuccessResponse;
import com.example.blogpost.tag.dto.TagDto;
import org.springframework.http.ResponseEntity;

public interface TagService {
    ResponseEntity<SuccessResponse> saveTag(TagDto tagDto);
    ResponseEntity findAllTag();
}
