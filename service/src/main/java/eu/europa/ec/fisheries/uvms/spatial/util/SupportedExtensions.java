package eu.europa.ec.fisheries.uvms.spatial.util;

public enum SupportedExtensions {
    DBF("dbf"),
    PRJ("prj"),
    SHP("shp"),
    SHX("shx");

    private final String ext;

    SupportedExtensions(String ext) {
        this.ext = ext;
    }

    public String getExt() {
        return ext;
    }

    static SupportedExtensions fromValue(String ext) {
        for (SupportedExtensions extension : values()) {
            if (extension.getExt().equalsIgnoreCase(ext)) {
                return extension;
            }
        }
        throw new IllegalArgumentException("Unsupported extension");
    }

}