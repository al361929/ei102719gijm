package majorsacasa.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Contract {
    Integer idContract;
    String firma;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate releaseDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dateDown;
    Integer cantidad;
    String descripcion;
    String nifcompany;
    String dnielderly;

    public Contract() {
    }

    public Integer getIdContract() {
        return idContract;
    }

    public void setIdContract(Integer idContract) {
        this.idContract = idContract;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDate getDateDown() {
        return dateDown;
    }

    public void setDateDown(LocalDate dateDown) {
        this.dateDown = dateDown;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNifcompany() {
        return nifcompany;
    }

    public void setNifcompany(String nifcompany) {
        this.nifcompany = nifcompany;
    }

    public String getDnielderly() {
        return dnielderly;
    }

    public void setDnielderly(String dnielderly) {
        this.dnielderly = dnielderly;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "idContract=" + idContract +
                ", firma='" + firma + '\'' +
                ", releaseDate=" + releaseDate +
                ", dateDown=" + dateDown +
                ", cantidad=" + cantidad +
                ", descripcion='" + descripcion + '\'' +
                ", nifcompany='" + nifcompany + '\'' +
                ", dnielderly='" + dnielderly + '\'' +
                '}';
    }
}
