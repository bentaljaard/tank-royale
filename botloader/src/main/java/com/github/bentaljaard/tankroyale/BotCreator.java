package com.github.bentaljaard.tankroyale;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.ResourceDefinitionContext;
import io.fabric8.openshift.client.OpenShiftClient;

@ApplicationScoped
public class BotCreator {

    // private ResourceDefinitionContext context;
    @Inject OpenShiftClient oc;
    ResourceDefinitionContext context;

    @ConfigProperty(name = "bot.version") String botVersion;
    @ConfigProperty(name = "bot.server.url") String botUrl;
    
    public BotCreator() {

    }

    public KubernetesResourceList<TankRoyaleBot> getAllBots(){
        //Create an API client for the CR
        MixedOperation<TankRoyaleBot,KubernetesResourceList<TankRoyaleBot>,Resource<TankRoyaleBot>> botClient = 
        oc.resources(TankRoyaleBot.class);

        //Use current namespace
        String currentNamespace = oc.getNamespace();
        return botClient.inNamespace(currentNamespace).list();
    }

    public boolean createBot(String name, String encodedCode){
        if (encodedCode == null || encodedCode.isBlank()){
            //Load default code template
            encodedCode = loadBotCodeTemplate("ExternalCodeBot.java_template");
        }

        //Decode base64 encoded code
        byte[] decodedBytes = Base64.getDecoder().decode(encodedCode);
        String botCode = new String(decodedBytes, StandardCharsets.UTF_8);

        //Use current namespace
        String currentNamespace = oc.getNamespace();
        // oc.namespaces().list();

        //Create an API client for the CR
        MixedOperation<TankRoyaleBot,KubernetesResourceList<TankRoyaleBot>,Resource<TankRoyaleBot>> botClient = 
        oc.resources(TankRoyaleBot.class);

        //Build out the CR definition
        Map<String,String> botDefinition = new HashMap<String,String>();
        botDefinition.put("name", name);
        botDefinition.put("version", botVersion);
        botDefinition.put("code", botCode);

        Map<String,String> serverDefinition = new HashMap<String,String>();
        serverDefinition.put("url",botUrl);
        
        //Create new spec from our maps
        TankRoyaleBotSpec botSpec = new TankRoyaleBotSpec(botDefinition,serverDefinition);

        //Create the CR
        botClient.inNamespace(currentNamespace)
            .createOrReplace(new TankRoyaleBot(name, botSpec));

        
        //TODO add logic to check that it was created?
        

        return true;
    }

    public String loadBotCodeTemplate(String resourceName){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream codeResource = (InputStream) loader.getResourceAsStream("ExternalCodeBot.java_template");
        byte[] text = null;
        try {
            text = codeResource.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String botCode = Base64.getEncoder().encodeToString(text);
        return botCode;
    }

    

}
