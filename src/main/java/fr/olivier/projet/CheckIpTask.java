 package fr.olivier.projet;


import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import fr.olivier.projet.repository.IpRepository;
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

    @Scheduled(every="{ipMqtt.ping.delay}", concurrentExecution = ConcurrentExecution.SKIP) 
    void taskPing() {
        listeMac.stream().forEach(mac -> {
            ipRepository.find(mac).ifPresent(
                    nmapBo -> {
                        LOG.info(" Ping " + nmapBo.getName() + " ip:" + nmapBo.getIp());
                        LOG.info(" test ping  " + pingAdress(nmapBo.getIp()));
                    }

            );

        });
        
    }

    private synchronized boolean pingAdress(String ip) {
        try {
            String cmd = "";
            if(System.getProperty("os.name").startsWith("Windows")) {   
                    // For Windows
                    cmd = "ping -n 1 " + ip;
            } else {
                    // For Linux and OSX
                    cmd = "ping -c 1 " + ip;
            }

            Process myProcess = Runtime.getRuntime().exec(cmd);
            myProcess.waitFor();

            if(myProcess.exitValue() == 0) {

                    return true;
            } else {

                    return false;
            }

        } catch (Exception pExcept) {
            LOG.warn("Erreur sur IP " + ip , pExcept);
            return false;
        }
    }
    
 }