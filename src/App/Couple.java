package App;

public class Couple {

    private Person husband, wife;
    private String info, coordinates, destiny;
    private int x, y;

    public Couple(Person husband, Person wife){
        this.husband = husband;
        this.wife = wife;
        this.info = " C ";
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

    public String getDestiny() {
        return destiny;
    }

    public void printCouple(){
        System.out.printf("Casal\n" +
                "Marido: %s\n" +
                "Mulher: %s\n\n", husband.getId(), wife.getId());
    }

    public int posX(){
        return this.x;
    }

    public int posY(){
        return this.y;
    }
}
