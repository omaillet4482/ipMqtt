 package fr.olivier.projet;


import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import fr.olivier.projet.bo.NmapBo;
import fr.olivier.projet.mqtt.DomoticMqttService;
import fr.olivier.projet.repository.IpRepository;
import fr.olivier.projet.repository.PingRepository;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduled.ConcurrentExecution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
  * CheckIpTask
  */
  @ApplicationScoped
 public class CheckIpTask {
 
      private static final Logger LOG = Logger.getLogger(CheckIpTask.class);

    @Inject
    IpRepository ipRepository;

    @ConfigProperty(name = "ipMqtt.ping.mac")
    private List<String> listeMac;
  
    @Inject
    private PingRepository pingRepository;

    @Inject
    private DomoticMqttService domoticMqttService;

    @Scheduled(every="{ipMqtt.ping.delay}", concurrentExecution = ConcurrentExecution.SKIP) 
    void taskPing() {
        listeMac.stream().forEach(mac -> {
            ipRepository.find(mac).ifPresent(
                    nmapBo -> {
                        try {
                            pingProcess(nmapBo);
                        } catch (InterruptedException e) {
                            LOG.error(" Erreur sur le traitement du ping", e);
                        }
                    }

            );

        });
        
    }

    private void pingProcess(NmapBo nmapBo) throws InterruptedException {
        boolean pingb = pingRepository.pingAdress(nmapBo.getIp());
        LOG.info(" test ping  " + nmapBo.getIp() + " "  + pingb);
       
       
        if (pingb) {
            domoticMqttService.sendMessage(nmapBo, "on");
        } else if (nmapBo.isLastState() && !pingb) {
        /*
         * Cas d'un changement d etat vers false.
         * Mise en place d'un nouvel appel pour confirmer le false.
         */ 
            LOG.info(" Confirmation du false au prochain appel " + nmapBo.getIp());
        } else {
            domoticMqttService.sendMessage(nmapBo, "false");
        }
        nmapBo.setLastState(pingb);
    }

  
    
 }