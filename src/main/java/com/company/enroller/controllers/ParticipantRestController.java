package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

    //Pobieranie listy wszystkich uczestników
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants(

            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) String key) {

        Collection<Participant> participants;

        //filtrowanie po loginie
        if (key != null) {
            participants = participantService.getParticipantsByKey(key);
        }

        //sortowanie ASC
        else if ("login".equals(sortBy)
                && "ASC".equalsIgnoreCase(sortOrder)) {
            participants = participantService.getAllASC();
        }

        //sortowanie DESC
        else if ("login".equals(sortBy)
                && "DESC".equalsIgnoreCase(sortOrder)) {
            participants = participantService.getAllDESC();
        }

        //zwykła lista
        else {

            participants = participantService.getAll();
        }

        return new ResponseEntity<Collection<Participant>>(
                participants,
                HttpStatus.OK);
    }
    //Pobieranie listy pojedyncznego uczestnika
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Participant>(participant, HttpStatus.OK);
    }
    //Dodawanie  uczestnika
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerParticipant(@RequestBody Participant participant){
        Participant foundParticipant = participantService.findByLogin(participant.getLogin());
        if (foundParticipant != null) {
            return new ResponseEntity("Already exists", HttpStatus.CONFLICT);
        }
        participantService.add(participant);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //Kasowanie uczestnika
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        participantService.delete(participant);
        return new ResponseEntity<Participant>(participant, HttpStatus.OK);
    }

    //Aktualizowanie uczestnika
    @RequestMapping(value = "/{login}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateParticipant(@PathVariable("login") String login, @RequestBody Participant participant) {
        Participant foundParticipant = participantService.findByLogin(login);
        if (foundParticipant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        participantService.update(participant);
        return new ResponseEntity<Participant>(foundParticipant, HttpStatus.OK);
        }

}
