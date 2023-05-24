package com.example.progettomap;

import com.example.progettomap.data.Data;
import com.example.progettomap.data.OutOfRangeSampleSize;
import com.example.progettomap.keyboardInput.Keyboard;
import com.example.progettomap.mining.KMeansMiner;

public class MainTest {

    /**
     * @param args
     */
    public static void mainVecchio(String[] args) {
        Keyboard.setPrintErrors(false);
        int k, port, numIter;
        String server, db, tab, user, psw;
        Data data;
        KMeansMiner kmeans;
        do {
            try {
                if (option("Vuoi usare i valori di default? (y/n) ")) {
                    data = new Data("localhost", 3306, "MapDB", "playtennis", "MapUser", "map");
                } else {
                    System.out.print("Inserisci il nome del server (ad esempio localhost): ");
                    server = Keyboard.readString();
                    System.out.print("Inserisci la porta del server (ad esempio 3306): ");
                    port = Keyboard.readInt();
                    System.out.print("Inserisci il nome del database (ad esempio MapDB): ");
                    db = Keyboard.readString();
                    System.out.print("Inserisci il nome della tabella (ad esempio playtennis): ");
                    tab = Keyboard.readString();
                    System.out.print("Inserisci il nome dell'utente (ad esempio MapUser): ");
                    user = Keyboard.readString();
                    System.out.print("Inserisci la password dell'utente (ad esempio map): ");
                    psw = Keyboard.readString();
                    data = new Data(server, port, db, tab, user, psw);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
            System.out.println(data);
            System.out.print("Inserisci k: ");
            k = Keyboard.readInt();
            try {
                kmeans = new KMeansMiner(k);
                numIter = kmeans.kmeans(data);
            } catch (OutOfRangeSampleSize e) {
                System.out.println(e.getMessage());
                continue;
            }
            System.out.println("Numero di Iterazioni:" + numIter);
            System.out.println(kmeans.getC().toString(data));
        } while (option("Vuoi ripetere l'esecuzione? (y/n) "));
    }

    private static boolean option(String str) {
        char c;
        do {
            System.out.print(str);
            c = Character.toLowerCase(Keyboard.readChar());
        } while (c != 'y' && c != 'n');
        return c == 'y';
    }
}
