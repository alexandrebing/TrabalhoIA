package App;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class Board {

    private int line, column;
    private String coordinates[][];
    private ArrayList <Person> men = new ArrayList<>();
    private ArrayList <Person> women = new ArrayList<>();
    private ArrayList <Couple> couples = new ArrayList<>();
    private StringMatching s = new StringMatching();
    private int couplesN;


    //CRIA MAPA   (CAMPO + BARREIRAS + POPULAÇÃO)
    public Board (int line, int column, int couplesN, int registries){
        this.line = line;
        this.column = column;
        this.couplesN = couplesN;
        coordinates = mountEmptyBoard(line, column);
        insertRegistries(registries);
        generatePopulation(couplesN);
        generatePrioritiesList();
    }

    //MONTA MAPA INICIAL SEM ESTRUTURAS
    private String[][] mountEmptyBoard(int width, int height) {
        coordinates = new String[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                coordinates [x][y] = " . ";
            }
        }
        return coordinates;
    }

    //INSERE AS BARREIRAS (FALTA CARTÓRIO)
    private void insertRegistries(int n) {
        boolean validColumn = false;
        int randomY, randomX = 0;
        int index = 0;
        int usedColumns[] = new int[column];
        for (int i = 0; i < n ; i++) {
            int wallSize = line/2;
            validColumn = false;
            randomY = ((int) (Math.random() * line + 1)) - (wallSize -1);
            if(randomY < 0){
                randomY = 1;
            }
            while(!validColumn) {
                randomX = ((int) (Math.random() * column) + 1) - 2;
                if (randomX < 0) {
                    randomX = 1;
                }
                if (isValidColumn(randomX, usedColumns)){
                    usedColumns[index] = randomX;
                    index++;
                    if (isValidColumn(randomX+1, usedColumns)){
                        if(randomX < column - 1) {
                            usedColumns[index] = randomX + 1;
                            index++;
                        }
                    }
                    if (isValidColumn(randomX-1, usedColumns)){
                        if (randomX < column - 1) {
                            usedColumns[index] = randomX - 1;
                            index++;
                        }
                    }
                    validColumn = true;
                }
            }

            for (int j = 0; j < wallSize - 1; j++) {
                coordinates[randomY][randomX] = "[ ]";
                randomY ++;
            }
        }
    }

    //VERIFICAÇÃO PARA IMPEDIR BARREIRAS NA MESMA COLUNA
    private boolean isValidColumn(int randomX, int[] usedColumns) {

        for (int i = 0; i < usedColumns.length ; i++) {
            if (randomX == usedColumns[i]) return false;
        }
        return true;
    }

    //GERA POPULAÇÃO INICIAL (A MODIFICAR)
    private void generatePopulation(int couples) {

        generatePopulation(couples, " M ", men);
        generatePopulation(couples, " F ", women);

    }

    private void generatePopulation(int couples, String s, ArrayList<Person> a){
        for (int i = 1; i <= couples; i++) {
            Person p = new Person(i, s,"Single");
            a.add(p);
            insertOnBoard(p);
        }
    }

    private void generatePrioritiesList(){

        ArrayList<Integer> preferenceOrder = new ArrayList<Integer>();
        for (int i = 1; i <= couplesN * 2; i++) {
            preferenceOrder.add(i);
        }

        for (Person p: men
             ) {
            Collections.shuffle(preferenceOrder);
            p.setPreferences(preferenceOrder);
        }

        for (Person p: women
             ) {
            Collections.shuffle(preferenceOrder);
            p.setPreferences(preferenceOrder);
        }
    }

    //INSERE A POPULAÇÃO INICIAL ALEATORIAMENTE NO MAPA
    private void insertOnBoard(Person p) {
        int randomX, randomY;
        boolean found = false;
        while(!found) {
            randomY = (int) ((Math.random() * line) + 1) - 1 ;
            randomX = (int) ((Math.random() * column) + 1) - 1 ;
            if (emptySpace(randomX,randomY)){
                coordinates[randomY][randomX] = p.getGender();
                p.setPosition(randomX, randomY);
                found = true;
            }
        }

    }

    //VERIFICA SE O ESPAÇO É VÁLIDO
    private boolean emptySpace(int x, int y){
        if (coordinates[y][x].equals(" . "))
            return true;
        return false;
    }

    //IMPRIME O MAPA EM SEU ETADO ATUAL
    public boolean printBoard(){

        System.out.println("*****************");
        for (int x = 0; x < line; x++) {
            for (int y = 0; y < column; y++) {

                String element = getQuadrant(x, y);
                System.out.printf("%s", element);
            }
            System.out.println();

        }

        return true;
    }

    //RETORNA COORDENADAS NA FORMA DE STRING
    private String getQuadrant(int x, int y) {

        return coordinates [x][y];

    }

    //MOVIMENTA A POPULAÇÃO
    public void movePopulation() {
        String pos;
        int xPos, yPos, newX, newY;
        movePopulation(men);
        movePopulation(women);
        RemoveMarriedMan();

    }

    //MOVIMENTA A POPULAÇÃO (PRIVADO E SEPARANDO POR SEXO)
    private void movePopulation(ArrayList<Person> population) {
        String pos;
        int xPos;
        int yPos;
        boolean objective = false;
        for (Person p: population
                ) {
            pos = p.getCoordinates();
            if(Men(p)) {
                objective = objectiveFound(p);
                Interact(p);
            }
            if(p.getState().equals("Single")){
                String parts [] = pos.split(";");
                xPos = Integer.parseInt(parts[0]);
                yPos = Integer.parseInt(parts[1]);
                if(!objective){
                    pos = p.RandomMove();
                    MovingPerson(p, xPos, yPos, pos);
                }
                else{
                    pos = p.MoveToObjective(p.getDestiny());
                    if(!MovingPerson(p, xPos, yPos, pos)){
                        //Aqui vai o para o algoritmo A*
                    }
                }
            }
            //APENAS PARA VISUALIZAR A MOVIMENTAÇÃO.
            System.out.print("\033[H\033[2J");
            System.out.flush();
            printBoard();
            try {

                Thread.sleep(100);

            }
            catch (InterruptedException e){

                Thread.currentThread().interrupt();

            }
            //FIM VISUALIZAÇÃO.
        }
    }

    private boolean Men(Person p) {
        if(p.getGender() == " M ") return true;
        return false;
    }

    private boolean MovingPerson(Person p, int x, int y, String pos){
        int newX, newY;
        String parts [] = pos.split(";");
        newX = x + Integer.parseInt(parts[0]);
        newX = checkLimits(newX, column);
        newY = y + Integer.parseInt(parts[1]);
        newY = checkLimits(newY, line);
        if(emptySpace(newX, newY)) {
            coordinates[y][x] = " . ";
            coordinates[newY][newX] = p.getGender();
            p.setPosition(newX, newY);
            return true;
        }
        return false;

    }

    //GARANTE QUE UMA NOVA COORDENADA NÃO VAI EXCEDER O TAMANHO DA MATRIZ OU TER VALOR NEGATIVO.
    private int checkLimits(int n, int size) {

        if (n < 0) return 0;
        if (n > size - 1) return size - 1;
        else return n;
    }

    //Metodo que pega os arredores de uma pessoa e retorna estes valores.
    private boolean objectiveFound(Person p){

        String part [] = p.getCoordinates().split(";");
        int xCoord = Integer.parseInt(part[0]);
        int yCoord = Integer.parseInt(part[1]);
        String objective;
        if(p.getGender().equals(" M ")){
            objective = " F ";
        }
        else{
            objective = " M ";
        }

        //DEFINIR AÇÃO COM BASE NOS VIZINHOS

        //DEFINIR DIREÇÃO COM BASE NOS VIZINHOS DOS VIZINHOS;

        if(CheckLine(p, objective, yCoord - 2, xCoord, true)){
            return true;
        }

        if(CheckLine(p, objective, xCoord + 2, yCoord, false)){
            return true;

        }

       if( CheckLine(p, objective, yCoord + 2, xCoord, true)){
            return true;

       }

        if (CheckLine(p, objective, xCoord - 2, yCoord, false)){
            return true;
        }
        return false;
    }


    //PROBLEMA NESTE MÉTODO
    private boolean CheckLine(Person p, String objective, int constant, int variable, boolean x) {
        for(int i = variable - 2; i <= variable +2; i++){
            //X é a variável
            if(x) {
                if (!BoardLimitsY(constant)) return false; //Se a constante é inválida nem checo a variável
                if (BoardLimitsX(i)) {
                    if (coordinates[constant][i].equals(objective)) {
                        String destiny = i + ";" + constant;
                        p.setDestiny(destiny);
                        return true;
                    }
                }
            }
                //y é a variável
                else{
                    if(!BoardLimitsX(constant)) return false;
                    if(BoardLimitsY(i)){
                        if (coordinates[i][constant].equals(objective)){
                            String destiny = constant + ";" + i;
                            p.setDestiny(destiny);
                            return true;
                        }
                    }
                }
            }
        return false;
    }

    private boolean BoardLimitsX(int x){

        return (x >= 0 && x < column);

    }

    private boolean BoardLimitsY(int y){
        return (y >= 0 && y < line);
    }

    private void Interact(Person p){
        String objective1 = " F ";
        String objective2 = " C ";
        int x = p.getX();
        int y = p.getY();
        for (int i = y - 1 ; i <= y + 1 ; i++) {
            if(BoardLimitsY(i)) {
                for (int j = x - 1; j <= x + 1; j++) {
                    if(BoardLimitsX(j)) {
                            if (coordinates[i][j].equals(objective1)) {
                                String wCoordinates = j + ";" + i;
                                Person w = SearchWoman(wCoordinates);
                                if(p.getState().equals("Single")){
                                    if (s.matches(p, w)) {
                                        generateCouple(p, w);
                                        coordinates[y][x] = " C ";;
                                        x = w.getX();
                                        y = w.getY();
                                        coordinates[y][x] = " . ";
                                        printBoard();
                                        return;
                                    }
                                }
                            }
                            else {
                                if (coordinates[i][j].equals(objective2)) {
                                    String cCoordinates = j + ";" + i;
                                    Couple c = SearchCouple(cCoordinates);
                                    try{
                                        Person w = c.getWife();
                                        if (s.matches(p, w)) {
                                            Person outsider = dissolveCouple(c);
                                            coordinates[y][x] = " C ";
                                            String parts[] = cCoordinates.split(";");
                                            x = Integer.parseInt(parts[0]);
                                            y = Integer.parseInt(parts[1]);
                                            outsider.setPosition(x, y);
                                            outsider.setStatus("Single");
                                            outsider.setActualParterPriority(0);
                                            coordinates[y][x] = " M ";
                                            generateCouple(p, w);
                                            printBoard();
                                        }

                                    } catch (NullPointerException e){
                                        e.getMessage();

                                    }


                                }

                            }


                    }
                }
            }
        }
        return;
    }

    private Person dissolveCouple(Couple c) {
        Person p = c.getHusband();
        couples.remove(c);
        return p;
    }

    private Couple SearchCouple(String cCoordinates) {
        for (Couple c: couples
             ) {
            if(c.getCoordinates().equals(cCoordinates)){
                return c;
            }

        }
        return null;
    }

    private void generateCouple(Person p, Person w) {
        Couple c = new Couple(p, w);
        p.setStatus("Married");
        w.setStatus("Married");
        c.setCoordinates(p.getCoordinates());
//        p.setPosition(-1,-1);
//        w.setPosition(-1,-1);
        couples.add(c);
        //men.remove(p);
        women.remove(w);
    }

    private void RemoveMarriedMan(){
        Iterator<Person> list = men.iterator();

        while(list.hasNext()){
            Person p = list.next();
            if(p.getState().equals("Married")){
                list.remove();
            }
        }



    }

    private Person SearchWoman(String wCoordinates) {
        for (Person p: women
             ) {
            if (p.getCoordinates().equals(wCoordinates))return p;
        }
        return null;
    }
}
