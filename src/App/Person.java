package App;

import java.util.ArrayList;

public class Person {

    private int id, actualParterPriority;
    private String gender, state;
    private int[][] destiny;
    String coordinates;
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

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getPreferences() {
        return preferences;
    }

    public int getActualParterPriority() {
        return actualParterPriority;
    }

    public void setActualParterPriority(int actualParterPriority) {
        this.actualParterPriority = actualParterPriority;
    }

    public String getState() {
        return state;
    }

    public void setPosition(int x, int y){
        coordinates = x + ";" + y;
    }

    public String Move(){

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

}
