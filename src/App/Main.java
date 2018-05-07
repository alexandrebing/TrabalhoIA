package App;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //CRIA O MAPA - FALTA PEGAR O TXT DA ENTRADA E USAR COMO PARAMETRO PARA CONSTRUÇÃO DO MAPA, E MANDAR O RESTO PARA CRIAR AS PESSOAS.
        Scanner s = new Scanner(System.in);
        Board b = new Board(30,40, 20, 5 );
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
