/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

@Stateless
public class PropertiesBean2 {

    final static Logger LOG = LoggerFactory.getLogger(PropertiesBean2.class);

    private Properties props;
    private AtomicInteger accessCount = new AtomicInteger(0);

    @PostConstruct
    public void startup() {

        LOG.info("In PropertiesBean(Singleton)::startup()");

        try {
            InputStream propsStream =
                    PropertiesBean2.class.getResourceAsStream("/config.properties");
            props = new Properties();

            props.load(propsStream);

            propsStream.close();

            InputStream propsStream2 =
                    PropertiesBean2.class.getResourceAsStream("/logging.properties");

            props.load(propsStream2);
            propsStream2.close();

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
        LOG.info("In PropertiesBean(Singleton)::shutdown()");
    }
}