package repositorios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Codes.Livro;


public class LivroRepository {

    private final List<Livro> livros = new ArrayList<>();

    public void inserir(Livro livro) {
        livros.add(livro);
    }

    
    public List<Livro> listarTodos() {
        return new ArrayList<>(livros);
    }

    public int quantidade() {
        return livros.size();
    }

    public Livro buscarPorIndice(int indice) {
        return livros.get(indice);
    }

    public Optional<Livro> buscarPorTituloExato(String titulo) {
        for (Livro livro : livros) {
            if (livro.getTitulo().equalsIgnoreCase(titulo)) {
                return Optional.of(livro);
            }
        }
        return Optional.empty();
    }

    public List<Livro> buscarPorTitulo(String trecho) {
        List<Livro> encontrados = new ArrayList<>();
        for (Livro livro : livros) {
            if (contem(livro.getTitulo(), trecho)) {
                encontrados.add(livro);
            }
        }
        return encontrados;
    }

    public List<Livro> buscarPorAutor(String trecho) {
        List<Livro> encontrados = new ArrayList<>();
        for (Livro livro : livros) {
            if (contem(livro.getAutor(), trecho)) {
                encontrados.add(livro);
            }
        }
        return encontrados;
    }

    public void alterar(int indice, Livro livro) {
        livros.set(indice, livro);
    }

    public void excluir(int indice) {
        livros.remove(indice);
    }

    private boolean contem(String texto, String trecho) {
        return texto.toLowerCase().contains(trecho.toLowerCase());
    }
}
