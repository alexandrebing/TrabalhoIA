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
    }

    //MONTA MAPA INICIAL SEM ESTRUTURAS
    private String[][] mountEmptyBoard(int width, int height) {
        coordinates = new String[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                coordinates [y][x] = " . ";
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
                coordinates[j][col] = "[ ]";
            }
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
        for (int i = couplesNum + 1; i <= (couplesNum * 2) ; i++) {
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
        if(BoardLimitsX(x) && BoardLimitsY(y)) {
            if (coordinates[y][x].equals(" . "))
                return true;
        }
        return false;
    }

    //IMPRIME O MAPA EM SEU ETADO ATUAL
    public boolean printBoard(){

        System.out.println("*****************");
        for (int y = 0; y < line; y++) {
            for (int x = 0; x < column; x++) {
                System.out.printf("%s", coordinates[y][x]);
            }
            System.out.println();

        }

        return true;
    }

    //MOVIMENTA A POPULAÇÃO
    public void movePopulation() {

        movePopulation(men);
        movePopulation(women);

    }

    private void movePopulation(ArrayList<Person> pop) {
        for (Person p: pop
             ) {
            int x = p.getX();
            int y = p.getY();
            if (p.getGender().equals(" M ")) {
                if (LookAround(p)) {
                    String s = p.MoveToObjective();
                    String xy[] = s.split("\\;");
                    int newX = Integer.parseInt(xy[0]);
                    int newY = Integer.parseInt(xy[1]);
                    newX = newY + x;
                    newY = newY + y;
                    if (emptySpace(newX, newY)) {
                        p.setPosition(newX, newY);
                        coordinates[newY][newX] = p.getGender();
                        coordinates[y][x] = " . ";
                    }

                } else {
                    RandomMove(p);
                }
            } else {
                RandomMove(p);

            }

            Wait(100);
        }
    }

    private void RandomMove(Person p) {
        int attempts = 0;
        while(attempts < 9) {
            int x = p.getX();
            int y = p.getY();
            String[] xy = p.RandomMove().split("\\;");
            int newX = Integer.parseInt(xy[0]);
            int newY = Integer.parseInt(xy[1]);
            newX = newX + x;
            newY = newY + y;
            if (emptySpace(newX, newY)) {
                p.setPosition(newX, newY);
                coordinates[newY][newX] = p.getGender();
                coordinates[y][x] = " . ";
                return;
            }
            attempts++;
        }
    }


    //GARANTE QUE UMA NOVA COORDENADA NÃO VAI EXCEDER O TAMANHO DA MATRIZ OU TER VALOR NEGATIVO.
    private int checkLimits(int n, int size) {
        if (n < 0) return 0;
        if (n > size - 1) return size - 1;
        else return n;
    }

    private boolean LookAround(Person p){
        int x = p.getX();
        int y = p.getY();
        String objective1 = " F ";
        String objective2 = " C ";
        if(p.getState().equals(" F ")) objective1 = " M " ;
        if(FoundDestiny(p, objective1)) return true;
        if(FoundDestiny(p, objective2)) return true;
        return false;
    }

    //Metodo que pega os arredores de uma pessoa e retorna estes valores.
    public boolean FoundDestiny(Person p, String o){
        int x = p.getX();
        int y = p.getY();

        for (int i = x - 2; i <= x + 2; i++) {
            for (int j = y - 2; j <= y + 2 ; j++) {
                int distX = Math.abs(i - x);
                int distY = Math.abs(j - y);
                if(distX > 1 || distY > 1){
                    if (VerifyObjective(i, j, o)){
                        String destiny = i + ";" + j;
                        p.setDestiny(destiny);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean VerifyObjective(int x, int y, String o) {
        if(BoardLimitsX(x) && BoardLimitsY(y)) {
            if (coordinates[y][x].equals(o)) {
                return true;
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
        c.setX(p.getX());
        c.setY(p.getY());
//        p.setPosition(-1,-1);
//        w.setPosition(-1,-1);
        couples.add(c);
        women.remove(w);
    }

    private void RemoveMarriedMan(){
        Iterator<Person> list = men.iterator();

        while(list.hasNext()){
            Person p = list.next();
            if(p.getState().equals("Married")) list.remove();
            else if (p.getState().equals("Engaged")) list.remove();
        }

    }

    private Person SearchWoman(int x, int y) {
        for (Person p: women
             ) {
            if (p.getX() == x && p.getY() == y)return p;
        }
        return null;
    }

    private void Wait(int n){
        //APENAS PARA VISUALIZAR A MOVIMENTAÇÃO.
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printBoard();
        try {

            Thread.sleep(n);

        }
        catch (InterruptedException e){

            Thread.currentThread().interrupt();

        }
        //FIM VISUALIZAÇÃO.

    }
}
