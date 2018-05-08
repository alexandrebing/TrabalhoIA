import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        //CRIA O MAPA - FALTA PEGAR O TXT DA ENTRADA E USAR COMO PARAMETRO PARA CONSTRUÇÃO DO MAPA, E MANDAR O RESTO PARA CRIAR AS PESSOAS.
        Scanner in = new Scanner (new File("30Casais.txt"));
        String offset = "   ";
        ArrayList<String> lines = new ArrayList<>();
        System.out.println("Simulação:\n 1 - 10 casais\n 2 - 30 casais\n 3 - 50 casais");
        while(in.hasNextLine()){
            lines.add(in.nextLine());
        }
        String parts [] = lines.get(0).split(" ");
        int size = Integer.parseInt(parts[0]);
        int registries = Integer.parseInt(parts[1]);
        if(size == 10) offset = "    ";
        else{
            offset = "   ";
        }
        lines.remove(0);
        
        Board b = new Board(size, registries, lines, offset);
        b.printBoard();
        int n = 0;

        //REPETE AS ACOES DA POPULAÇÃO NO MAPA
        while(n < 100){
            //clearScreen();
            //s.next();
            b.printBoard();
            b.movePopulation();//ESSE METODO IDEALIZEI COMO O CENTRAL, ONDE A POPULAÇÃO VAI SE MOVER, VERIFICAR SE HÁ PARCEIROS DISPONÍVEIS, INICIAR O STRING MATCHING, MUDAR O ESTADO DA PESSOA.
            try {

                Thread.sleep(1000);

            }
            catch (InterruptedException e){

                Thread.currentThread().interrupt();

            }
            n++;
        }



    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
