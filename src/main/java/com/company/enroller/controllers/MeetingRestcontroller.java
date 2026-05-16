package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meeting")


public class MeetingRestcontroller {

    @Autowired
    MeetingService meetingService;

    //Pobieranie listy wszystkich Spotkań
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> Meetings(

            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) String key) {

        Collection<Meeting> meetings;

        //filtrowanie
        if (key != null) {
            meetings = meetingService.getMeetingsByKey(key);
        }

        //sortowanie ASC
        else if ("title".equals(sortBy)
                && "ASC".equalsIgnoreCase(sortOrder)) {
            meetings = meetingService.getAllASC();
        }

        //sortowanie DESC
        else if ("title".equals(sortBy)
                && "DESC".equalsIgnoreCase(sortOrder)) {
            meetings = meetingService.getAllDESC();
        }

        //zwykła lista
        else {
            meetings = meetingService.getAll();
        }

        return new ResponseEntity<Collection<Meeting>>(
                meetings,
                HttpStatus.OK);
    }

    // Pobieranie listy pojedyncznego spotkania
    @RequestMapping(value = "/{meeting_id}", method = RequestMethod.GET)
    public ResponseEntity<?> findByMeetingId(@PathVariable("meeting_id") long meeting_id) {
        Meeting meeting = meetingService.findByMeetingId(meeting_id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    //Dodawanie spotkań
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findByMeetingId(meeting.getId());
        if (foundMeeting != null) {
            return new ResponseEntity("Already exists", HttpStatus.CONFLICT);
        }
        meetingService.add(meeting);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //Usuwanie spotkań
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long meeting_id) {
        Meeting meeting = meetingService.findByMeetingId(meeting_id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.delete(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    //Aktualizowanie spotkań
        @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findByMeetingId(id);
        if (foundMeeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.update(meeting);
        return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
    }

}
