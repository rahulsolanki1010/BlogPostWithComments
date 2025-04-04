package com.example.blogpost.tag.service;

import com.example.blogpost.response.SuccessResponse;
import com.example.blogpost.tag.dto.TagDto;
import com.example.blogpost.tag.model.Tag;
import com.example.blogpost.tag.repo.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService{
    @Autowired
    private TagRepo tagRepo;
    @Override
    public ResponseEntity<SuccessResponse> saveTag(TagDto tagDto){
        Tag tag = new Tag();
        tag.setTagName(tagDto.getTagName());
        tagRepo.save(tag);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.CREATED, "Tag added SuccessFully.");
        return  ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }
    @Override
    public ResponseEntity findAllTag(){
        List<Tag> tagList = tagRepo.findAll();
        if(!tagList.isEmpty()){
            List<TagDto> tagDtos = tagList.stream().map(tag->{
                TagDto tagDto = new TagDto();
                tagDto.setTagId(tag.getTagId());
                tagDto.setTagName(tag.getTagName());
                return tagDto;
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(HttpStatus.OK,tagDtos));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(HttpStatus.OK,"No Tags Found."));
        }
    }
}
