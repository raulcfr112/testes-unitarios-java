package br.ce.wcaquino.servicos;

import exceptions.NaoPodeDividirPorZeroException;

public class Calculadora {
    public int somar(int a, int b) {
        System.out.println("Estou executando o metodo somar");
        return a + b;
    }

    public int subtrair(int a, int b) {
        return a - b;
    }

    public void imprime(){
        System.out.println("Passei aqui");
    }

    public int divide(int a, int b) throws NaoPodeDividirPorZeroException {
        if (b == 0){
            throw new NaoPodeDividirPorZeroException();
        }
        return a / b;
    }
}
