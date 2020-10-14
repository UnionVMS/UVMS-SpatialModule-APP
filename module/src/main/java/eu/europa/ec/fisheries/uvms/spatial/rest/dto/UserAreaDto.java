package eu.europa.ec.fisheries.uvms.spatial.rest.dto;


import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;

import java.util.List;

public class UserAreaDto {
    String name;
    String description;
    String id;
    List<String> scopeSelection;
    String startDate;
    String endDate;
    String areaGroup;
    String datasetName;
    String geometry;
    String extent;
    AreaType type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getScopeSelection() {
        return scopeSelection;
    }

    public void setScopeSelection(List<String> scopeSelection) {
        this.scopeSelection = scopeSelection;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAreaGroup() {
        return areaGroup;
    }

    public void setAreaGroup(String areaGroup) {
        this.areaGroup = areaGroup;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getExtent() {
        return extent;
    }

    public void setExtent(String extent) {
        this.extent = extent;
    }

    public AreaType getType() {
        return type;
    }

    public void setType(AreaType type) {
        this.type = type;
    }
}
