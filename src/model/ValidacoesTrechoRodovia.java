package model;

public class ValidacoesTrechoRodovia {

    public boolean validarString(String string){
        if(string.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    public boolean validarQuilometros(int kmInicial, int kmFinal){
        if(kmFinal<kmInicial || kmInicial<0 || kmFinal<1 ){
            return false;
        }else{
            return true;
        }
    }

    public boolean validarNivelVegetacao(double nivelVegetacao){
        if(nivelVegetacao<0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean validacaoGeral(String nomeTrecho, int kmInicial, int kmFinal, double nivelVegetacao, String tipoClima){
        if(!validarString(nomeTrecho)|| !validarString(tipoClima)|| !validarQuilometros(kmInicial, kmFinal)|| !validarNivelVegetacao(nivelVegetacao)){
            return false;
        }else{
            return true;
        }
    }
}
