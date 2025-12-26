/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Categorie {
    private int id;
    private String code;
    private String nom;
    private String description;
    private int nombreProduits;
    
    public Categorie() {}
    
    public Categorie(String code, String nom) {
        this.code = code;
        this.nom = nom;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getNombreProduits() { return nombreProduits; }
    public void setNombreProduits(int nombreProduits) { this.nombreProduits = nombreProduits; }
    
    @Override
    public String toString() { return code + " - " + nom; }
}