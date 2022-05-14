package ru.vsu.cs.zachetka_server.controller;

import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.zachetka_server.model.dto.request.AddMarkRawsRequest;
import ru.vsu.cs.zachetka_server.model.dto.request.MarkRequest;
import ru.vsu.cs.zachetka_server.model.dto.request.UpdateGroupMarksRequest;
import ru.vsu.cs.zachetka_server.model.dto.response.SubjLectsAndGroupsResponse;
import ru.vsu.cs.zachetka_server.model.dto.response.lecturer.LecturerInfoResponse;
import ru.vsu.cs.zachetka_server.service.MarkService;

import java.util.List;
import java.util.UUID;

@RestController
public class MarkController {

    private final MarkService markService;

    public MarkController(MarkService markService) {
        this.markService = markService;
    }

    @PostMapping("/mark")
    public void addMark(@RequestBody AddMarkRawsRequest markRequest) {
        this.markService.addMark(markRequest);
    }

    @PutMapping("/mark/update/{sl_uid}")
    public List<LecturerInfoResponse> updateMarks(
            @PathVariable("sl_uid") UUID uid,
            @RequestBody List<UpdateGroupMarksRequest> updateGroupMarksRequests
    ) {
        return this.markService.updateGroupData(uid, updateGroupMarksRequests);
    }

    @GetMapping("/mark/{period}/{semester}")
    public SubjLectsAndGroupsResponse getSubjLectsAndGroups(
            @PathVariable("period") String period,
            @PathVariable(value = "semester") Byte semester
    ) {
        return this.markService.getLists(period, semester);
    }
}
