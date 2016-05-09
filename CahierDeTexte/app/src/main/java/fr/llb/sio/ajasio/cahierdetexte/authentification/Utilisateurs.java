package fr.llb.sio.ajasio.cahierdetexte.authentification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BtsSio1 on 18/04/2016.
 */
public class Utilisateurs {
    protected String nom;
    protected String login;
    protected String passwd;
    private String descriptionClasse;
    List<String> lesClasses = new ArrayList<>();
    protected String type="";

    public Utilisateurs(String login, String passwd){
        this.login = login;
        this.passwd = passwd;
    }


    public String getLogin(){
        return this.login;
    }

    public String getNom() {
        return nom;
    }

    public String getDescriptionClasse() {
        return descriptionClasse;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPasswd(){
        return this.passwd;
    }

    public void setDescriptionClasse(String description){
        this.descriptionClasse = description;
    }

    public void setListClasse(List<String> lesClasses)
    {
        this.lesClasses = lesClasses;
    }

    public List<String> getLesClasses(){
        return lesClasses;
    }

    @Override
    public String toString(){
        String result;
        if ( type=="eleve")
            result = String.format("Login : %s\nClasse : %s", this.login, this.descriptionClasse);
        else
            result = String.format("Login : %s\nClasse : %s", this.login, this.lesClasses);
        return result;
    }
}
