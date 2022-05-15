package ru.vsu.cs.zachetka_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.zachetka_server.model.dto.request.AddStudentRequest;
import ru.vsu.cs.zachetka_server.model.dto.request.StudentRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.student.MainStudentInfoResponse;
import ru.vsu.cs.zachetka_server.service.StudentService;

import java.util.UUID;

@RestController
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/student/{user_uid}")
    public MainStudentInfoResponse main(@PathVariable(value = "user_uid") UUID userUid) {
        return this.studentService.getInfo(userUid);
    }

    @PostMapping("/student")
    public void addStudent(@RequestBody AddStudentRequest addStudentRequest) {
        this.studentService.addStudent(addStudentRequest);
    }
}
