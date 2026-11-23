package tn.pedialink.dto;

import java.time.LocalDate;

public class SuiviCroissanceDto {
    private String patientId;
    private LocalDate dateMesure;
    private Double poids;
    private Double taille;
    private Integer ageEnMois;
    private String observation;

     // Getters andSetters
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public LocalDate getDateMesure() {
        return dateMesure;
    }

    public void setDateMesure(LocalDate dateMesure) {
        this.dateMesure = dateMesure;
    }

    public Double getPoids() {
        return poids;
    }

    public void setPoids(Double poids) {
        this.poids = poids;
    }

    public Double getTaille() {
        return taille;
    }

    public void setTaille(Double taille) {
        this.taille = taille;
    }

    public Integer getAgeEnMois() {
        return ageEnMois;
    }

    public void setAgeEnMois(Integer ageEnMois) {
        this.ageEnMois = ageEnMois;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
