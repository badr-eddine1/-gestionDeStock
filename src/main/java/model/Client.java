/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Client {
    private int id;
    private String code;
    private String nom;
    private String type;
    private String telephone;
    private String email;
    private String adresse;
    private String ville;
    private double credit;
    
    public Client() {
        this.type = "PARTICULIER";
        this.credit = 0.0;
    }
    
    public Client(String code, String nom) {
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
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    
    public double getCredit() { return credit; }
    public void setCredit(double credit) { this.credit = credit; }
    
    @Override
    public String toString() { return code + " - " + nom; }
}
