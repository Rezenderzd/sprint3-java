CREATE TABLE intervencoesOperacionais (
id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
nome VARCHAR2(100) NOT NULL,
quilometroInicial NUMBER NOT NULL,
quilometroFinal NUMBER NOT NULL,
tipoClima VARCHAR2(100) NOT NULL,
nomeEquipe VARCHAR2(100) NOT NULL
);

CREATE TABLE equipesManutencao (
id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
nomeEquipe VARCHAR2(100) NOT NULL,
quantidadeFuncionarios NUMBER NOT NULL,
rocadaDeAtuacao VARCHAR2(100) NOT NULL
);

CREATE TABLE trechos (
id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
nome VARCHAR2(100) NOT NULL,
quilometroInicial NUMBER NOT NULL,
quilometroFinal NUMBER NOT NULL,
nivelVegetacaoEmCm NUMBER NOT NULL,
tipoClima VARCHAR2(100) NOT NULL,
trechoComSenor NUMBER(1) NOT NULL
);

CREATE TABLE relatoriosPrioridade (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    dataGeracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    totalEquipes NUMBER,
    totalTrechos NUMBER,
    trechosComSensor NUMBER,
    trechosSemSensor NUMBER
);

CREATE TABLE rankingEquipes (
                                id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                relatorioId NUMBER NOT NULL,
                                nomeEquipe VARCHAR2(100),
                                totalIntervencoes NUMBER,

                                CONSTRAINT fk_ranking_equipes_relatorio
                                    FOREIGN KEY (relatorioId)
                                        REFERENCES relatoriosPrioridade(id)
);

CREATE TABLE rankingTrechos (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    relatorioId NUMBER NOT NULL,
    nome VARCHAR2(100),
    quilometroInicial NUMBER,
    quilometroFinal NUMBER,
    totalIntervencoes NUMBER,

    CONSTRAINT fk_ranking_trechos_relatorio
        FOREIGN KEY (relatorioId)
        REFERENCES relatoriosPrioridade(id)
);