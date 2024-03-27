package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class CalculadoraMockTest {

    @Mock
    private Calculadora calcMock;

    @Spy
    private Calculadora calcSpy;

    @Mock
    private EmailService email;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void devoMostrarDiferencaEntreMockSpy(){
        Mockito.when(calcMock.somar(1,2)).thenReturn(5);
        Mockito.when(calcSpy.somar(1,2)).thenReturn(5);
        Mockito.doNothing().when(calcSpy).imprime();

        System.out.println("Mock " + calcMock.somar(1, 2));
        System.out.println("Spy " + calcSpy.somar(1, 2));

        System.out.println("Mock");
        calcMock.imprime();
        System.out.println("Spy");
        calcSpy.imprime();

    }

    @Test
    public void teste(){
        Calculadora calculadora = Mockito.mock(Calculadora.class);
        Mockito.when(calculadora.somar(Mockito.eq(1),Mockito.anyInt())).thenReturn(5);

        Assert.assertEquals(5, calculadora.somar(1, 10000));
    }

}
