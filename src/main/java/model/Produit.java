/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Produit {
    private int id;
    private String reference;
    private String nom;
    private String description;
    private double prixAchat;
    private double prixVente;
    private int categorieId;
    private int fournisseurId;
    private int seuilAlerte;
    private String uniteMesure;
    private String image;
    private String categorieNom;
    private String fournisseurNom;
    private int quantiteStock;
    
    public Produit() {
        this.seuilAlerte = 10;
        this.uniteMesure = "Pièce";
    }
    
    public Produit(String reference, String nom, double prixAchat, double prixVente) {
        this();
        this.reference = reference;
        this.nom = nom;
        this.prixAchat = prixAchat;
        this.prixVente = prixVente;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getPrixAchat() { return prixAchat; }
    public void setPrixAchat(double prixAchat) { this.prixAchat = prixAchat; }
    
    public double getPrixVente() { return prixVente; }
    public void setPrixVente(double prixVente) { this.prixVente = prixVente; }
    
    public int getCategorieId() { return categorieId; }
    public void setCategorieId(int categorieId) { this.categorieId = categorieId; }
    
    public int getFournisseurId() { return fournisseurId; }
    public void setFournisseurId(int fournisseurId) { this.fournisseurId = fournisseurId; }
    
    public int getSeuilAlerte() { return seuilAlerte; }
    public void setSeuilAlerte(int seuilAlerte) { this.seuilAlerte = seuilAlerte; }
    
    public String getUniteMesure() { return uniteMesure; }
    public void setUniteMesure(String uniteMesure) { this.uniteMesure = uniteMesure; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public String getCategorieNom() { return categorieNom; }
    public void setCategorieNom(String categorieNom) { this.categorieNom = categorieNom; }
    
    public String getFournisseurNom() { return fournisseurNom; }
    public void setFournisseurNom(String fournisseurNom) { this.fournisseurNom = fournisseurNom; }
    
    public int getQuantiteStock() { return quantiteStock; }
    public void setQuantiteStock(int quantiteStock) { this.quantiteStock = quantiteStock; }
    
    public double getMarge() { return prixVente - prixAchat; }
    
    public double getTauxMarge() {
        if (prixAchat == 0) return 0;
        return ((prixVente - prixAchat) / prixAchat) * 100;
    }
    
    public boolean estEnRupture() { return quantiteStock <= seuilAlerte; }
    
    public String getStatutStock() {
        if (quantiteStock == 0) return "RUPTURE";
        if (quantiteStock <= seuilAlerte) return "ALERTE";
        return "DISPONIBLE";
    }
    
    @Override
    public String toString() { return reference + " - " + nom; }
}