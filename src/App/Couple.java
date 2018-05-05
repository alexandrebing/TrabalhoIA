package App;

public class Couple {

    private Person husband, wife;

    public Couple(Person husband, Person wife){
        this.husband = husband;
        this.wife = wife;
    }

    public Person getHusband() {
        return husband;
    }

    public Person getWife() {
        return wife;
    }

    public void printCouple(){
        System.out.printf("Casal\n" +
                "Marido: %s\n" +
                "Mulher: %s\n\n", husband.getId(), wife.getId());
    }
}
