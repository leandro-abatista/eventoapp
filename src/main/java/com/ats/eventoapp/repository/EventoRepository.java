package com.ats.eventoapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ats.eventoapp.models.Evento;
@Repository
public interface EventoRepository extends CrudRepository<Evento, Long> {

	Evento findById(Evento evento);
}
