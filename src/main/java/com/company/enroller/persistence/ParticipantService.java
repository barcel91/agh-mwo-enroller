package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}


    //Poniżej wymagania na 3.0
    //Wyświetlanie wszystkich uczestników
	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

    //Wyszukiwanie uczestnika po loginie
    public Participant findByLogin(String login) {
        return (Participant) connector.getSession().get(Participant.class, login);
    }

    //Dodawanie uczestnika
    public void add(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(participant);
        transaction.commit();
    }

    //Kasowanie uczestnika
    public void delete(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(participant);
        transaction.commit();
    }
    //Aktualizowanie uczestnika
    public void update(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().merge(participant);
        transaction.commit();
    }

    //Poniżej wymagania na 4.0
    public Collection<Participant> getAllASC() {
        String hql = "FROM Participant ORDER BY login ASC";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }

    //Sortowanie wszystkich uczestników rosnąco (DESC)
    public Collection<Participant> getAllDESC() {
        String hql = "FROM Participant ORDER BY login DESC";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }

    //Wyświatlanie po kluczu
    public Collection<Participant> getParticipantsByKey(String key) {
        String hql ="FROM Participant WHERE login LIKE :key";
        Query query = connector.getSession().createQuery(hql);
        query.setParameter("key", "%" + key + "%");
        return query.list();
    }



}
