package App;

import java.util.ArrayList;

public class Person {

    private int id, actualParterPriority,  posX, posY;
    private String gender, state;
    private String destiny;
    private String coordinates;
    private ArrayList<Integer> preferences = new ArrayList<>();

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

    public String MoveToObjective(String s) {

        String pos [] = this.coordinates.split(";");
        int x = Integer.parseInt(pos[0]);
        int y = Integer.parseInt(pos[1]);
        pos = this.destiny.split(";");
        int destX = Integer.parseInt(pos[0]);
        int destY = Integer.parseInt(pos[1]);

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

    public void setPreferenceList(String s){
        String pref [] = s.split("  ");
        for (int i = 0; i < pref.length - 1 ; i++) {
            int n = Integer.parseInt(pref[i]);
            preferences.add(n);
        }
        int last = pref.length -1;
            int invalidSpace = pref[last].length() - 1;
            int n = Integer.parseInt(pref[last].substring(0,invalidSpace));
            preferences.add(n);
    }

    public int getX(){
        return this.posX;
    }

    public int getY(){
        return this.posY;
    }
}
