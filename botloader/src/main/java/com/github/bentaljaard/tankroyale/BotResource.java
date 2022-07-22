package com.github.bentaljaard.tankroyale;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api")
public class BotResource {

    @Inject BotCreator bc;

    @Path("/bot")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BotConfig createBot(BotConfig config) {
        bc.createBot(config.getName(), config.getCode());
        return config;
    }

    @Path("/bot")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BotConfig> listBots(){
        //TODO: list all bots (get CRs in namespace)
        return new ArrayList<BotConfig>();
    }

    
    //TODO: Remove bots (cleanup indiviual or all)
    //TODO: Update bot (eg. update code)
}
