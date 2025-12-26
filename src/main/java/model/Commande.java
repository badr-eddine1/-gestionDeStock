package model;

import java.time.LocalDate;
import java.util.List;

public class Commande {
    private int id;
    private String numero;
    private String type;
    private Integer fournisseurId; 
    private Integer clientId;      
    private LocalDate dateCommande;
    private LocalDate dateLivraison;
    private String statut;
    private double totalHt;
    private double totalTtc;
    private double tva;
    private String modePaiement;
    private String remarques;
    private String fournisseurNom;
    private String clientNom;
    private List<LigneCommande> lignes;
    
    public Commande() {
        this.dateCommande = LocalDate.now();
        this.statut = "EN_ATTENTE";
        this.tva = 20.0;
        this.modePaiement = "ESPECES";
        this.totalHt = 0.0;
        this.totalTtc = 0.0;
    }
    
    public Commande(String numero, String type) {
        this();
        this.numero = numero;
        this.type = type;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Integer getFournisseurId() { return fournisseurId; }
    public void setFournisseurId(Integer fournisseurId) { this.fournisseurId = fournisseurId; }
    
    public Integer getClientId() { return clientId; }
    public void setClientId(Integer clientId) { this.clientId = clientId; }
    
    public LocalDate getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDate dateCommande) { this.dateCommande = dateCommande; }
    
    public LocalDate getDateLivraison() { return dateLivraison; }
    public void setDateLivraison(LocalDate dateLivraison) { this.dateLivraison = dateLivraison; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public double getTotalHt() { return totalHt; }
    public void setTotalHt(double totalHt) { this.totalHt = totalHt; }
    
    public double getTotalTtc() { return totalTtc; }
    public void setTotalTtc(double totalTtc) { this.totalTtc = totalTtc; }
    
    public double getTva() { return tva; }
    public void setTva(double tva) { this.tva = tva; }
    
    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }
    
    public String getRemarques() { return remarques; }
    public void setRemarques(String remarques) { this.remarques = remarques; }
    
    public String getFournisseurNom() { return fournisseurNom; }
    public void setFournisseurNom(String fournisseurNom) { this.fournisseurNom = fournisseurNom; }
    
    public String getClientNom() { return clientNom; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }
    
    public List<LigneCommande> getLignes() { return lignes; }
    public void setLignes(List<LigneCommande> lignes) { this.lignes = lignes; }
    
    public void calculerTotaux() {
        totalHt = 0;
        if (lignes != null) {
            for (LigneCommande ligne : lignes) {
                totalHt += ligne.getQuantite() * ligne.getPrixUnitaire() * (1 - ligne.getRemise() / 100);
            }
        }
        totalTtc = totalHt * (1 + tva / 100);
    }
    
    @Override
    public String toString() { return numero + " - " + type; }
}
