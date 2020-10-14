package eu.europa.ec.fisheries.uvms.spatial.service.entity;


import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "area_update")
public class AreaUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "uploaded_file")
    byte[] uploadedFile;

    @Column(name = "area_type")
    String areaType;

    @Column(name = "uploaded_date")
    Instant uploadDate;

    String uploader;

    @Column(name = "process_completed")
    boolean processCompleted = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(byte[] uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public Instant getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Instant uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public boolean isProcessCompleted() {
        return processCompleted;
    }

    public void setProcessCompleted(boolean processCompleted) {
        this.processCompleted = processCompleted;
    }
}
