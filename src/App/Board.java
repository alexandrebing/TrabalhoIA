package App;

import java.util.ArrayList;
import java.util.Iterator;

public class Board {

    private int line, column;
    private String coordinates[][];
    private ArrayList<String> lines;
    private ArrayList <Person> men = new ArrayList<>();
    private ArrayList <Person> women = new ArrayList<>();
    private ArrayList <Couple> couples = new ArrayList<>();
    private StableMatching s = new StableMatching();
    private int couplesNum;


    //CRIA MAPA   (CAMPO + BARREIRAS + POPULAÇÃO)
    public Board (int couples, int registries, ArrayList<String> lines, String offset){
        this.line = couples;
        this.column = couples;
        this.couplesNum = couples;
        this.lines = lines;
        coordinates = mountEmptyBoard(line, column);
        insertRegistries(registries);
        generatePopulation(couplesNum, lines, offset);
        System.out.println("Done");
        //generatePrioritiesList();
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

        int y = (int)(Math.random() * (couplesNum/2)) + 1;

        int slots [] = generateLines(n);
        for (int i = 0; i < n; i++) {
            int col = slots[i];
            for (int j = y; j < y + (couplesNum/2); j++) {
                coordinates[col][j] = "[ ]";
            }
            printBoard();
        }

    }

    private int[] generateLines(int n) {
        int constant = couplesNum/n;
        int var = 1;
        int res [] = new int [n];
        for (int i = 0; i < n ; i++) {
            res[i] = var;
            var += constant;
        }
        return res;
    }

    //GERA POPULAÇÃO INICIAL (A MODIFICAR)
    private void generatePopulation(int couples, ArrayList<String> lines, String offset) {

        for (int i = 0; i < couplesNum; i++) {
            String s = lines.get(i);
            String parts [] = s.split(offset);
            int id = Integer.parseInt(parts[0]);
            String preferenceList = parts[1];
            Person p = new Person(id," M ", "Single");
            p.setPreferenceList(preferenceList);
            men.add(p);
            insertOnBoard(p);
        }
        for (int i = couplesNum + 1; i < (couplesNum * 2) ; i++) {
            String s = lines.get(i);
            String parts [] = s.split(offset);
            int id = Integer.parseInt(parts[0]);
            String preferenceList = parts[1];
            Person p = new Person(id," F ", "Single");
            p.setPreferenceList(preferenceList);
            women.add(p);
            insertOnBoard(p);
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

                System.out.printf("%s", coordinates[y][x]);
            }
            System.out.println();

        }

        return true;
    }

    //MOVIMENTA A POPULAÇÃO
    public void movePopulation() {
        moveCouple(couples);
        movePopulation(men);
        RemoveMarriedMan();
        movePopulation(women);


    }

    private void moveCouple(ArrayList<Couple> couples) {
        String pos;
        int xPos;
        int yPos;
        boolean objective = false;
        for (Couple c: couples
                ) {
            pos = c.getCoordinates();
            Person p = c.getHusband();
            xPos = c.posX();
            yPos = c.posY();
            p.setPosition(c.posX(), c.posY());
            objective = objectiveFound(p);
            if (!Interact(c)){
                String parts [] = pos.split(";");
                xPos = Integer.parseInt(parts[0]);
                yPos = Integer.parseInt(parts[1]);
                if(!objective){
                    pos = p.RandomMove();
                    MovingCouple(c, xPos, yPos, pos);
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

                Thread.sleep(50);

            }
            catch (InterruptedException e){

                Thread.currentThread().interrupt();

            }
            //FIM VISUALIZAÇÃO.
        }

    }

    private boolean MovingCouple(Couple c, int x, int y, String pos) {
        int newX, newY;
        String parts [] = pos.split(";");
        newX = x + Integer.parseInt(parts[0]);
        newX = checkLimits(newX, column);
        newY = y + Integer.parseInt(parts[1]);
        newY = checkLimits(newY, line);
        if(emptySpace(newX, newY)) {
            coordinates[y][x] = " . ";
            coordinates[newY][newX] = c.getInfo();
            c.getHusband().setPosition(newX, newY);
            c.getWife().setPosition(newX, newY);
            return true;
        }
        return false;
    }

    private boolean Interact(Couple c) {
        return false;
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

                Thread.sleep(50);

            }
            catch (InterruptedException e){

                Thread.currentThread().interrupt();

            }
            //FIM VISUALIZAÇÃO.
        }
    }

    private void MoveCouples(){

    }

    private boolean Men(Person p) {
        if(p.getGender() == " M ") return true;
        return false;
    }

    //MOVIMENTA UMA PESSOA
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
