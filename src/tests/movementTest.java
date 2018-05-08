import org.junit.Test;

import static org.junit.Assert.*;

public class movementTest {

    public static void main(String[] args) {


        int matrix [][] = {{1,2,3},
                           {4,5,6},
                           {7,8,9}};
        matrix[2][0] = 3;

        System.out.println(matrix[2][0]);
    }

}
