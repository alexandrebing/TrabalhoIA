package App;

import java.util.ArrayList;
import java.util.Collections;

public class StableMatchingTest {

    public static void main(String[] args) {

        int coupleNumber = 5;
        ArrayList<Person> men = new ArrayList<>();
        ArrayList<Person> women = new ArrayList<>();
        ArrayList<Couple> couples = new ArrayList<>();
        ArrayList<Integer> preferenceOrder = new ArrayList<Integer>();
        Couple c;
        Person p;
        boolean unStable = true;
        StableMatching sm = new StableMatching();

        for (int i = 0; i < coupleNumber; i++) {
            preferenceOrder.add(i+1);
        }

        //Homens para teste
        for (int i = 0; i < coupleNumber; i++) {
            p = new Person(i+1, "m", "single");
            Collections.shuffle(preferenceOrder);
            p.setPreferences(preferenceOrder);
            p.printStats();
            men.add(p);
        }

        for (int i = 0; i < coupleNumber; i++) {
            p = new Person(i +1, "f", "single");
            Collections.shuffle(preferenceOrder);
            p.setPreferences(preferenceOrder);
            p.printStats();
            women.add(p);
        }

        int round = 1;

        while(unStable){

            System.out.println("Round: " + round + "-----------------------------------------------------");
            System.out.println("Men size = " + men.size());

            int changes = 0;
            for (Person m: men
                 ) {
                if(m.getState().equals("Solteiro")){
                    m.setActualParterPriority(0);
                }
                int r = (int)(Math.random() * coupleNumber);
                Person f = women.get(r);
                if(sm.matches(m, f)){
                    changes++;
                    if(m.getState().equals("Casado")){
                        Couple divorced = SearchCouple(m, couples);
                        p = divorced.getWife();
                        p.setStatus("Solteiro");
                        p.setActualParterPriority(0);
                        couples.remove(divorced);
                    }
                    if(f.getState().equals("Casado")){
                        Couple divorced = SearchCouple(f, couples);
                        p = divorced.getHusband();
                        p.setStatus("Solteiro");
                        p.setActualParterPriority(0);
//                        System.out.println("Casal removido:");
//                        divorced.printCouple();
                        couples.remove(divorced);
                    }
                    c = new Couple(m,f);
                    m.setStatus("Casado");
                    f.setStatus("Casado");
                    couples.add(c);
//                    c.printCouple();
                }
            }
            if(changes == 0 && couples.size() == coupleNumber){
                unStable = false;
            }

            for (Couple couple: couples
                 ) {
                couple.printCouple();

            }
            round ++;

        }

        System.out.println("RESULTADO FINAL");
        for (Couple couple: couples) {
            couple.printCouple();
        }




    }

    private static Couple SearchCouple(Person p, ArrayList<Couple> couples){

        for (Couple c: couples
             ) {
            if (c.getHusband() == p) return c;
            if (c.getWife() == p) return c;
        }
        return null;
    }

}
