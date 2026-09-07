# Sprint 3 – Gestão de Manutenção de Rodovias (Java + JDBC + Oracle)

Sistema em Java que controla o crescimento de vegetação em trechos de rodovia,
aciona automaticamente equipes de manutenção quando o nível de vegetação
ultrapassa o limite definido, registra as intervenções realizadas e gera um
relatório de prioridades — persistido no banco de dados Oracle.

## Sumário

- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Pré-requisitos](#pré-requisitos)
- [1. Configurar o banco de dados](#1-configurar-o-banco-de-dados)
- [2. Configurar a conexão (ConexaoBanco.java)](#2-configurar-a-conexão-conexaobancojava)
- [3. Compilar o projeto](#3-compilar-o-projeto)
- [4. Executar o projeto](#4-executar-o-projeto)
- [O que o Main.java demonstra](#o-que-o-mainjava-demonstra)
- [Modelo de dados](#modelo-de-dados)
- [Problemas comuns](#problemas-comuns)

## Tecnologias utilizadas

- Java 17+ (uso de `record`, pattern matching de `instanceof`, text blocks)
- JDBC (`java.sql.*`)
- Banco de dados Oracle (testado com o Oracle da FIAP — `oracle.fiap.com.br`)
- Driver `ojdbc17.jar` (incluído em `lib/`)

## Estrutura do projeto

```
sprint3-java/
├── lib/
│   └── ojdbc17.jar              # driver JDBC do Oracle
├── src/
│   ├── db/
│   │   └── ConexaoBanco.java    # abre a conexão com o Oracle
│   ├── model/                   # entidades e regras de domínio
│   │   ├── TrechoRodovia.java
│   │   ├── TrechoComSensor.java
│   │   ├── EquipeManutencao.java
│   │   ├── IntervencaoOperacional.java (abstrata)
│   │   ├── RocadaMecanizada.java
│   │   ├── Pulverizacao.java
│   │   ├── EquipesRanking.java / TrechoRanking.java (records p/ relatório)
│   │   ├── MonitoravelViaIoT.java (interface)
│   │   └── ValidacoesTrechoRodovia.java
│   ├── dao/                     # acesso a dados (CRUD via JDBC)
│   │   ├── TrechoRodoviaDAO.java
│   │   ├── EquipeManutencaoDAO.java
│   │   ├── IntervencaoOperacionalDAO.java
│   │   └── RelatorioPrioridadeDAO.java
│   ├── service/
│   │   └── Relatorio.java       # monta e persiste o relatório de prioridades
│   ├── main/
│   │   ├── Main.java            # ponto de entrada da aplicação
│   │   ├── AtualizandoTrechos.java
│   │   └── AvaliacaoTrechos.java
│   └── scripts/
│       ├── criar-tabela.sql     # DDL de todas as tabelas
│       └── dados.sql            # massa de dados de teste
└── README.md
```

## Pré-requisitos

- JDK 17 ou superior instalado (`java -version` / `javac -version`)
- Acesso a um banco Oracle (host, porta, SID, usuário e senha)
- Cliente SQL para rodar os scripts (Oracle SQL Developer, DBeaver, etc.)

## 1. Configurar o banco de dados

Execute os scripts **nesta ordem**, usando seu cliente SQL preferido:

1. `src/scripts/criar-tabela.sql` — cria as tabelas:
   `trechos`, `equipesManutencao`, `intervencoesOperacionais`,
   `relatoriosPrioridade`, `rankingEquipes` e `rankingTrechos`.
2. `src/scripts/dados.sql` — insere os dados de teste iniciais
   (trechos de rodovia e equipes de manutenção).

> **Dica:** no Oracle SQL Developer, você pode colar o conteúdo dos dois
> arquivos (primeiro `criar-tabela.sql`, depois `dados.sql`) numa mesma aba
> de *Worksheet* em vez de abrir um script por vez. A execução normal
> (Ctrl+Enter / "Run Statement") roda só o comando onde o cursor está —
> para rodar **todos** os comandos colados de uma vez, use o **F5**
> ("Run Script").

## 2. Configurar a conexão (ConexaoBanco.java)

Abra `src/db/ConexaoBanco.java` e ajuste as constantes com **suas** credenciais
de acesso ao Oracle:

```java
private static final String HOST = "oracle.fiap.com.br";
private static final String PORT = "1521";
private static final String SID = "ORCL";
private static final String USER = "SEU_USUARIO";
private static final String PASSWORD = "SUA_SENHA";
```

> O projeto já vem com o `lib/ojdbc17.jar` no classpath — não é necessário
> baixar o driver separadamente.

## 3. Compilar o projeto

**Pelo terminal (Linux/Mac):**
```bash
mkdir -p bin
javac -cp "lib/ojdbc17.jar" -d bin $(find src -name "*.java")
```

**Pelo terminal (Windows / PowerShell):**
```powershell
mkdir bin
javac -cp "lib/ojdbc17.jar" -d bin (Get-ChildItem -Recurse -Filter *.java -Path src).FullName
```

**Pela IDE (IntelliJ IDEA):**
Abra a pasta do projeto normalmente — o módulo `untitled.iml` já referencia
`ojdbc17.jar` como biblioteca do projeto. Basta rodar `Main.java`.

## 4. Executar o projeto

**Linux/Mac:**
```bash
java -cp "bin:lib/ojdbc17.jar" main.Main
```

**Windows:**
```powershell
java -cp "bin;lib/ojdbc17.jar" main.Main
```

## O que o `Main.java` demonstra

1. **Atualização dos trechos** — simula o crescimento da vegetação em cada
   trecho (trechos com sensor via `TrechoComSensor.simularCrescimento()`,
   trechos comuns via `TrechoRodovia.registrarCrescimento()`) e persiste a
   atualização com `TrechoRodoviaDAO.atualizar()`.
2. **Listagem de trechos e equipes** — `listarTodos()` de cada DAO.
3. **Geração de intervenções** — para cada trecho cuja vegetação ultrapassa
   o limite (30 cm), sorteia uma equipe compatível com o clima do trecho e
   registra a intervenção (`RocadaMecanizada` para clima úmido,
   `Pulverizacao` para clima seco), salvando-a com
   `IntervencaoOperacionalDAO.inserir()`.
4. **Listagem das intervenções realizadas**.
5. **Exibição do relatório de prioridades** — contagens gerais e rankings
   de equipes/trechos com mais intervenções (`Relatorio.exibirRelatorio()`).
6. **Persistência do relatório** — `Relatorio.salvarRelatorio()` grava o
   relatório gerado e seus rankings nas tabelas `relatoriosPrioridade`,
   `rankingEquipes` e `rankingTrechos`, dentro de uma transação
   (`commit`/`rollback`).

## Modelo de dados

| Tabela | Descrição |
|---|---|
| `trechos` | Trechos de rodovia monitorados (com ou sem sensor) |
| `equipesManutencao` | Equipes disponíveis para roçada manual ou mecanizada |
| `intervencoesOperacionais` | Histórico de intervenções realizadas |
| `relatoriosPrioridade` | Um registro por execução do relatório (contagens gerais) |
| `rankingEquipes` | Ranking de equipes por nº de intervenções, vinculado a um relatório |
| `rankingTrechos` | Ranking de trechos por nº de intervenções, vinculado a um relatório |

## Problemas comuns

| Problema | Solução |
|---|---|
| `Driver not found` | Confirme que `lib/ojdbc17.jar` está no classpath de compilação e execução |
| `ORA-01017: invalid username/password` | Verifique as credenciais em `ConexaoBanco.java` |
| `ORA-00942: table or view does not exist` | Execute `criar-tabela.sql` antes de rodar o `Main` |
| `ORA-02292: integrity constraint violated` | Existem registros dependentes (FK); remova-os antes de excluir o pai |
| `Connection closed` / erro de conexão | Confira host, porta e SID; teste a conexão em um cliente SQL antes |

## Integrantes

| RM | Nome |
|---|---|
| 563415 | Fernando Caires Silva |
| 563567 | Raphael Mischiatti de Souza |
| 563500 | Guilherme Martins Rezende |
| 565434 | Giovanna Fernandes Pereira |
| 565323 | João Pedro de Moura Albino |
| 561675 | Kauê Silva Matheus |
