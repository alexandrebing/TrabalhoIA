import java.util.ArrayList;

public class StringMatching {


    public boolean matches(Person m, Person f) {
        boolean propose = false;
        boolean accept = false;
        ArrayList<Integer> preferences = m.getPreferences();
        int pretenderRate = preferences.size();
        //BUSCA PREFERENCIAS DO HOMEM
        for (int n: preferences
             ) {
            if(f.getId() == n) break;
            pretenderRate --;
        }
        if(pretenderRate >= m.getActualParterPriority()){
            propose = true;
            m.setActualParterPriority(pretenderRate);
        }
        //BUSCA PREFERENCIAS DA MULHER
        preferences = f.getPreferences();
        pretenderRate = preferences.size();
        for (int n: preferences
                ) {
            if(m.getId() == n) break;
            pretenderRate --;
        }

        if(pretenderRate >= f.getActualParterPriority()){
            accept = true;
            f.setActualParterPriority(pretenderRate);
        }

        if(propose && accept){
            return true;
        }
        return false;

    }
}
