package engine.client;

import java.util.Objects;

public class Client {
    private String name;
    private boolean isAdmin = false;

    public Client(String name, boolean isAdmin) {
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin1)
    {
        this.isAdmin = isAdmin1;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return isAdmin() == client.isAdmin() && Objects.equals(getName(), client.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), isAdmin());
    }
}
