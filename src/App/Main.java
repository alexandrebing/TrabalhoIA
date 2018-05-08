package App;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        //CRIA O MAPA - FALTA PEGAR O TXT DA ENTRADA E USAR COMO PARAMETRO PARA CONSTRUÇÃO DO MAPA, E MANDAR O RESTO PARA CRIAR AS PESSOAS.
        Scanner in = new Scanner (new File(args[0]));
        String offset = "   ";
        ArrayList<String> lines = new ArrayList();
        while(in.hasNextLine()){
            lines.add(in.nextLine());
        }
        String parts [] = lines.get(0).split(" ");
        int size = Integer.parseInt(parts[0]);
        int registries = Integer.parseInt(parts[1]);
        if(size <= 10) offset = "    ";
        else{
            offset = "   ";
        }
        lines.remove(0);
        Board b = new Board(size, registries, lines, offset);

        b.printBoard();

        b.movePopulation();

        b.printBoard();

        int n = 0;

//        REPETE AS ACOES DA POPULAÇÃO NO MAPA
//        while(n < 100) {
//            clearScreen();
//            b.printBoard();
//            b.movePopulation();//ESSE METODO IDEALIZEI COMO O CENTRAL, ONDE A POPULAÇÃO VAI SE MOVER, VERIFICAR SE HÁ PARCEIROS DISPONÍVEIS, INICIAR O STRING MATCHING, MUDAR O ESTADO DA PESSOA.
//        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
