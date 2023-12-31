package fr.olivier.projet;

import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import fr.olivier.projet.bo.NmapBo;
import fr.olivier.projet.mqtt.DomoticMqttService;
import fr.olivier.projet.repository.IpRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/checkIp")
public class CheckIpResource {

    private static final Logger LOG = Logger.getLogger(CheckIpResource.class);
   

    private Pattern ligneUN;

    private Pattern ligneTrois;

    @Inject
    private IpRepository ipRepository;

    @Inject
    private DomoticMqttService domoticMqttService;
    

    @Inject
    public CheckIpResource( @ConfigProperty(name = "ipMqtt.parseur.ligne1") String ligne1,
        @ConfigProperty(name = "ipMqtt.parseur.ligne3") String ligne3 ) {
        this.ligneUN = Pattern.compile(ligne1); 
        this.ligneTrois = Pattern.compile(ligne3); 
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String postIp(String body) {
        parseBody(body);
        return "size " + ipRepository.findAll().size();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listIp(){
        return Response.ok(ipRepository.findAll()).build();
    }

     @GET
    @Path("/mqtt/{topic}/{message}")
    @Produces(MediaType.TEXT_PLAIN)
    public String testmessage(String topic, String message){
        domoticMqttService.sendMessage(topic, message);
        return "send "+ message;
    }

     @GET
    @Path("/config/{mac}")
    @Produces(MediaType.TEXT_PLAIN)
    public String testmessage(String mac){
        Optional<NmapBo> equipe = ipRepository.find(mac);

        equipe.ifPresent( nmap -> {
             domoticMqttService.sendMessageConfig(nmap);

        });
       
        return "send "+ mac;
    }
    /**
     * Parsing des flux du nmap
     * 
     
     * @param fluxRaw - Flux brute
     */
    protected void parseBody(String fluxRaw) {
        try (final var scanner = new Scanner(fluxRaw)){
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();
                LOG.info(currentLine);
                Matcher matcherUN = ligneUN.matcher(currentLine);
                if(matcherUN.find()){

                    String name = matcherUN.group("name");
                    String ip = matcherUN.group("ip");
                    NmapBo nmapBo = new NmapBo();
                    nmapBo.setIp(ip);
                    nmapBo.setName(name);
                    // lecture ligne 2 obligatoire
                    if(scanner.hasNextLine()) {
                        scanner.nextLine();
                        // lecture ligne 3
                         if(scanner.hasNextLine()) {
                            String currentLigne3 = scanner.nextLine();
                            Matcher matcherTrois = ligneTrois.matcher(currentLigne3);
                            if(matcherTrois.find()) {
                                    nmapBo.setMac(matcherTrois.group("mac"));
                                    ipRepository.add(nmapBo);
                                }

                            }

    
                         }
                    }
                    }
                  
                }
            }
        }
