/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.a.docker.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author 
 */
@Named
@SessionScoped
public class ControladorContatos implements Serializable {

    private Contatos contatos;
    private Contato contato;
    private Contato contatologado;
    private String email;
    private String senha;
    private String busca;
    private String inicial;
    private List<Contato> listcontatos;
    private List<Contato> listcontatosLetra;
    
    @PostConstruct
    public void initObjects() {
        this.contatos = new ContatosEmJDBC();
        this.contato = new Contato();
        this.contatologado = new Contato();
        this.listcontatos = new ArrayList<>();
        this.listcontatosLetra = new ArrayList<>();
        this.email = "";
        this.senha = "";
        this.busca = "";
        this.inicial = "";
    }
    
    public String salvar() {
        this.contatos.create(this.contato);
        this.contato = new Contato();
        return "index.xhtml";
    }
    
    public String atualizar() {
        this.contatos.update(contato);
        this.contato = new Contato();
        return "principal.xhtml";
    }
    
    public String remover(){
        this.contatos.remove(contatologado);
        this.contatologado = new Contato();
        return "index.xhtml";
    }
    
    public List<Contato> listar(){
        return this.contatos.contatosOrdenados();
    }
    
    public List<Contato> listarPorNome() {
        this.listcontatos.add(this.contatos.buscaPorNome(busca));
        return this.listcontatos;
    }

    public List<Contato> listarPorIncial() {
       this.listcontatosLetra = this.contatos.contatosAgrupados(inicial);
       return this.listcontatosLetra;
    }
    
    public String logar(){
        contatologado = this.contatos.autentica(email, senha);
        this.email = "";
        this.senha = "";
        if(contatologado.getEmail().equals(email)){
            return "principal.xhtml";
        }else{
            return "index.xhtml";
        }
    }
    
    public String logoff(){
        this.contatologado = new Contato();
        return "index.xhtml";
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public Contato getContatologado() {
        return contatologado;
    }

    public void setContatologado(Contato contatologado) {
        this.contatologado = contatologado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
}
