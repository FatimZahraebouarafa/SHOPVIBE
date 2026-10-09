package com.ecommerce.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    @Column(nullable = false, unique = true, length = 50)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column
    private Integer connectionNumber = 0;

    @Column(nullable = false)
    private String role = "ROLE_USER";

    // Un User peut avoir plusieurs commandes
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Command> commands;

    // Constructeurs
    public User() {}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.connectionNumber = 0;
    }

    // Getters & Setters
    public Integer getIdUser() { return idUser; }
    public void setIdUser(Integer idUser) { this.idUser = idUser; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getConnectionNumber() { return connectionNumber; }
    public void setConnectionNumber(Integer connectionNumber) { this.connectionNumber = connectionNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<Command> getCommands() { return commands; }
    public void setCommands(List<Command> commands) { this.commands = commands; }
}
