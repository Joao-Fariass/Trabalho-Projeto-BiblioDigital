package excecoes;


// Aqui é lançada pelo Service quando uma regra/etc não é "atendida".

public class LivroException extends Exception{
    public LivroException(String messsage) {
        super(messsage);
    }
}
