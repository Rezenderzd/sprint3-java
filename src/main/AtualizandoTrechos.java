package main;

import dao.TrechoRodoviaDAO;
import model.TrechoComSensor;
import model.TrechoRodovia;
import java.util.List;
import java.util.Random;

public class AtualizandoTrechos {

    public void atualizandoTrechosBanco(TrechoRodoviaDAO trechoDao) {
        System.out.println("Atualizando crescimento no banco de dados...");
        Random random = new Random();

        List<TrechoRodovia> trechos = trechoDao.buscarTodosEInstanciar();

        for (TrechoRodovia trecho : trechos) {
            if (trecho instanceof TrechoComSensor sensor) {
                sensor.simularCrescimento();
            } else {
                trecho.registrarCrescimento(random.nextInt(1,15));
            }
            trechoDao.atualizar(trecho);
        }
    }
}