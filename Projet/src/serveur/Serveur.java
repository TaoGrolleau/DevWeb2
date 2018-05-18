package serveur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import tchatche.GestionMessages;
import tchatche.Message;

public class Serveur extends Thread {
	private GestionMessages chat;
	private boolean newmodif;
	private String fichier;
	private HashMap<Integer, String> fichierMap;
	private HashMap<Integer, GestionMessages> chatMap;
	public Serveur() {
		chat = new GestionMessages();
		this.fichier = "";
		this.fichierMap = new HashMap<Integer, String>();
		this.chatMap = new HashMap<Integer, GestionMessages>();
		this.start();
	}

	public boolean getnewmodif() {
		return newmodif;
	}

	public void setnewmodif(boolean newmodif) {
		this.newmodif = newmodif;
	}

	public String getfichier() {
		return fichier;
	}

	public void setfichier(String fichier) {
		this.fichier = fichier;
	}

	public String getFichierMap(String idFichier, String lienFichier) {
		int idFichierInt = Integer.parseInt(idFichier);
		if (fichierMap.containsKey(idFichierInt))
			return fichierMap.get(idFichierInt);

		File fichier = new File(lienFichier);
		try {
			String strFichier = "";
			BufferedReader bfr = new BufferedReader(new FileReader(fichier));
			String ligne = bfr.readLine();

			while (ligne != null) {
				ligne += "\n";
				strFichier += ligne;
				ligne = bfr.readLine();
			}
			bfr.close();
			fichierMap.put(idFichierInt, strFichier);
			return strFichier;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void setFichierMap(String idFichier, String fichier, String lienFichier) {
		fichierMap.put(Integer.parseInt(idFichier), fichier);
		if (fichier != null) {
			try {
				PrintWriter out = new PrintWriter(lienFichier);
				out.print(fichier);
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public GestionMessages getChatMap(String idFichier){
		int idFichierInt = Integer.parseInt(idFichier);
		
		if (!chatMap.containsKey(idFichierInt)) {
			
			chatMap.put(idFichierInt, new GestionMessages());
		}
		
			return chatMap.get(idFichierInt);
	}

	public void run() {
		final int PORT = 8888;
		try {
			ServerSocket server = new ServerSocket(PORT, 1);
			String ligne, l1, l2;
			while (true) {
				Socket s = server.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter out = new PrintWriter(s.getOutputStream());

				ligne = in.readLine();
				while (!(ligne.equals("xyz") && ligne != null)) {
					if (ligne.equals("nbmessages")) {
						ligne = in.readLine();
						if(chatMap.containsKey(Integer.parseInt(ligne))){
							chat = chatMap.get(Integer.parseInt(ligne));
							
						}
						else {
							chat = getChatMap(ligne);
						}
						out.println(chat.nbmessage());
						out.flush();
					} else if (ligne.equals("add")) {
						ligne = in.readLine();
						if(chatMap.containsKey(Integer.parseInt(ligne))){
							chat = chatMap.get(Integer.parseInt(ligne));
							
						}
						else {
							chat = getChatMap(ligne);
						}
						l1 = in.readLine();
						l2 = in.readLine();
						if (!ligne.equals("")) {
							chat.add(l1, l2);
						}
					}else if (ligne.equals("ajouteruser")) {
						l1 = in.readLine();
						l2 = in.readLine();
						
						try {
							basededonnees.Request.addUsertoFile(l2, Integer.parseInt(l1));
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
					else if (ligne.equals("supprimeruser")) {
						l1 = in.readLine();
						l2 = in.readLine();
						
						try {
							basededonnees.Request.delUsertoFile(l2, Integer.parseInt(l1));
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if (ligne.equals("fichier")) {
						String f = "";
						ligne = in.readLine();
						while (!(ligne.equals(";;//*::::;;;;:;"))) {
							f = f + ligne;
							ligne = in.readLine();
							if (!(ligne.equals(";;//*::::;;;;:;"))) {
								f = f + '\n';
							}
						}
						String id = in.readLine();
						fichier = fichierMap.get(Integer.parseInt(id));
						ligne = in.readLine();
						setFichierMap(id,f,ligne);
						newmodif = true;
					} else if (ligne.equals("getfichier")) {
						String id = in.readLine();
						ligne = in.readLine();
						if(fichierMap.containsKey(Integer.parseInt(id))){
							fichier = fichierMap.get(Integer.parseInt(id));
							
						}
						else {
							fichier = getFichierMap(id,ligne);
						}
						
						out.println(fichier + "\n;;//*::::;;;;:;\n");
						out.flush();
					} else if (ligne.equals("connexion")) {
						l1 = in.readLine();
						l2 = in.readLine();
						try {
							if (basededonnees.Request.check_user(l1, l2)) {
								out.println("true\n");
								out.flush();
							} else {
								out.println("false\n");
								out.flush();
							}
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (ligne.equals("crecompte")) {
						l1 = in.readLine();
						l2 = in.readLine();
						try {
							if (basededonnees.Request.addUser(l1, l2)) {
								out.println("true\n");
								out.flush();
							} else {
								out.println("false\n");
								out.flush();
							}
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (ligne.equals("nbfichier")) {
						ligne = in.readLine();
						try {
							ArrayList<String[]> listef = basededonnees.Request.user_file(ligne);
							out.println(listef.size() + "\n");
							out.flush();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						out.println("-1\n");
						out.flush();
					} else if (ligne.equals("actufichier")) {
						ligne = in.readLine();
						try {
							ArrayList<String[]> listef = basededonnees.Request.user_file(ligne);
							for (int i = 0; i < listef.size(); i++) {
								
								out.println(listef.get(i)[0] + "\n" + listef.get(i)[1] + "\n" + listef.get(i)[2] + "\n"
										+ listef.get(i)[3] + "\n");
							}
							out.println("fin\n");
							out.flush();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						out.println("-1\n");
						out.flush();
					}
					else if (ligne.equals("membrefichier")) {
						ligne = in.readLine();
						try {
							
							ArrayList<String> listeM = basededonnees.Request.list_user(Integer.parseInt(ligne));
							for (int i = 0; i < listeM.size(); i++) {
								
								out.println(listeM.get(i) + "\n");
							}
							out.println("fin\n");
							out.flush();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (ligne.equals("newfichier")) {
						String f = "";
						String pseudo;
						String name_file;
						String link_file;
						pseudo = in.readLine();
						name_file = in.readLine();
						ligne = in.readLine();
						while (!(ligne.equals(";;//*::::;;;;:;"))) {
							f = f + ligne;
							ligne = in.readLine();
							if (!(ligne.equals(";;//*::::;;;;:;"))) {
								f = f + '\n';
							}
						}
						//ligne = in.readLine();

						fichier = f;
						link_file = ("/home/lucas/uploads/Lucas/"+name_file);
						File fi = new File(link_file) ;
						PrintWriter outf = new PrintWriter(new FileWriter(fi)) ;
						outf.write(fichier) ; 
						outf.close() ; 
						try {
							basededonnees.Request.addFile(pseudo, name_file, link_file);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						newmodif = true;
					} else {
						if (!ligne.equals("xyz")) {
							if(chatMap.containsKey(Integer.parseInt(ligne))){
								chat = chatMap.get(Integer.parseInt(ligne));
								
							}
							else {
								chat = getChatMap(ligne);
							}
							ligne = in.readLine();
							out.println(chat.afficherClientLourd(Integer.parseInt(ligne)));
							out.flush();
						}
					}
					ligne = in.readLine();
				}
				s.close();

			}
		} catch (IOException e) {
		}

	}

	public GestionMessages getChat() {
		return chat;
	}

}
