package ru.vsu.cs.zachetka_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.zachetka_server.model.dto.request.StudentRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.StudentInfoResponse;
import ru.vsu.cs.zachetka_server.service.StudentService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/student/{user_uid}")
    public void addStudent(@RequestBody StudentRequest studentRequest,
                           @PathVariable(value = "user_uid") UUID userUid
    ) {
        this.studentService.addStudent(studentRequest, userUid);
    }

    @GetMapping("/student/{user_uid}")
    public Map<Byte, List<StudentInfoResponse>> main(@PathVariable(value = "user_uid") UUID userUid) {
        return this.studentService.getInfo(userUid);
    }
}
