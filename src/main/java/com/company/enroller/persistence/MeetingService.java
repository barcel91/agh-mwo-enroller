package com.company.enroller.persistence;

import java.util.Collection;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

    //wyświetlanie wszystkich spotkań
	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

    //wyszukiwanie spotkania po meeting_id
    public Meeting findByMeetingId(long meeting_id) {
        return (Meeting) connector.getSession().get(Meeting.class, meeting_id);
    }

    //Dodawanie spotkań
    public void add(Meeting meeting) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(meeting);
        transaction.commit();
    }

    //kasowanie spotkania
    public void delete(Meeting meeting) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(meeting);
        transaction.commit();
    }

}
