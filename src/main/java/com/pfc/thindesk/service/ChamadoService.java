package com.pfc.thindesk.service;

import com.pfc.thindesk.entity.Chamado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.pfc.thindesk.repository.ChamadoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Chamado criarChamado(String titulo, String descricao, String solicitante) {
        Chamado chamado = new Chamado();
        chamado.setTitulo(titulo.trim());
        chamado.setDescricao(descricao.trim());
        chamado.setUsuario(solicitante);
        chamado.setDataAbertura(java.time.Instant.now());
        chamado.setStatus("ABERTO");
        return chamadoRepository.save(chamado);
    }

    public List<Chamado> listarChamados() {
        return chamadoRepository.findAll();
    }

    public List<Chamado> listarChamadosDoUsuario(String usuario, String solicitante) {
        verificarSolicitante(usuario, solicitante);
        return chamadoRepository.findByUsuario(usuario);
    }

    public Chamado consultarChamado(String id, String solicitante) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chamado nao encontrado"));
        verificarSolicitante(chamado.getUsuario(), solicitante);
        return chamado;
    }

    private void verificarSolicitante(String usuario, String solicitante) {
        boolean tecnico = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_TECNICO"));
        if (!tecnico && !solicitante.equals(usuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso permitido apenas ao solicitante");
        }
    }

    public Chamado atualizarChamado(String id, Chamado chamadoAtualizado) {
        consultarChamado(id, SecurityContextHolder.getContext().getAuthentication().getName());
        if (chamadoAtualizado.getStatus() != null && !"ABERTO".equals(chamadoAtualizado.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Use o endpoint de resolucao para resolver o chamado");
        }
        Update update = new Update().set("descricao", chamadoAtualizado.getDescricao())
                .set("tipo", chamadoAtualizado.getTipo());
        return atualizarAberto(id, update);
    }

    @PreAuthorize("hasRole('TECNICO')")
    public Chamado resolverChamado(String id, String solucao, String tecnico) {
        consultarChamado(id, tecnico);
        Update update = new Update().set("solucao", solucao.trim()).set("status", "RESOLVIDO")
                .set("dataResolucao", java.time.Instant.now()).set("tecnico", tecnico);
        return atualizarAberto(id, update);
    }

    private Chamado atualizarAberto(String id, Update update) {
        Query query = Query.query(Criteria.where("id").is(id).and("status").is("ABERTO"));
        Chamado chamado = mongoTemplate.findAndModify(query, update,
                FindAndModifyOptions.options().returnNew(true), Chamado.class);
        if (chamado == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chamado resolvido ou nao esta ABERTO");
        }
        return chamado;
    }

}
