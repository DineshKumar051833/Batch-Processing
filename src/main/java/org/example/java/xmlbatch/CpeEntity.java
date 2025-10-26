package org.example.java.xmlbatch;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cpe_entry")
@XmlRootElement(name = "cpe_item")
@XmlAccessorType(XmlAccessType.FIELD)
public class CpeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement(name = "cpe_title")
    @Column(name = "cpe_title", nullable = false)
    private String title;

    @XmlElement(name = "cpe_22_uri")
    @Column(name = "cpe_22_uri", nullable = false)
    private String cpe22URI;

    @XmlElement(name = "cpe_23_uri")
    @Column(name = "cpe_23_uri", nullable = false)
    private String cpe23URI;

    @XmlElementWrapper(name = "reference_links")
    @XmlElement(name = "link")
    @ElementCollection
    @CollectionTable(name = "cpe_reference_links", joinColumns = @JoinColumn(name = "cpe_id"))
    @Column(name = "link")
    private List<String> references;

    @XmlElement(name = "cpe_22_deprecation_date")
    @XmlJavaTypeAdapter(LocalDateAdapterDTO.class)
    @Column(name = "cpe_22_deprecation_date")
    private LocalDate cpe22DeprecationDate;

    @XmlElement(name = "cpe_23_deprecation_date")
    @XmlJavaTypeAdapter(LocalDateAdapterDTO.class)
    @Column(name = "cpe_23_deprecation_date")
    private LocalDate Cpe23DeprecationDate;

    public CpeEntity(){}

    public CpeEntity(Long id, String title, String cpe22URI, String cpe23URI, List<String> references, LocalDate cpe22DeprecationDate, LocalDate cpe23DeprecationDate) {
        this.id = id;
        this.title = title;
        this.cpe22URI = cpe22URI;
        this.cpe23URI = cpe23URI;
        this.references = references;
        this.cpe22DeprecationDate = cpe22DeprecationDate;
        Cpe23DeprecationDate = cpe23DeprecationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCpe22URI() {
        return cpe22URI;
    }

    public void setCpe22URI(String cpe22URI) {
        this.cpe22URI = cpe22URI;
    }

    public String getCpe23URI() {
        return cpe23URI;
    }

    public void setCpe23URI(String cpe23URI) {
        this.cpe23URI = cpe23URI;
    }

    public List<String> getReferences() {
        return references;
    }

    public void setReferences(List<String> references) {
        this.references = references;
    }

    public LocalDate getCpe22DeprecationDate() {
        return cpe22DeprecationDate;
    }

    public void setCpe22DeprecationDate(LocalDate cpe22DeprecationDate) {
        this.cpe22DeprecationDate = cpe22DeprecationDate;
    }

    public LocalDate getCpe23DeprecationDate() {
        return Cpe23DeprecationDate;
    }

    public void setCpe23DeprecationDate(LocalDate cpe23DeprecationDate) {
        Cpe23DeprecationDate = cpe23DeprecationDate;
    }
}