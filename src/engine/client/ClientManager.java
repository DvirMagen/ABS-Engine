package engine.client;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ClientManager {
    private final Set<Client> usersSet;

    public ClientManager() {
        usersSet = new HashSet<>();
    }

    public synchronized void addClient(String username, boolean isAdmin) {
        Client c = new Client(username,isAdmin);
        usersSet.add(c);
    }

    public synchronized void removeClient(String username) {
        usersSet.removeIf(client -> client.getName().equalsIgnoreCase(username));
    }

    public synchronized Set<Client> getClients() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isClientExists(String username) {
        for (Client client : usersSet) {
            if(client.getName().equalsIgnoreCase(username))
                return true;
        }
        return false;
    }

    public boolean isAdminConnected(){
        for (Client client : usersSet) {
            if(client.isAdmin())
                return true;
        }
        return false;
    }
}
