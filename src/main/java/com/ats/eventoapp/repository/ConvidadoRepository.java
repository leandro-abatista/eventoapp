package com.ats.eventoapp.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ats.eventoapp.models.Convidado;
import com.ats.eventoapp.models.Evento;

@Repository
public interface ConvidadoRepository extends CrudRepository<Convidado, Long> {

	Iterable<Convidado> findByEvento(Evento evento);
}
