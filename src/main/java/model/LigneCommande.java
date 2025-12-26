/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class LigneCommande {
    private int id;
    private int commandeId;
    private int produitId;
    private int quantite;
    private double prixUnitaire;
    private double remise;
    private String produitNom;
    
    public LigneCommande() {
        this.remise = 0.0;
    }
    
    public LigneCommande(int produitId, int quantite, double prixUnitaire) {
        this();
        this.produitId = produitId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getCommandeId() { return commandeId; }
    public void setCommandeId(int commandeId) { this.commandeId = commandeId; }
    
    public int getProduitId() { return produitId; }
    public void setProduitId(int produitId) { this.produitId = produitId; }
    
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    
    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    
    public double getRemise() { return remise; }
    public void setRemise(double remise) { this.remise = remise; }
    
    public String getProduitNom() { return produitNom; }
    public void setProduitNom(String produitNom) { this.produitNom = produitNom; }
    
    public double getTotalLigne() {
        return quantite * prixUnitaire * (1 - remise / 100);
    }
}
