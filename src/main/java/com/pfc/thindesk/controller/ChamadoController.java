package com.pfc.thindesk.controller;

import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.dto.AberturaChamadoRequest;
import com.pfc.thindesk.dto.ResolucaoChamadoRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.security.Principal;
import com.pfc.thindesk.service.ChamadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    @Autowired
    private ChamadoService chamadoService;

    @PostMapping
    public ResponseEntity<Chamado> criarChamado(@Valid @RequestBody AberturaChamadoRequest request, Principal principal) {
        Chamado chamado = chamadoService.criarChamado(request.titulo(), request.descricao(), principal.getName());
        return ResponseEntity.created(URI.create("/api/chamados/" + chamado.getId())).body(chamado);
    }

    @GetMapping
    public List<Chamado> listarChamados() {
        return chamadoService.listarChamados();
    }

    @GetMapping("/usuario/{id}")
    public List<Chamado> listarChamadosDoUsuario(@PathVariable("id") String id, Principal principal) {
        return chamadoService.listarChamadosDoUsuario(id, principal.getName());
    }

    @GetMapping("/{id}")
    public Chamado consultarChamado(@PathVariable("id") String id, Principal principal) {
        return chamadoService.consultarChamado(id, principal.getName());
    }

    @PutMapping("/{id}/resolver")
    public Chamado resolverChamado(@PathVariable("id") String id,
            @Valid @RequestBody ResolucaoChamadoRequest request, Principal principal) {
        return chamadoService.resolverChamado(id, request.solucao(), principal.getName());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chamado> atualizarChamado(@PathVariable("id") String id, @RequestBody Chamado chamadoAtualizado) {
        Chamado chamado = chamadoService.atualizarChamado(id, chamadoAtualizado);
        return ResponseEntity.ok(chamado);
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarChamado(@PathVariable("id") String id, @ModelAttribute Chamado chamadoAtualizado, Model model) {
        chamadoService.atualizarChamado(id, chamadoAtualizado);
        return "redirect:/chamados"; // Redireciona para a página da lista de chamados
    }
}
