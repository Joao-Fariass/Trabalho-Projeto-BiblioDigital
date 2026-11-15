package excecoes;


// Aqui é lançada pelo Service quando uma regra/etc não é "a"tendida".

public class LivroException extends Exception{
    public LivroException(String messsage) {
        super(messsage);
    }
}
