package info705.tp3.ftp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Server implements Ftp {
    public String currentDir;

    public Server() {
        currentDir = "D:/";
    }

    public static void main(String args[]) {
        try {
            Server obj = new Server();
            // Declare le stub sur lequel sera expose l’objet distribue
            Ftp stub = (Ftp) UnicastRemoteObject.exportObject(obj, 0);
            // Permet de lier au Registre l’objet distribue
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("Ftp", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public byte[] get(String filenmae) {
        Path path = Paths.get(this.currentDir + "/" + filenmae);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void put(String filename, byte[] file) {
        Path path = Paths.get(this.currentDir + "/" + filename);
        try {
            Files.write(path, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cd(String dir) {
        String newDir = currentDir;
        if(dir.equals("..")) {
            int lastIndex = currentDir.lastIndexOf("/");
            if(lastIndex != -1) {
                newDir = currentDir.substring(0, lastIndex);
            }
        } else
            newDir += "/" + dir;
        if(Files.exists(Paths.get(newDir)))
            currentDir = newDir;
    }

    @Override
    public List<String> ls() {
        List<String> fileNames = new ArrayList<>();
        File folder = new File(currentDir);
        File[] files = folder.listFiles();
        if(files == null) return null;
        for(File f : files) {
            fileNames.add(f.getName());
        }
        return fileNames;
    }

    @Override
    public String pwd() {
        return currentDir;
    }
}