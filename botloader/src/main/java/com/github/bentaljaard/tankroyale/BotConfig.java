package com.github.bentaljaard.tankroyale;

public class BotConfig {

    public String name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BotConfig(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public BotConfig(){

    }


    
}
