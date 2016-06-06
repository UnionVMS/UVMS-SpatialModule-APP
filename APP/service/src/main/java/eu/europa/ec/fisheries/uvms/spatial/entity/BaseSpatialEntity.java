package eu.europa.ec.fisheries.uvms.spatial.entity;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import eu.europa.ec.fisheries.uvms.domain.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMappingProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import javax.persistence.AttributeOverride;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.opengis.feature.Property;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MappedSuperclass
@ToString
@EqualsAndHashCode(callSuper = true)
@Slf4j
@AttributeOverride(name = "id", column = @Column(name = "GID"))
public class BaseSpatialEntity extends BaseEntity {

    private static final String ISO_8859_1 = "ISO-8859-1";
    private static final String UTF_8 = "UTF-8";

    @Type(type = "org.hibernate.spatial.GeometryType")
    @ColumnAliasName(aliasName = "geometry")
    private Geometry geom;

    @Column(length = 255)
    @ColumnAliasName(aliasName="name")
    private String name;

    @Column(length = 20)
    @ColumnAliasName(aliasName = "code")
    private String code;

    @Convert(converter = CharBooleanConverter.class)
    @Column(nullable = false, length = 1)
    @ColumnAliasName(aliasName = "enabled")
    private Boolean enabled = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enabled_on")
    private Date enabledOn;

    public BaseSpatialEntity(Map<String, Object> values, List<UploadMappingProperty> mapping) throws ServiceException {

        try {
            geom = (Geometry) values.get("the_geom");
            enabled = true;
            enabledOn = new Date();
            if (mapping != null){
                for (UploadMappingProperty property : mapping){
                    Object value = values.get(property.getSource());
                    if ("code".equals(property.getTarget())){
                        if (value!= null){
                            code = String.valueOf(value);
                        }
                    }
                    else if ("name".equals(property.getTarget())){
                        if (value!= null){
                            name = String.valueOf(value);
                        }
                    }
                    else {
                        FieldUtils.writeDeclaredField(this, property.getTarget(), value, true);
                    }
                }
            }

        } catch (IllegalAccessException e) {
            throw new ServiceException("ERROR WHILE MAPPING ENTITY", e);
        }
    }

    public BaseSpatialEntity() {
        // why JPA why
    }

    public Geometry getGeom() {
        return this.geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getEnabledOn() {
        return enabledOn;
    }

    public void setEnabledOn(Date enabledOn) {
        this.enabledOn = enabledOn;
    }

    protected String readStringProperty(Map<String, Object> values, String propertyName) throws ServiceException {
        try {
            return new String(((String) values.get(propertyName)).getBytes(ISO_8859_1), UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("Area upload media not supported", e);
        }
    }

    public static Map<String, Object> createAttributesMap(List<Property> properties) {
        Map<String, Object> resultMap = Maps.newHashMap();
        for (Property property : properties) {
            String name = property.getName().toString();
            Object value = property.getValue();
            resultMap.put(name, value);
        }
        return resultMap;
    }

    public Map<String, Object> getFieldMap(){
        Map<String, Object> map = new HashMap<>();

        try {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            Field[] superDeclaredFields = this.getClass().getSuperclass().getDeclaredFields();
            Field[] fields = ArrayUtils.addAll(declaredFields, superDeclaredFields);
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(ColumnAliasName.class)) {
                    String aliasName = field.getAnnotation(ColumnAliasName.class).aliasName();

                    log.info("Alias Name : " + aliasName);
                    Object value;
                    if ((field.get(this) instanceof Number)) {
                        Number numberVal = (Number) field.get(this);
                        value = String.valueOf(numberVal);
                    } else if ((field.get(this) instanceof Geometry)) {
                        Geometry geometry = ((Geometry) field.get(this));
                        value = new WKTWriter().write(geometry);
                    } else if ((field.get(this) instanceof Date)) {
                        value = DateUtils.UI_FORMATTER.print(new DateTime(field.get(this)));
                    } else if ((field.get(this) instanceof Boolean)) {
                        value = Boolean.toString((Boolean) field.get(this));
                    } else {
                        value = field.get(this);
                    }
                    map.put(aliasName, value);
                    log.info("Value is : " + value);
                }
            }
        } catch (IllegalAccessException e) {
            log.error("Illegal access exception : ", e);
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
        return map;
    }
}
