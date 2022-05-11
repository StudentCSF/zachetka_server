package ru.vsu.cs.zachetka_server.controller;

import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.zachetka_server.model.dto.request.UpdateGroupMarksRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.lecturer.*;
import ru.vsu.cs.zachetka_server.service.LecturerService;

import java.util.List;
import java.util.UUID;

@RestController
public class LecturerController {

    private final LecturerService lecturerService;

    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @PostMapping("/lecturer/{user_uid}")
    public void addLecturer(@RequestBody String fio,
                            @PathVariable(value = "user_uid") UUID userUid
    ) {
        this.lecturerService.addLecturer(fio, userUid);
    }

//    @GetMapping("/lecturer/{user_lect_uid}")
//    public MainLecturerInfoResponse getMainPage(
//            @PathVariable("user_lect_uid") UUID uid
//    ) {
//        return this.lecturerService.getData(uid);
//    }

    @GetMapping("/lecturer/{user_lect_uid}")
    public LecturerFirstResponse getMainPage(
            @PathVariable("user_lect_uid") UUID uid
    ) {
        return this.lecturerService.getPeriods(uid);
    }

    @PutMapping("/lecturer/update/{sl_uid}")
    public List<LecturerInfoResponse> updateMarks(
        @PathVariable("sl_uid") UUID uid,
        @RequestBody List<UpdateGroupMarksRequest> updateGroupMarksRequests
    ) {
        return this.lecturerService.updateGroupData(uid, updateGroupMarksRequests);
    }

    @GetMapping("/lecturer/subjects/{lect_uid}/{period}")
    public List<LecturerKeySubjectResponse> getSubjects(
            @PathVariable(value = "lect_uid") UUID uid,
            @PathVariable(value = "period") String period
    ) {
        return this.lecturerService.getSubjects(uid, period);
    }
}
