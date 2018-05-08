import java.util.ArrayList;

public class Person {

    private int id, actualParterPriority,  posX, posY;
    private String gender, state;
    private String destiny;
    private String coordinates;
    private ArrayList<Integer> preferences = new ArrayList<>();
    private ArrayList<String> registryCoordinates = new ArrayList<>();

    public Person(int id, String gender, String state) {
        this.id = id;
        this.gender = gender;
        this.state = state;
        actualParterPriority = 0;
    }

    public String getGender() {
        return gender;
    }

    public String Identification(){
        return this.gender + this.id;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getPreferences() {
        return preferences;
    }

    public int getActualParterPriority() {
        return actualParterPriority;
    }

    public String getDestiny() {
        return destiny;
    }
    
    public void addPreferences(int n){
        preferences.add(n);
    }
    
    public void addRegistry(String c){
        registryCoordinates.add(c);
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

    public void setActualParterPriority(int actualParterPriority) {
        this.actualParterPriority = actualParterPriority;
    }

    public String getState() {
        return state;
    }

    public void setPosition(int x, int y){

        posX = x;
        posY = y;
        coordinates = x + ";" + y;
    }

    public String RandomMove(){

        int n = (int)(Math.random() * 4);
        return moveSet(n);

    }

    private String moveSet(int dir){
        switch (dir){
            case 0: return "0;-1";
            case 1: return "0;1";
            case 2: return "1;0";
            case 3: return "-1;0";
            default: return "0;0";
        }

    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setPreferences(ArrayList<Integer> preferences) {
        this.preferences = preferences;
    }

    public void printStats(){
        String id = gender +this.id;
        String preference = "[";
        String oposit = getCouterGender();
        for (int n: this.preferences
             ) {
            preference = preference + oposit + n + " , ";
        }
        preference = preference + "]";
        System.out.printf("Identidade: %s\nEstado: %s\nPreferências: %s\n\n", id, state, preference);
    }

    private String getCouterGender() {
        switch (this.gender){
            case "m": return "f";
            case "f": return "m";
            default: return "m";
        }
    }

    public void setStatus(String newStatus){
        this.state = newStatus;
    }

    public String MoveToObjective(Board board) {

        String pos [] = this.coordinates.split(";");
        int x = Integer.parseInt(pos[0]);
        int y = Integer.parseInt(pos[1]);
        pos = this.destiny.split(";");
        int destX = Integer.parseInt(pos[0]);
        int destY = Integer.parseInt(pos[1]);

        int nextX = x - 1;
        int nextY = y - 1;
        double nota = heuristica(nextX,nextY,destX,destY);
        double nota_atual = nota;
        for(int x_atual = x - 1; nextX < x + 2; nextX++){
            for(int y_atual = y - 1; nextY < y + 2; nextY++){
                nota_atual = heuristica(x_atual,y_atual,destX,destY);
                if(nota_atual > nota && board.emptySpace(x_atual,y_atual)){
                    nextX = x_atual;
                    nextY = y_atual;
                }
            }
        }

        int diffX = destX - x; //Será +2, -2 ou 0
        int diffY = destY - y; //Será +2, -2 ou 0

        diffX = correctMove(diffX);
        diffY = correctMove(diffY);

        return diffX + ";" + diffY;

    }

    private int correctMove(int d) {
        if (d == 2) return 1;
        if (d == -2) return -1;
        else return 0;
    }

    public double heuristica(int XAtual, int YAtual, int XFinal, int YFinal) {
        int deltaX = Math.abs(XAtual - XFinal);
        int deltaY = Math.abs(YAtual - YFinal);
        
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public int getX(){
        return this.posX;
    }

    public int getY(){
        return this.posY;
    }
}
