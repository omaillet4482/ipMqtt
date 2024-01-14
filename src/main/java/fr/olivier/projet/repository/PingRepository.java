package fr.olivier.projet.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Service permettant la réalisation d'une commande PING linux.
 */
@ApplicationScoped
public class PingRepository {

    private static final Logger LOG = Logger.getLogger(PingRepository.class);

    @ConfigProperty(name = "ipMqtt.ping.cmd", defaultValue = "ping -c 1 ")
    private String pingCmd;
    
    public synchronized boolean pingAdress(String ip) {
        try {
                    // For Linux and OSX
            String cmd = pingCmd + " " + ip;
           
            Process myProcess = Runtime.getRuntime().exec(cmd);
            if(LOG.isDebugEnabled()) {
                readOutput(myProcess.getInputStream()).stream().forEach(line -> {
                    LOG.debug("PING LOG : " + line);
                });;
            }
            

            myProcess.waitFor(1500, TimeUnit.MILLISECONDS);

            boolean result = myProcess.exitValue() == 0 ? true : false;

            LOG.debug(cmd+" result "+result);            

            myProcess.destroy();
            return result;

        } catch (Exception pExcept) {
            LOG.warn("Erreur sur IP " + ip , pExcept);
            return false;
        } 
    }

     private List<String> readOutput(InputStream inputStream) throws IOException {
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
            return output.lines()
                .collect(Collectors.toList());
        }
    }
}
