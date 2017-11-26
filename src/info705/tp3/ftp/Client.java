package info705.tp3.ftp;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static String path = "D:/Partage/";

    private Client() {}

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            Ftp stub = (Ftp) registry.lookup("Ftp");

            /*
            byte[] data = stub.get("D:/Images/Avatars/blackbird.jpg");
            stub.put("D:/Images/Avatars/blackbird_copie.jpg", data);
            */

            String cmd;
            Scanner scanner = new Scanner(System.in);

            do {
                System.out.print(stub.pwd() + " >> ");
                cmd = scanner.nextLine();
                switch (cmd) {
                    case "ls":
                        List<String> res = stub.ls();
                        if(res == null) {
                            System.out.println("Aucun fichier dans le répertoire courant");
                        } else {
                            for(String file : stub.ls()) {
                                System.out.println(file);
                            }
                        }
                        break;
                    case "exit":
                        System.out.println("Le programme est terminé.");
                        break;
                    default:
                        if(cmd.contains("cd")) {
                            stub.cd(cmd.substring(2).trim());
                            stub.ls();
                        } else if(cmd.contains("pwd")) {
                            System.out.println(stub.pwd());
                        } else if(cmd.contains("get")) {
                            byte[] file = stub.get(cmd.substring(3).trim());
                            Client.put(cmd.substring(3).trim(), file);
                            System.out.println("Le fichier a été récupéré dans le dossier : " + Client.path);
                        } else if(cmd.contains("put")) {
                            stub.put(cmd.substring(3).trim(), Client.get(cmd.substring(3).trim()));
                            System.out.println("Le fichier a bien été envoyé !");
                        } else {
                            System.out.println("Entrez une commande valide...");
                        }
                        break;
                }
            } while(!cmd.equals("exit"));

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static byte[] get(String filenmae) {
        Path path = Paths.get(Client.path + filenmae);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void put(String filename, byte[] file) {
        Path path = Paths.get(Client.path + filename);
        try {
            Files.write(path, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
