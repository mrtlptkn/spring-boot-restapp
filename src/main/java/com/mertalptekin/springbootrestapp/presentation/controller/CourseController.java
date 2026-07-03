package com.mertalptekin.springbootrestapp.presentation.controller;

import com.mertalptekin.springbootrestapp.secondarydb.entity.Course;
import com.mertalptekin.springbootrestapp.secondarydb.repository.ICourseRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final ICourseRepository courseRepository;

    public CourseController(ICourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @PostMapping
    public String createCourse() {

        Course.CourseBuilder courseBuilder = Course.builder();
        Course course = courseBuilder.name("Java Spring Boot").build();

        this.courseRepository.save(course);

        return "Course created";
    }


}
