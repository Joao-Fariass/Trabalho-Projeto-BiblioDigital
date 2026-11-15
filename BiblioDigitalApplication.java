import java.util.List;

import Codes.AudioLivro;
import Codes.Livro;
import Codes.LivroDigital;
import Codes.LivroFisico;
import excecoes.LivroException;
import repositorios.LivroRepository;
import servicos.LivroService;
import visoes.Input;

private final LivroService service = new LivroService(new LivroRepository());

void main() {
    IO.println("===== Bem vindo ao BiblioDigital =====");
    int opcao;
    do {
        mostrarMenu();
        opcao = Input.scanInt("Escolha uma das opções acima: ");
        switch (opcao) {
            case 1 -> cadastrar();
            case 2 -> pesquisarPorTitulo();
            case 3 -> pesquisarPorAutor();
            case 4 -> alterar();
            case 5 -> excluir();
            case 0 -> IO.println("Até logo!!!");
            default -> IO.println("Opção inválida! Tente novamente.");
        }
        if (opcao != 0) {
            IO.readln("Pressione Enter para continuar...");
        }
    } while (opcao != 0);
}

void mostrarMenu() {
    IO.println("""

            Menu:

            1 - Cadastrar novo Livro
            2 - Pesquisar Livro por título
            3 - Pesquisar Livro por autor
            4 - Alterar dados do Livro
            5 - Excluir Livro
            0 - Sair
            """);
}

void cadastrar() {
    int tipo = Input.scanInt("""
            Qual tipo de livro deseja cadastrar?
            1 - Livro físico
            2 - Livro digital
            3 - Audiolivro

            Escolha uma opção: """);
    if (tipo < 1 || tipo > 3) {
        IO.println("Opção de tipo de livro inválida.");
        return;
    }
    Livro novoLivro = lerDadosLivro(tipo);
    try {
        service.cadastrar(novoLivro);
        IO.println("Livro cadastrado com sucesso!");
    } catch (LivroException e) {
        IO.println("Erro: " + e.getMessage());
    }
}

void pesquisarPorTitulo() {
    String titulo = IO.readln("Digite o título (ou parte dele): ");
    try {
        exibirResultados(service.pesquisarPorTitulo(titulo));
    } catch (LivroException e) {
        IO.println("Erro: " + e.getMessage());
    }
}

void pesquisarPorAutor() {
    String autor = IO.readln("Digite o autor (ou parte do nome): ");
    try {
        exibirResultados(service.pesquisarPorAutor(autor));
    } catch (LivroException e) {
        IO.println("Erro: " + e.getMessage());
    }
}

void alterar() {
    List<Livro> livros = service.listar();
    if (livros.isEmpty()) {
        IO.println("O acervo está vazio.");
        return;
    }
    listarComIndice(livros);
    int indice = Input.scanInt("Informe o índice do livro que deseja alterar: ");
    try {
        Livro atual = service.buscarPorIndice(indice - 1);
        IO.println("Dados atuais: " + atual);
        IO.println("Informe os novos dados:");
        Livro alterado = lerDadosLivro(obterTipo(atual));
        service.alterar(indice - 1, alterado);
        IO.println("Livro alterado com sucesso!");
    } catch (LivroException e) {
        IO.println("Erro: " + e.getMessage());
    }
}

void excluir() {
    List<Livro> livros = service.listar();
    if (livros.isEmpty()) {
        IO.println("O acervo está vazio.");
        return;
    }
    listarComIndice(livros);
    int indice = Input.scanInt("Informe o índice do livro que deseja excluir: ");
    try {
        Livro livro = service.buscarPorIndice(indice - 1);
        String resposta = IO.readln("Confirma a exclusão de \"%s\"? (S/N): ".formatted(livro.getTitulo()));
        if (resposta.trim().equalsIgnoreCase("S")) {
            service.excluir(indice - 1);
            IO.println("Livro excluído com sucesso!");
        } else {
            IO.println("Exclusão cancelada.");
        }
    } catch (LivroException e) {
        IO.println("Erro: " + e.getMessage());
    }
}

Livro lerDadosLivro(int tipo) {
    String titulo = IO.readln("Digite o título do livro: ");
    String autor = IO.readln("Digite o autor do livro: ");
    int anoPublicacao = Input.scanInt("Digite o ano de publicação do livro: ");
    if (tipo == 1) {
        int quantidadeExemplares = Input.scanInt("Digite o número de exemplares: ");
        String localizacao = IO.readln("Digite a localização: ");
        return new LivroFisico(titulo, autor, anoPublicacao, quantidadeExemplares, localizacao);
    } else if (tipo == 2) {
        String formato = IO.readln("Digite o formato do arquivo: ");
        double tamanhoArquivo = Input.scanDouble("Digite o tamanho do arquivo (MB): ");
        return new LivroDigital(titulo, autor, anoPublicacao, formato, tamanhoArquivo);
    } else {
        int duracao = Input.scanInt("Digite a duração do audiolivro (minutos): ");
        String narrador = IO.readln("Digite o nome do narrador: ");
        return new AudioLivro(titulo, autor, anoPublicacao, duracao, narrador);
    }
}

int obterTipo(Livro livro) {
    if (livro instanceof LivroFisico) {
        return 1;
    } else if (livro instanceof LivroDigital) {
        return 2;
    }
    return 3;
}

void listarComIndice(List<Livro> livros) {
    IO.println("===== Livros cadastrados =====");
    for (int i = 0; i < livros.size(); i++) {
        IO.println("%d - %s".formatted(i + 1, livros.get(i).getTitulo()));
    }
}

void exibirResultados(List<Livro> encontrados) {
    if (encontrados.isEmpty()) {
        IO.println("Não existem resultados para a pesquisa realizada.");
        return;
    }
    IO.println("===== Resultado da pesquisa =====");
    for (Livro livro : encontrados) {
        IO.println("[%s] %s".formatted(livro.getTipo(), livro));
    }
}
