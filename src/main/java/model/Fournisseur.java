/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Fournisseur {
    private int id;
    private String code;
    private String nom;
    private String telephone;
    private String email;
    private String adresse;
    private String ville;
    private String pays;
    
    public Fournisseur() { this.pays = "Maroc"; }
    
    public Fournisseur(String code, String nom, String telephone) {
        this();
        this.code = code;
        this.nom = nom;
        this.telephone = telephone;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    
    @Override
    public String toString() { return code + " - " + nom; }
}