package eu.europa.ec.fisheries.uvms.spatial.dao;

import org.apache.commons.lang.StringEscapeUtils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Created by Michal Kopyczok on 27-Aug-15.
 */
@Stateless
@LocalBean
public class PostgreSqlEncoder {
    public String encode(String value) {
        return StringEscapeUtils.escapeSql(value);
    }
}
