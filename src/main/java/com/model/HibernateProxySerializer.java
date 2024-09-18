package com.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.hibernate.proxy.HibernateProxy;

import java.lang.reflect.Type;

public class HibernateProxySerializer implements JsonSerializer<HibernateProxy> {
    @Override
    public JsonElement serialize(HibernateProxy proxy, Type type, JsonSerializationContext context) {
        // Get the underlying entity and serialize it instead of the proxy
        return context.serialize(proxy.getHibernateLazyInitializer().getImplementation());
    }
}
