package visoes;


public class Input {

    public static int scanInt(String message){
        while (true) {
            try{
                return Integer.parseInt(IO.readln(message).trim());
            }   catch (NumberFormatException e){
                IO.println("Entrada Inválida. Por favor, digite um número inteiro.");
            }
        }
    }

    public static double scanDouble (String message){
        while (true) {
            try{
                return Double.parseDouble(IO.readln(message).trim().replace(',', '.'));
            } catch (NumberFormatException e){
                IO.println("Entrada Inválida. Por favor, digite um número.");
            }
            
        }
    }
}