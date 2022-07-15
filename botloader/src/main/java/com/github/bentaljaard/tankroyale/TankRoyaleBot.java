package com.github.bentaljaard.tankroyale;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Version("v1alpha1")
@Group("tank-royale.quay.io")
public class TankRoyaleBot extends CustomResource<TankRoyaleBotSpec, Void> implements Namespaced{

    public TankRoyaleBot() {
        super();
    }

    public TankRoyaleBot(String metaName, TankRoyaleBotSpec spec) {
        setMetadata(new ObjectMetaBuilder().withName(metaName).build());
        setSpec(spec);
    }
    
}
