package App;

public class Person {

    private int id;
    private String gender, state;
    private int[][] destiny;
    String pos;

    public Person(int id, String gender, String state) {
        this.id = id;
        this.gender = gender;
        this.state = state;
    }

    public String getGender() {
        return gender;
    }

    public void setPosition(int x, int y){
        pos = x + ";" + y;
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
        return pos;
    }
}
