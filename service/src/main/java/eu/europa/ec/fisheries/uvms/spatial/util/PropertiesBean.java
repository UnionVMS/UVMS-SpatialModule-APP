package eu.europa.ec.fisheries.uvms.spatial.util;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

@Stateless
@Slf4j
public class PropertiesBean {

    private Properties props;
    private AtomicInteger accessCount = new AtomicInteger(0);

    @PostConstruct
    public void startup() {

        log.info("In PropertiesBean(Singleton)::startup()");

        try {
            InputStream propsStream =
                    PropertiesBean.class.getResourceAsStream("/config.properties");
            props = new Properties();

            props.load(propsStream);
        } catch (IOException e) {
            throw new EJBException("PropertiesBean initialization error", e);
        }
    }

    public String getProperty(final String name) {
        accessCount.incrementAndGet();
        return props.getProperty(name);
    }

    public int getAccessCount() {
        return accessCount.get();
    }

    @PreDestroy
    private void shutdown() {
        log.info("In PropertiesBean(Singleton)::shutdown()");
    }
}