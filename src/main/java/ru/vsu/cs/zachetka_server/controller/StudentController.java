package ru.vsu.cs.zachetka_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.zachetka_server.model.dto.request.AddStudentRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.student.MainStudentInfoResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.student.StudentFirstResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.student.StudentInfoResponse;
import ru.vsu.cs.zachetka_server.service.StudentService;

import java.util.List;
import java.util.UUID;

@RestController
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

//    @GetMapping("/student/{user_uid}")
//    public MainStudentInfoResponse main(@PathVariable(value = "user_uid") UUID userUid) {
//        return this.studentService.getInfo(userUid);
//    }

    @PostMapping("/student")
    public void addStudent(@RequestBody AddStudentRequest addStudentRequest) {
        this.studentService.addStudent(addStudentRequest);
    }

    @GetMapping("/student/{user_uid}")
    public StudentFirstResponse getSemesters(@PathVariable(value = "user_uid") UUID uid) {
        return this.studentService.getSemesters(uid);
    }

    @GetMapping("/student/summary/{stud_uid}/{semester}")
    public List<StudentInfoResponse> getInfo(
            @PathVariable(value = "stud_uid") UUID uid,
            @PathVariable(value = "semester") Byte semester
    ) {
        return this.studentService.getInfo(uid, semester);
    }
}
