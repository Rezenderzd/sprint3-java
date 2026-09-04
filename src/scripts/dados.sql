INSERT INTO trechos (
nome,
quilometroInicial,
quilometroFinal,
nivelVegetacaoEmCm,
tipoClima,
trechoComSenor
) VALUES (
'Br',
10,
15,
20.0,
'umido',
0
);

INSERT INTO trechos (
nome,
quilometroInicial,
quilometroFinal,
nivelVegetacaoEmCm,
tipoClima,
trechoComSenor
) VALUES (
'Rodo Anel',
20,
30,
19.0,
'seco',
0
);

INSERT INTO trechos (
nome,
quilometroInicial,
quilometroFinal,
nivelVegetacaoEmCm,
tipoClima,
trechoComSenor
) VALUES (
'Motiva Sorocabana',
15,
20,
18.0,
'umido',
1
);

INSERT INTO trechos (
nome,
quilometroInicial,
quilometroFinal,
nivelVegetacaoEmCm,
tipoClima,
trechoComSenor
) VALUES (
'Rodovia Presidente Dutra',
30,
40,
22.0,
'seco',
1
);

INSERT INTO equipesManutencao (
nomeEquipe,
quantidadeFuncionarios,
rocadaDeAtuacao
) VALUES (
'Equipe Alpha',
5,
'manual'
);

INSERT INTO equipesManutencao (
nomeEquipe,
quantidadeFuncionarios,
rocadaDeAtuacao
) VALUES (
'Equipe Teta',
7,
'mecanizada'
);

INSERT INTO equipesManutencao (
nomeEquipe,
quantidadeFuncionarios,
rocadaDeAtuacao
) VALUES (
'Equipe Gama',
8,
'manual'
);

INSERT INTO equipesManutencao (
nomeEquipe,
quantidadeFuncionarios,
rocadaDeAtuacao
) VALUES (
'Equipe Beta',
7,
'mecanizada'
);

COMMIT;
