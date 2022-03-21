package com.petlife.ospedalemicroservizio.controllers;

import com.petlife.ospedalemicroservizio.models.Visita;
import com.petlife.ospedalemicroservizio.repositories.OspedaleRepository;
import com.petlife.ospedalemicroservizio.repositories.VisitaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RabbitMqReceiver implements RabbitListenerConfigurer {

   @Autowired
   OspedaleRepository ospedaleRepository;

   @Autowired
   VisitaRepository visitaRepository;

   @Autowired
   OspedaleController ospedaleController;

   @Override
   public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {

   }

   @RabbitListener(queues = "${spring.rabbitmq.queue}")
   public void receivedMessage(Long idAnimaleEliminato) {
      log.info("Id animale eliminato: " + idAnimaleEliminato);
      //elimino tutte le visite dell'animale eliminato
      ospedaleController.loadOspedale();
      List<Visita> visiteDaEliminare = OspedaleController.ospedale.getVisite().stream().filter(visita -> visita.getIdAnimale().equals(idAnimaleEliminato)).collect(Collectors.toList());
      OspedaleController.ospedale.getVisite().removeAll(visiteDaEliminare);
      ospedaleRepository.save(OspedaleController.ospedale);
      visitaRepository.deleteAll(visiteDaEliminare);
      log.info("Visite eliminate dell'animale eliminato");
      log.info(visiteDaEliminare.toString());
   }

}
