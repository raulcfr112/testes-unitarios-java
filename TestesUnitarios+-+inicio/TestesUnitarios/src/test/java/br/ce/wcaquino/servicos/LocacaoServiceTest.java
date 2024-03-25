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
import static org.junit.Assert.*;

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
    public void deveAlugarFilme() throws Exception {
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
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Raul");
        List<Filme> filmes = Arrays.asList(new Filme("Matrix", 0, 5.0));

        //acao
        locacaoService.alugarFilme(usuario, filmes);

    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
        //cenario

        LocacaoService locacaoService = new LocacaoService();
        List<Filme> filmes = Arrays.asList(new Filme("Matrix", 1, 5.0));


        //acao
        try {
            locacaoService.alugarFilme(null, filmes);
            fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }
    }

    @Test
    public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = new Usuario("Raul");
        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("F vazio");

        //acao

        locacaoService.alugarFilme(usuario, null);

    }

    @Test
    public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0));
        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(11.0));

    }

    @Test
    public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0));
        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(13.0));

    }

    @Test
    public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2,
                4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0),
                new Filme("Filme 5", 2, 4.0));
        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(14.0));

    }
    @Test
    public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 2,
                4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0),
                new Filme("Filme 5", 2, 4.0),
                new Filme("Filme 6", 2, 4.0));
        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(14.0));

    }
}
