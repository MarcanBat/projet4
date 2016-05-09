package fr.llb.sio.ajasio.cahierdetexte.authentification;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by BtsSio1 on 18/04/2016.
 */
public class Professeur extends Utilisateurs {

    List<String> lesClasses = new ArrayList<>();

    public Professeur(String login, String passwd){
        super(login,passwd);
    }

    public Professeur(Utilisateurs user) {
        super(user.login, user.passwd);
    }

    public void setListClasse(List<String> lesClasses)
    {
        this.lesClasses = lesClasses;
    }

    public List<String> getLesClasses(){
        return lesClasses;
    }
}
