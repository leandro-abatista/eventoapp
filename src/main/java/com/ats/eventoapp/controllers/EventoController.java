package com.ats.eventoapp.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.hibernate.boot.jaxb.spi.Binding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ats.eventoapp.models.Convidado;
import com.ats.eventoapp.models.Evento;
import com.ats.eventoapp.repository.ConvidadoRepository;
import com.ats.eventoapp.repository.EventoRepository;



@Controller
public class EventoController {
	
	@Autowired
	private EventoRepository eventoRepository;
	
	@Autowired
	private ConvidadoRepository convidadoRepository;

	@RequestMapping(value = "/cadastrarEvento", method = RequestMethod.GET)//retorna os dados do formulário
	public String form() {
		return "evento/formEvento";
	}
	
	@RequestMapping(value = "/cadastrarEvento", method = RequestMethod.POST)//retorna os dados do formulário
	public String form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
		
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/cadastrarEvento";
		}
		
		eventoRepository.save(evento);
		return "redirect:/cadastrarEvento";
	}
	
	@RequestMapping(value = "/eventos", method = RequestMethod.GET)//retorna uma lista de eventos
	public ModelAndView listaEventos() {
		
		ModelAndView view = new ModelAndView("index");
		
		Iterable<Evento> eventos = eventoRepository.findAll();//busca uma lista de eventos cadastrados no banco
		view.addObject("eventos", eventos);
		
		return view;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView detalhesEvento(@PathVariable("id") Long id, 
											RedirectAttributes attributes) {
		
		Evento evento = eventoRepository.findById(id).get();

		ModelAndView view = new ModelAndView("evento/detalhesEvento");
		view.addObject("evento", evento);

		Iterable<Convidado> convidados = convidadoRepository.findByEvento(evento);
		view.addObject("convidados", convidados);

		return view;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String detalhesEventoForm(@PathVariable("id") Long id, 
									 @Valid	Convidado convidado,
									 BindingResult result,
									 RedirectAttributes attributes) {
		
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/{id}";
		}
		Evento evento = eventoRepository.findById(id).get();
		convidado.setEvento(evento);
		convidadoRepository.save(convidado);
		attributes.addFlashAttribute("mensagem", "Registro adicionado com sucesso!");
		return "redirect:/{id}";
	}
	
	@RequestMapping("/deletarEvento")
	public String deletarEvento(Long id) {
		Evento evento = eventoRepository.findById(id).get();
		eventoRepository.delete(evento);
		return "redirect:/eventos";
	}
	
	@RequestMapping("/deletarConvidado")
	public String deletarConvidado(Long id) {
		Convidado convidado = convidadoRepository.findById(id).get();
		convidadoRepository.delete(convidado);
		
		Evento evento = convidado.getEvento();
		Long codigoLong = evento.getId();
		String idE = "" + codigoLong;
		return "redirect:/" + idE;
	}
}
