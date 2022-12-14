package com.epam.esm.controller;

import com.epam.esm.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tags")
public class TagsController {
    TagService tagService;
    @GetMapping()
    public String getAllTags(ModelMap model){
        model.addAttribute("tags", tagService.read(null));
        return "tag";
    }
    @GetMapping("/{id}")
    public String getOneTag(@PathVariable("id")Long id, ModelMap model){
        model.addAttribute("message", "Spring MVC XML Config Example");
        return "tag";
    }
}
