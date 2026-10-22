package com.pfc.thindesk.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "chamados")
public class Chamado{
    
    @Id
    private String id;
    private String titulo;
    private java.time.Instant dataAbertura;
    private String solucao;
    private java.time.Instant dataResolucao;

    public String getSolucao() {
        return solucao;
    }

    public void setSolucao(String solucao) {
        this.solucao = solucao;
    }

    public java.time.Instant getDataResolucao() {
        return dataResolucao;
    }

    public void setDataResolucao(java.time.Instant dataResolucao) {
        this.dataResolucao = dataResolucao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public java.time.Instant getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(java.time.Instant dataAbertura) {
        this.dataAbertura = dataAbertura;
    }
    private String descricao;
    private String status;
    private String tipo;
    private String tecnico;
    private String usuario;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    
    
}
