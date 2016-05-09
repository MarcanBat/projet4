package fr.llb.sio.ajasio.cahierdetexte.authentification;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
/**
 * Created by BtsSio1 on 11/04/2016.
 */
public class Eleve extends Utilisateurs{
    private String descriptionClasse;

    public Eleve(String login, String passwd){
        super(login,passwd);
    }

    public Eleve(Utilisateurs user) {
        super(user.login, user.passwd);
    }
    public String getDescriptionClasse(){
        return this.descriptionClasse;
    }

    public void setDescriptionClasse(String description){
        this.descriptionClasse = description;
    }

    @Override
    public String toString(){
        String result = String.format("Login : %s\nClasse : %s", this.login, this.descriptionClasse);
        return result;
    }
}
