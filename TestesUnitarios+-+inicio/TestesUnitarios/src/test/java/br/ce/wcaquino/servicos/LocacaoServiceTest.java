package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import exceptions.FilmeSemEstoqueException;
import exceptions.LocadoraException;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.*;

public class LocacaoServiceTest {

    private LocacaoService locacaoService;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup(){
        locacaoService = new LocacaoService();
    }

    @Test
    public void testeLocacao() throws Exception {
        //cenario

        Usuario usuario = new Usuario("Raul");
        List<Filme> filmes = Arrays.asList(new Filme("Matrix", 1, 5.0));




        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void testLocacao_filmeSemEstoque() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Raul");
        List<Filme> filmes = Arrays.asList(new Filme("Matrix", 0, 5.0));

        //acao
        locacaoService.alugarFilme(usuario, filmes);

    }

    @Test
    public void testeLocacacao_usuarioVazio() throws FilmeSemEstoqueException {
        //cenario

        LocacaoService locacaoService = new LocacaoService();
        List<Filme> filmes = Arrays.asList(new Filme("Matrix", 1, 5.0));


        //acao
        try {
            locacaoService.alugarFilme(null, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuario vazio"));
        }
    }

    @Test
    public void testLocacao_FilmeVazio() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = new Usuario("Raul");
        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("F vazio");

        //acao

        locacaoService.alugarFilme(usuario, null);


    }
}
