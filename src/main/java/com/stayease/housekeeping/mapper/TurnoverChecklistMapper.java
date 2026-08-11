package com.stayease.housekeeping.mapper;

import com.stayease.housekeeping.dto.TurnoverChecklistRequest;
import com.stayease.housekeeping.dto.TurnoverChecklistResponse;
import com.stayease.housekeeping.entity.TurnoverChecklist;
import com.stayease.housekeeping.enums.ChecklistStatus;

public final class TurnoverChecklistMapper {

    private TurnoverChecklistMapper() {
    }

    public static TurnoverChecklist toEntity(TurnoverChecklistRequest request) {
        TurnoverChecklist c = new TurnoverChecklist();
        c.setTurnoverId(request.turnoverId());
        apply(c, request);
        return c;
    }

    public static void updateEntity(TurnoverChecklist c, TurnoverChecklistRequest request) {
        c.setTurnoverId(request.turnoverId());
        apply(c, request);
    }

    private static void apply(TurnoverChecklist c, TurnoverChecklistRequest request) {
        c.setTaskName(request.taskName());
        c.setCategory(request.category());
        c.setCompleted(Boolean.TRUE.equals(request.completed()));
        c.setNotes(request.notes());
        c.setStatus(request.status() != null ? request.status() : ChecklistStatus.PENDING);
    }

    public static TurnoverChecklistResponse toResponse(TurnoverChecklist c) {
        return new TurnoverChecklistResponse(
                c.getId(),
                c.getTurnoverId(),
                c.getTaskName(),
                c.getCategory(),
                c.isCompleted(),
                c.getNotes(),
                c.getStatus());
    }
}


//package com.stayease.housekeeping.mapper;
//
//import com.stayease.housekeeping.dto.TurnoverChecklistRequest;
//import com.stayease.housekeeping.dto.TurnoverChecklistResponse;
//import com.stayease.housekeeping.entity.TurnoverChecklist;
//import com.stayease.housekeeping.enums.ChecklistStatus;
//import org.mapstruct.AfterMapping;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.MappingTarget;
//
//@Mapper(componentModel = "spring")
//public interface TurnoverChecklistMapper {
//
//    @Mapping(target = "completed", expression = "java(Boolean.TRUE.equals(request.completed()))")
//    TurnoverChecklist toEntity(TurnoverChecklistRequest request);
//
//    @Mapping(target = "completed", expression = "java(Boolean.TRUE.equals(request.completed()))")
//    void updateEntity(@MappingTarget TurnoverChecklist c, TurnoverChecklistRequest request);
//
//    TurnoverChecklistResponse toResponse(TurnoverChecklist c);
//
//    @AfterMapping
//    default void applyDefaultStatus(TurnoverChecklistRequest request, @MappingTarget TurnoverChecklist c) {
//        if (request.status() == null) {
//            c.setStatus(ChecklistStatus.PENDING);
//        }
//    }
//}