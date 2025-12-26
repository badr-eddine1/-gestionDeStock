/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

public class Stock {
    private int id;
    private int produitId;
    private String produitNom;
    private int quantite;
    private int quantiteMin;
    private int quantiteMax;
    private String emplacement;
    private LocalDateTime dateMaj;
    
    public Stock() {
        this.quantiteMin = 0;
        this.quantiteMax = 1000;
        this.dateMaj = LocalDateTime.now();
    }
    
    public Stock(int produitId, int quantite) {
        this();
        this.produitId = produitId;
        this.quantite = quantite;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getProduitId() { return produitId; }
    public void setProduitId(int produitId) { this.produitId = produitId; }
    
    public String getProduitNom() { return produitNom; }
    public void setProduitNom(String produitNom) { this.produitNom = produitNom; }
    
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    
    public int getQuantiteMin() { return quantiteMin; }
    public void setQuantiteMin(int quantiteMin) { this.quantiteMin = quantiteMin; }
    
    public int getQuantiteMax() { return quantiteMax; }
    public void setQuantiteMax(int quantiteMax) { this.quantiteMax = quantiteMax; }
    
    public String getEmplacement() { return emplacement; }
    public void setEmplacement(String emplacement) { this.emplacement = emplacement; }
    
    public LocalDateTime getDateMaj() { return dateMaj; }
    public void setDateMaj(LocalDateTime dateMaj) { this.dateMaj = dateMaj; }
    
    public boolean estEnAlerte() { return quantite <= quantiteMin; }
    
    public boolean estEnSurstock() { return quantite >= quantiteMax; }
    
    public double getTauxRemplissage() {
        if (quantiteMax == 0) return 0;
        return (quantite * 100.0) / quantiteMax;
    }
    
    public String getStatut() {
        if (quantite == 0) return "RUPTURE";
        if (estEnAlerte()) return "ALERTE";
        if (estEnSurstock()) return "SURSTOCK";
        return "NORMAL";
    }
    
    public void ajouterQuantite(int qte) {
        this.quantite += qte;
        this.dateMaj = LocalDateTime.now();
    }
    
    public void retirerQuantite(int qte) {
        this.quantite -= qte;
        this.dateMaj = LocalDateTime.now();
    }
}
