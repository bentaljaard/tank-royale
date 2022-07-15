package com.github.bentaljaard.tankroyale;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
    String botCode;
    
    public BotCreator() {
        
        context = new ResourceDefinitionContext.Builder()
            .withNamespaced(false)
            .withKind("TankRoyaleBot")
            .withGroup("tank-royale.quay.io")
            .withVersion("v1alpha1")
            .withPlural("TankRoyaleBots")
            .build();
    }



    public boolean createBot(String name, String code){
        String currentNamespace = oc.getNamespace();
        oc.namespaces().list();

        MixedOperation<TankRoyaleBot,KubernetesResourceList<TankRoyaleBot>,Resource<TankRoyaleBot>> botClient = 
            oc.resources(TankRoyaleBot.class);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream codeResource = (InputStream) loader.getResourceAsStream("ExternalCodeBot.java_template");
        byte[] text = null;
        try {
            text = codeResource.readAllBytes();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        botCode = new String(text, StandardCharsets.UTF_8);
        
        Map<String,String> botDefinition = new HashMap<String,String>();
        botDefinition.put("name", name);
        botDefinition.put("version", botVersion);
        botDefinition.put("code", botCode);

        Map<String,String> serverDefinition = new HashMap<String,String>();
        serverDefinition.put("url",botUrl);
       
        
        TankRoyaleBotSpec botSpec = new TankRoyaleBotSpec(botDefinition,serverDefinition);

        botClient.inNamespace(currentNamespace)
            .createOrReplace(new TankRoyaleBot(name, botSpec));

        
        //TODO add logic to check that it was created?
        

        return true;
    }

    

}
