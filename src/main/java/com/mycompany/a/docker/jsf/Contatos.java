/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.a.docker.jsf;

import java.util.List;

/**
 *
 * @author 
 */
public interface Contatos {

    void create(Contato contato);
    void remove(Contato contato);
    void update(Contato contato);
    Contato buscaPorNome(String nome);
    List<Contato> contatosOrdenados();
    Contato autentica(String email , String senha);
    List<Contato> contatosAgrupados(String inicial);

}
