package fr.llb.sio.ajasio.cahierdetexte.authentification;


import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;

import java.util.ArrayList;
import java.util.List;



public abstract class ConnexionLDAP {
	
	private static String dn = "uid=%s,ou=People,dc=llb,dc=fr";

	
	public static LDAPConnection loginLDAP(Utilisateurs user) throws LDAPException{
		final LDAPConnection connect = new LDAPConnection("llb.ac-corse.fr", 389);
		
		if(!user.getLogin().isEmpty() && !user.getLogin().isEmpty()){
			try{
				connect.bind(String.format(dn, user.getLogin()), user.getPasswd());
				System.out.println(String.format(dn, user.getLogin()));
			}catch(LDAPException e){
				connect.close();
			}
		}	
		return connect;
	}

	
	public static Utilisateurs searchType(LDAPConnection connect, Utilisateurs user){
		//String classe = "";
		//List<String> lesClasses = new ArrayList<>();
		try {

			//Filter filterLDAP = Filter.create("(memberUid="+user.getLogin()+")");

			//Filter filterLDAP = Filter.create("(&(memberUid="+user.getLogin()+")(|(cn=Classe_*)(cn=Equipe_*)))");

			Filter filterLDAP = Filter.create("(&(memberUid="+user.getLogin()+")(cn=Equipe_*))");

			//Requete pour les tests
			//Filter filterLDAP = Filter.create("(&(memberUid=abeest19)(cn=Classe_*))");
			
			SearchRequest searchRequest = new SearchRequest("ou=Groups,dc=llb,dc=fr", SearchScope.SUB, filterLDAP, "cn", "description", "memberUid");

			SearchResult result = connect.search(searchRequest);

			//Test pour savoir si c'est un professeur ou un prof.
			//String test = result.getSearchEntries().get(0).getAttributeValue("cn");

			/*if(result.getEntryCount() > 0) {
				System.out.println(test);
				if (test == "Eleves"){
					user.type="eleve";
				}
				else{
					user.type="prof";
				}
				System.out.println(user.type);
			}*/
			if(result.getEntryCount() > 0)
			{
					user.type="prof";
			}
			else
			{
				filterLDAP = Filter.create("(&(memberUid="+user.getLogin()+")(cn=Classe_*))");
				searchRequest = new SearchRequest("ou=Groups,dc=llb,dc=fr", SearchScope.SUB, filterLDAP, "cn", "description", "memberUid");
				result = connect.search(searchRequest);
				if(result.getEntryCount() > 0)
				{
					user.type="eleve";
				}
			}
				System.out.println(user.type);


			
		} catch (LDAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	
	public static String searchNom(LDAPConnection connect, Utilisateurs user){
		String nom = "";
		try {
			Filter filterLDAP = Filter.create("uid=" + user.getLogin());
			
			//Requete pour les tests
			//Filter filterLDAP = Filter.create("(&(memberUid=abeest19)(cn=Classe_*))");
			
			SearchRequest searchRequest = new SearchRequest("ou=People,dc=llb,dc=fr", SearchScope.SUB, filterLDAP, "cn");
			
			SearchResult result = connect.search(searchRequest);
			
			if(result.getEntryCount() > 0){
				for(SearchResultEntry entry : result.getSearchEntries()){
					nom = entry.getAttributeValue("cn");
					System.out.println(nom);
				}
			}
			
		} catch (LDAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nom;
		
		
	}
}
