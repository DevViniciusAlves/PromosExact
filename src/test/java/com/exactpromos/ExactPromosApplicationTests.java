package com.exactpromos;

import com.exactpromos.Enum.PerfilEnum;
import com.exactpromos.entity.Usuario;
import com.exactpromos.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExactPromosApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void shouldAllowCreatingUserWithoutAuthentication() throws Exception {
		mockMvc.perform(post("/usuarios")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "telegramId": "999999",
								  "nome": "Teste Usuario",
								  "nomeDeUsuario": "teste.usuario",
								  "perfil": "TECH"
								}
								"""))
				.andExpect(status().isOk());
	}

	@Test
	void shouldAllowCreatingProductWithoutAuthentication() throws Exception {
		mockMvc.perform(post("/produtos")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "produtoId": "MANUAL-001",
								  "plataforma": "SHOPEE",
								  "nome": "Monitor Gamer 27 Polegadas",
								  "descricao": "Monitor gamer full hd com 165hz e painel ips",
								  "urlImagem": "https://example.com/monitor.jpg",
								  "categoria": "Monitores",
								  "marca": "AOC",
								  "precoAtual": 1299.90,
								  "precoOriginal": 1499.90,
								  "emEstoque": true
								}
								"""))
				.andExpect(status().isOk());
	}

	@Test
	void shouldAllowUpdatingUserWithoutAuthentication() throws Exception {
		Usuario usuario = new Usuario();
		usuario.setTelegramId("888888");
		usuario.setNome("Usuario Original");
		usuario.setNomeDeUsuario("usuario.original");
		usuario.setPerfil(PerfilEnum.TECH);
		usuario.setPremium(false);
		usuario.setSenha("sem-senha");
		usuario = usuarioRepository.save(usuario);

		mockMvc.perform(patch("/usuarios/atualizar/{id}", usuario.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "nome": "Usuario Atualizado",
								  "perfil": "GAMER"
								}
								"""))
				.andExpect(status().isOk());
	}

}
