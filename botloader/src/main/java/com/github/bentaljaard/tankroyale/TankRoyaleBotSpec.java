package com.github.bentaljaard.tankroyale;

import java.io.Serializable;
import java.util.Map;

public class TankRoyaleBotSpec implements Serializable{

    private Map<String,String> bot;
    public Map<String, String> getBot() {
        return bot;
    }

    public void setBot(Map<String, String> bot) {
        this.bot = bot;
    }

    private Map<String,String> server; 

    public Map<String, String> getServer() {
        return server;
    }

    public void setServer(Map<String, String> server) {
        this.server = server;
    }

    public TankRoyaleBotSpec(){
        super();
    }

    public TankRoyaleBotSpec(Map<String,String> bot, Map<String,String> server){
        this.bot = bot;
        this.server = server;
    }

    @Override
    public String toString() {
        return "TankRoyaleBotSpec [bot=" + bot + ", server=" + server + "]";
    }
    
}
