package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.utils.DataUtils.verificarDiaSemana;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class})
public class LocacaoServiceTest_PowerMock {
    @InjectMocks
    private LocacaoService locacaoService;

    @Mock
    private SPCService spc;

    @Mock
    private LocacaoDAO dao;

    @Mock
    private EmailService email;


    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        locacaoService = PowerMockito.spy(locacaoService);
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        Assume.assumeFalse(verificarDiaSemana(new Date(), Calendar.SATURDAY));
        //cenario

        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

//        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28,4,2017));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 28);
        calendar.set(Calendar.MONTH, Calendar.APRIL);
        calendar.set(Calendar.YEAR, 2017);
        PowerMockito.mockStatic(Calendar.class);
        PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);


        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
//        error.checkThat(locacao.getDataLocacao(), ehHoje());
//        error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDeDias(1));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

//        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(30,3,2024));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 30);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.YEAR, 2024);
        PowerMockito.mockStatic(Calendar.class);
        PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);


        //acao
        Locacao retorno = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(retorno.getDataRetorno(), caiNumaSegunda());
//        PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();

        PowerMockito.verifyStatic(Calendar.class, Mockito.times(2));
        Calendar.getInstance();
    }

    @Test
    public void deveAlugarFilme_SemCalcularValor() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        PowerMockito.doReturn(1.0).when(locacaoService, "calcularValorLocacao", filmes);

        //acao
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(locacao.getValor(), is(1.0));
        PowerMockito.verifyPrivate(locacaoService).invoke("calcularValorLocacao", filmes);

    }

    @Test
    public void deveCalcularValorLocacao() throws Exception {
        //cenario
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //acao
        Double valor = (Double) Whitebox.invokeMethod(locacaoService, "calcularValorLocacao", filmes);

        //verificacao
        Assert.assertThat(valor, is(4.0));

    }

}
