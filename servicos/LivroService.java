package servicos;

import java.util.List;
import java.util.Optional;

import Codes.AudioLivro;
import Codes.Livro;
import Codes.LivroDigital;
import Codes.LivroFisico;
import excecoes.LivroException;
import repositorios.LivroRepository;


public class LivroService {

    private static final int ANO_PUBLICACAO_MINIMO = 1900;

    private final LivroRepository repository;

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    public void cadastrar(Livro livro) throws LivroException {
        validarENormalizar(livro, null);
        repository.inserir(livro);
    }

    public List<Livro> listar() {
        return repository.listarTodos();
    }

    public List<Livro> pesquisarPorTitulo(String titulo) throws LivroException {
        return repository.buscarPorTitulo(validarTermoPesquisa(titulo));
    }

    public List<Livro> pesquisarPorAutor(String autor) throws LivroException {
        return repository.buscarPorAutor(validarTermoPesquisa(autor));
    }

    public Livro buscarPorIndice(int indice) throws LivroException {
        validarIndice(indice);
        return repository.buscarPorIndice(indice);
    }

    public void alterar(int indice, Livro livroAlterado) throws LivroException {
        validarIndice(indice);
        Livro livroAtual = repository.buscarPorIndice(indice);
        
        validarENormalizar(livroAlterado, livroAtual);
        repository.alterar(indice, livroAlterado);
    }

    public void excluir(int indice) throws LivroException {
        validarIndice(indice);
        repository.excluir(indice);
    }

    
    private void validarENormalizar(Livro livro, Livro livroIgnorado) throws LivroException {
        if (livro == null) {
            throw new LivroException("Livro inválido.");
        }
        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new LivroException("O título não pode ser vazio.");
        }
        if (livro.getAutor() == null || livro.getAutor().isBlank()) {
            throw new LivroException("O autor não pode ser vazio.");
        }
        if (livro.getAnoPublicacao() < ANO_PUBLICACAO_MINIMO) {
            throw new LivroException("O ano de publicação deve ser igual ou superior a %d."
                    .formatted(ANO_PUBLICACAO_MINIMO));
        }

        livro.setTitulo(livro.getTitulo().trim());
        livro.setAutor(livro.getAutor().trim());
        normalizarCamposEspecificos(livro);

        Optional<Livro> existente = repository.buscarPorTituloExato(livro.getTitulo());
        if (existente.isPresent() && existente.get() != livroIgnorado) {
            throw new LivroException("Já existe um livro cadastrado com esse título.");
        }
    }

    
    private void normalizarCamposEspecificos(Livro livro) {
        if (livro instanceof LivroFisico) {
            LivroFisico fisico = (LivroFisico) livro;
            fisico.setLocalizacao(fisico.getLocalizacao().trim());
        } else if (livro instanceof LivroDigital) {
            LivroDigital digital = (LivroDigital) livro;
            digital.setFormato(digital.getFormato().trim());
        } else if (livro instanceof AudioLivro) {
            AudioLivro audio = (AudioLivro) livro;
            audio.setNarrador(audio.getNarrador().trim());
        }
    }

    private String validarTermoPesquisa(String termo) throws LivroException {
        if (termo == null || termo.isBlank()) {
            throw new LivroException("Informe um texto para realizar a pesquisa.");
        }
        return termo.trim();
    }

    private void validarIndice(int indice) throws LivroException {
        if (repository.quantidade() == 0) {
            throw new LivroException("O acervo está vazio.");
        }
        if (indice < 0 || indice >= repository.quantidade()) {
            throw new LivroException("Índice inválido.");
        }
    }
}
