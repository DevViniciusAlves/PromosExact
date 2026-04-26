# 🎯 PromosExact

> Plataforma backend para automacao de promocoes, ingestao de produtos e publicacao de ofertas com integracoes externas e regras de negocio centralizadas.

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![License](https://img.shields.io/badge/License-Private-lightgrey?style=for-the-badge)

## ✨ Destaques

- 🚀 **Fluxo automatizado** - recebe links, extrai dados e publica promocoes
- 🔐 **Seguranca reforcada** - segredos fora do codigo e rotas protegidas
- 📦 **Ingestao de produtos** - processamento de produtos e integracoes
- 🧠 **Regras centralizadas** - validacoes e normalizacao no backend
- 📲 **Telegram integrado** - envio de promocao com mensagem formatada
- 🗄️ **PostgreSQL + JPA** - persistencia organizada e modelagem clara
- ⚡ **Base preparada para evolucao** - estrutura pronta para novas fontes

## 🚀 Quick Start

### Requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL 14+

## 📋 Funcionalidades Principais

### 📦 Produtos

- Cadastro e consulta de produtos
- Ingestao via integracao externa
- Normalizacao de nome, descricao, imagem e preco

### 💸 Promoções

- Criacao de promocao a partir de produto salvo
- Processamento de links em lote
- Publicacao automatica no Telegram

### 🔐 Segurança

- Segredos fora do código
- Rotas sensiveis protegidas
- Validacao de entrada nos endpoints principais
- Rate limit basico nos pontos mais caros

### 🧾 Auditoria

- Registro de notificacoes
- Controle de envio
- Resposta de erro mais limpa para o cliente

## 🏗️ Arquitetura

```text
src/main/java/com/exactpromos/
├── config/
├── controller/
├── dto/
├── entity/
├── mapper/
├── repository/
└── service/
```

```text
src/main/resources/
├── application.properties
└── application-local.properties
```

## 🔌 Endpoints principais

- `GET /produtos`
- `POST /produtos`
- `POST /promocoes/links`
- `POST /promocoes/lote`
- `POST /telegram/teste`
- `GET /usuarios`

## 🛡️ Segurança e Operação

- `ddl-auto=validate`
- `show-sql=false`
- `spring.h2.console.enabled=false`
- credenciais via variáveis de ambiente
- escape de HTML nas mensagens do Telegram
- validação de payload com `@Valid`

## 📌 Observações

- O projeto está focado no fluxo de Mercado Livre neste momento.
- A estrutura já está pronta para reativar outras fontes depois.
- O README prioriza o estado atual da base, não uma promessa de roadmap.

## 📄 Licença

Uso interno ou conforme definido pelo proprietário do repositório.
