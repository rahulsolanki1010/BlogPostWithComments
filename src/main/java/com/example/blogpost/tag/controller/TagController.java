package com.example.blogpost.tag.controller;

import com.example.blogpost.response.SuccessResponse;
import com.example.blogpost.tag.dto.TagDto;
import com.example.blogpost.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @PostMapping
    ResponseEntity<SuccessResponse> saveTag(@RequestBody TagDto tagDto){
        return tagService.saveTag(tagDto);
    }
    @GetMapping
    ResponseEntity findAllTag(){
        return  tagService.findAllTag();
    }
}
