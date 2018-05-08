package App;

public class Couple {

    private Person husband, wife;
    private String info, coordinates, state;
    private int x, y;

    public Couple(Person husband, Person wife){
        this.husband = husband;
        this.wife = wife;
        this.info = " C ";
        this.state = getHusband().getState();
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public Person getHusband() {
        return husband;
    }

    public Person getWife() {
        return wife;
    }

    public String getInfo() {
        return info;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
        String parts [] = coordinates.split(";");
        this.x = Integer.parseInt(parts[0]);
        this.y = Integer.parseInt(parts[1]);
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void printCouple(){
        System.out.printf("Casal\n" +
                "Marido: %s\n" +
                "Mulher: %s\n\n", husband.getId(), wife.getId());
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }


    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
}
