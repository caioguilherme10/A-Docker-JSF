/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.a.docker.jsf;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author 
 */
public class ContatosEmJDBC implements Contatos {

    public ContatosEmJDBC() {
    }

    @Override
    public void create(Contato contato) {
        String sql = "INSERT INTO Contato (email, nome, senha, telefone, datanascimento) VALUES (?,?,?,?,?);";
        try(Connection connection = abrirConexao()) {
            PreparedStatement createStatement = connection.prepareStatement(sql);
            createStatement.setString(1, contato.getEmail());
            createStatement.setString(2, contato.getNome());
            createStatement.setString(3, contato.getSenha());
            createStatement.setString(4, contato.getTelefone());
            createStatement.setDate(5, Date.valueOf(contato.getDataNascimento()));
            createStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ContatosEmJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void remove(Contato contato) {
        String sql = "DELETE FROM Contato WHERE email=?";
        try (Connection connection = abrirConexao()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, contato.getEmail());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ContatosEmJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Contato contato) {
        String sql = "UPDATE Contato SET nome = ?, senha = ?, telefone = ?, datanascimento = ? WHERE email = ?";
        try (Connection connection = abrirConexao()){
            PreparedStatement createStatement = connection.prepareStatement(sql);
            createStatement.setString(1, contato.getNome());
            createStatement.setString(2, contato.getSenha());
            createStatement.setString(3, contato.getTelefone());
            createStatement.setDate(4, Date.valueOf(contato.getDataNascimento()));
            createStatement.setString(5, contato.getEmail());
            createStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ContatosEmJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Contato buscaPorNome(String nome) {
        String sql = "SELECT * FROM Contato WHERE nome = ?";
        try (Connection connection = abrirConexao()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nome);
            ResultSet result = statement.executeQuery();
            return criarNovoContato(result);
        } catch (SQLException ex) {
            Logger.getLogger(ContatosEmJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Contato> contatosOrdenados() {
        List<Contato> contatos = new ArrayList<>();
        String sql = "SELECT * FROM Contato ORDER BY nome ASC";
        try (Connection connection = abrirConexao()){
            PreparedStatement statement = connection.prepareStatement(sql);
            percorrerContatos(statement, contatos);
        } catch (SQLException ex) {
            Logger.getLogger(ContatosEmJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contatos;
    }

    @Override
    public Contato autentica(String email, String senha) {
        String sql = "SELECT * FROM Contato WHERE email = ? AND senha = ?";
        try (Connection connection = abrirConexao()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, senha);
            ResultSet result = statement.executeQuery();
            return criarNovoContato(result);
        } catch (SQLException ex) {
            Logger.getLogger(ContatosEmJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Contato> contatosAgrupados(String inicial) {
        PreparedStatement statement = null;

        try (Connection connection = abrirConexao()){
            String sql = "SELECT * FROM contato WHERE nome ILIKE ?;";
            statement = connection.prepareStatement(sql);
            statement.setString(1, inicial + "%");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {
            return criarContato(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Collections.emptyList();

    }
    
    private List<Contato> criarContato(PreparedStatement stmt) throws SQLException {
        List<Contato> contatos = new ArrayList<>();
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next())
            contatos.add(new Contato(
                    resultSet.getString("nome"),
                    resultSet.getString("email"),
                    resultSet.getString("senha"),
                    resultSet.getString("telefone"),
                    resultSet.getDate("dataNascimento").toLocalDate()
            ));
        
        return contatos;
    }

    private void percorrerContatos(PreparedStatement statement, List<Contato> contatos) throws SQLException {

        ResultSet result = statement.executeQuery();
        while (result.next()) {
            contatos.add(criarNovoContato(result));
        }
    }

    private Contato criarNovoContato(ResultSet result) throws SQLException{
        String email = result.getString("email");
        String nome = result.getString("nome");
        String senha = result.getString("senha");
        String telefone = result.getString("telefone");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate datanas = result.getDate("datanascimento").toLocalDate();
        LocalDate datanascimento = LocalDate.parse(datanas.toString(),formatter);

        return new Contato(nome, email, senha, telefone, datanascimento);
    }

    private Connection abrirConexao() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://host-banco:5432/a-docker-jsf",
                    "postgres", "0123");
            return connection;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ContatosEmJDBC.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
