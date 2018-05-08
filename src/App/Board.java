package App;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Board {


    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private int line, column;
    private String coordinates[][];
    private Random rand = new Random();
    private ArrayList <Person> men = new ArrayList<>();
    private ArrayList <Person> women = new ArrayList<>();
    private ArrayList <Couple> couples = new ArrayList<>();
    private ArrayList<String> lines, registryCoordinates = new ArrayList<>();
    private StableMatching s = new StableMatching();
    private int couplesNum;


    //CRIA MAPA   (CAMPO + BARREIRAS + POPULAÇÃO)
    public Board (int couples, int registries, ArrayList<String> lines, String offset){
        this.line = couples;
        this.column = couples;
        this.couplesNum = couples;
        this.lines = lines;
        coordinates = mountEmptyBoard(line, column);
        insertObstacles(couplesNum);
        insertRegistries(registries);
        generatePopulation(couplesNum, lines, offset);
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

    private void insertObstacles(int couplesN){
        int y = couplesN/2;
        int high = y-1;
        //System.out.println(res);
        int col = 0;
        for(double i = 0.2; i < 1; i += 0.2 ){
            col = (int)(couplesN*i);
            int res = rand.nextInt(high);
            //System.out.println(col);
            for(int j = res; j < res+y; j++){
                coordinates[j][col] = " X ";
            }
        }
    }

    private void insertRegistries(int r){
        int remaining = r;
        while(remaining != 0){
            int x = rand.nextInt(couplesNum-1);
            int y = rand.nextInt(couplesNum-1);
            if(!coordinates[x][y].equals(" X ")){
                if(verifyNearbyRegistry(x,y)){
                    coordinates[x][y] = " R ";
                    registryCoordinates.add(String.valueOf(y) + ";" + String.valueOf(x));
                    remaining--;
                }
            }
        }
    }

    private boolean verifyNearbyRegistry(int x, int y){
        if((y -= 1) == -1) return false;
        if((y += 1) == couplesNum) return false;

        if(coordinates[x][y-1].equals(" X ")) return true;
        if(coordinates[x][y+1].equals(" X ")) return true;
        return false;
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
    public boolean emptySpace(int x, int y){
        if(BoardLimitsX(x) && BoardLimitsY(y)) {
            if (coordinates[y][x].equals(" . "))
                return true;
        }
        return false;
    }

    private void ChangeMap(int x, int y, int oldX, int oldY, String entering, String leaving) {
        if (BoardLimitsX(x) && BoardLimitsY(y)) {
            coordinates[y][x] = entering;
            coordinates[oldY][oldX] = leaving;
        }
    }

    //IMPRIME O MAPA EM SEU ETADO ATUAL
    public boolean printBoard(){

        System.out.println("*****************");
        for (int y = 0; y < line; y++) {
            for (int x = 0; x < column; x++) {
                if(coordinates[y][x].equals(" . ")) System.out.printf(ANSI_BLACK + "%s", coordinates[y][x]);
                else if(coordinates[y][x].equals(" R ")) System.out.printf(ANSI_GREEN + "%s", coordinates[y][x]);
                else if(coordinates[y][x].equals(" X ")) System.out.printf(ANSI_RED + "%s",coordinates[y][x]);
                else if(coordinates[y][x].equals(" M ")) System.out.printf(ANSI_BLUE + "%s", coordinates[y][x]);
                else if(coordinates[y][x].equals(" F ")) System.out.printf(ANSI_PURPLE + "%s", coordinates[y][x]);
                else if(coordinates[y][x].equals(" C ")) System.out.printf(ANSI_CYAN + "%s",coordinates[y][x]);
                else System.out.printf(ANSI_RED + "%s",coordinates[y][x]);
            }
            System.out.println();

        }

        return true;
    }

    //MOVIMENTA A POPULAÇÃO
    public void movePopulation() {

        MoveCouples(couples);
        Interact(men);
        RemoveMarriedMan();
        AddDivorced();
        movePopulation(men);
        movePopulation(women);
        RemoveBrokeCouples();

    }

    private void Interact(ArrayList<Person> men) {
        String objective1 = " F ";
        String objective2 = " C ";
        int x, y;
        for (Person m: men
             ) {
            x = m.getX();
            y = m.getY();
            InteractionLoop(m, x, y, objective1, objective2);

        }

    }

    private void InteractionLoop(Person p, int x, int y, String objective1, String objective2){
        for (int i = x - 1; i <= x + 1 ; i++) {
            for (int j = y; j <= y+1 ; j++) {
                if(i != x || j!= y){
                    if(VerifyObjective(i,j, objective1)){
                        ProposeSingle(p, i, j);
                    }
                    else if(VerifyObjective(i, j, objective2)){
                        ProposeCouple(p, i, j);
                    }
                }
            }
        }
    }

    private void ProposeCouple(Person p, int x, int y) {

        Couple breakingUp = SearchCouple(x,y);
        Person w = breakingUp.getWife();
        if(s.matches(p, w)){
            Couple c = generateCouple(p, w);
            Person castOut = dissolveCouple(breakingUp, 0);;
            castOut.setStatus("Divorced");
            castOut.setActualParterPriority(0);
            ChangeMap(p.getX(), p.getY(), x, y, c.getInfo(), castOut.getGender());
            //CASA, MUDA ESTADO, ETC.
        } else{
            p.setActualParterPriority(0);
        }

    }

    private void ProposeSingle(Person p, int x, int y){
        Person w = SearchWoman(x,y);
        if(s.matches(p,w)){
            Couple c = generateCouple(p,w);
            ChangeMap(p.getX(), p.getY(), x, y, c.getInfo(), " . ");
            //CASA, MUDA ESTADO, ETC.
        } else {
            p.setActualParterPriority(0);
        }
    }

    //GENERATE COUPLE AQUI

    private Couple generateCouple(Person p, Person w) {
        p.setStatus("Engaged");
        w.setStatus("Engaged");
        Couple c = new Couple(p, w);
        c.setPosition(p.getX(), p.getY());
        p.setDestiny(ClosestRegistry(p.getX(), p.getY()));
        w.setPosition(p.getX(),p.getY());
        couples.add(c);
        women.remove(w);
        return c;
    }

    //DISSOLVE COUPLE AQUI

    private Person dissolveCouple(Couple c, int sex) {
        Person p;
        if(sex == 0){
            p = c.getHusband();

        } else{
            p = c.getWife();
        }
        c.setState("Divorced");
        return p;
    }

    private Couple SearchCouple(int x, int y) {
        for (Couple c: couples
                ) {
            if(x == c.getX() && y == c.getY()){
                return c;
            }

        }
        return null;
    }

    private void movePopulation(ArrayList<Person> pop) {
        for (Person p: pop
             ) {
            int x = p.getX();
            int y = p.getY();
            if (p.getGender().equals(" M ")) {
                if (LookAround(p)) {
                    String s = p.MoveToObjective(this);
                    String xy[] = s.split("\\;");
                    int newX = Integer.parseInt(xy[0]);
                    int newY = Integer.parseInt(xy[1]);
                    newX = newX + x;
                    newY = newY + y;
                    if (emptySpace(newX, newY)) {
                        ChangeMap(newX, newY, x, y, p.getGender(), " . ");
                        p.setPosition(newX, newY);
                    } else {
                        RandomMove(p);
                    }

                } else {
                    RandomMove(p);
                }
            } else {
                RandomMove(p);

            }

            Wait(80);
        }
    }

    private void MoveCouples(ArrayList <Couple> couples){
        for (Couple c: couples
                ) {
            if(c.getState().equals("Engaged")){
                //SE ESTÁ AO LADO DO CARTÓRIO, ALTERA DA CONTINUE



                //Faz o A* e dá Return;
                Person p = c.getHusband();
                int x = p.getX();
                int y = p.getY();
                String s  = p.MoveToObjective(this);
                String xy [] = s.split("\\;");
                int newX = Integer.parseInt(xy[0]);
                int newY = Integer.parseInt(xy[1]);
                newX = newX + x;
                newY = newY + y;
                if (emptySpace(newX, newY)) {
                    ChangeMap(newX, newY, x, y, c.getInfo(), " . ");
                    c.setPosition(newX, newY);
                    p.setPosition(newX, newY);
                    c.getWife().setPosition(newX, newY);
                } else {
                    RandomMoveCouples(c);
                }
                return;
            }

            RandomMoveCouples(c);

        }
        Wait(80);
    }



    private String ClosestRegistry(int x, int y){

        double weight = 100000;
        String res = "";
        for (String s: registryCoordinates) {

            String parts[] = s.split("\\;");
            int regX = Integer.parseInt(parts[0]);
            int regY = Integer.parseInt(parts[1]);

            int deltaX = Math.abs(x - regX);
            int deltaY = Math.abs(y - regY);

            double newWeight = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if(newWeight < weight){
                weight = newWeight;
                res = s;
            }

        }
        return res;
    }

    private void RandomMoveCouples(Couple c) {
        Person p = c.getHusband();
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
                c.setPosition(newX, newY);
                Person w = c.getWife();
                w.setPosition(newX, newY);
                p.setPosition(newX, newY);
                ChangeMap(newX, newY, x, y, c.getInfo(), " . ");;
                return;
            }
            attempts++;
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
                ChangeMap(newX, newY, x, y, p.getGender(), " . ");;
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

    private void RemoveMarriedMan(){
        Iterator<Person> list = men.iterator();

        while(list.hasNext()){
            Person p = list.next();
            if(p.getState().equals("Married")) list.remove();
            else if (p.getState().equals("Engaged")) list.remove();
        }

    }

    private void AddDivorced(){
        Iterator<Couple> list = couples.iterator();

        while(list.hasNext()){
            Couple c = list.next();
            Person p = c.getHusband();
            if(p.getState().equals("Divorced")){
                men.add(p);
                list.remove();
            }
        }
    }

    private void RemoveBrokeCouples(){

        Iterator<Couple> list = couples.iterator();

        while(list.hasNext()){
            Couple c = list.next();
            if(c.getState().equals("Divorced")) list.remove();
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

    public void printStatus() {
        int n = 1;
        for (Couple c: couples
             ) {
            System.out.printf("Casal %d: %s & %s\n", n, c.getHusband().getId(), c.getWife().getId() );
            n++;
        }
    }
}
