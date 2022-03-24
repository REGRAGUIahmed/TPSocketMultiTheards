package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurMT extends Thread {
	private boolean isActive=true;
	private int nombreClient=0;
	public static void main(String[] args) {
		new ServeurMT().start();
		//System.out.println("Suite de l'application");
	}
	@Override 
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(1234);
			System.out.println("Démarrage de serveur ...");
			while(isActive) {
				Socket socket = ss.accept();
				++nombreClient;
				new Conversation(socket,nombreClient).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	class Conversation extends Thread{
		private Socket socket;
		private int nombreClient;
		public Conversation(Socket s, int nombreClient) {
			this.socket = s;
			this.nombreClient=nombreClient;
		}
		@Override //////////////////
		public void run() {
			try {
				InputStream is = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				
				OutputStream os = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(os, true);
				String IP = socket.getRemoteSocketAddress().toString();
				System.out.println("Connexion de client numero: "+nombreClient+" IP= "+IP);
				pw.println("Bienvenu vous etes le client numéro"+nombreClient);
				while(true) {
					String req = br.readLine();
					String reponse = "Length= " + req.length();
					//System.out.println("Le client "+IP+" a envoyé une requete " +req);
					pw.println(reponse);
				}	
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
