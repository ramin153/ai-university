import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class TickTokToe {
    private final static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args)
    {
        int[] game =  new int[2];
        while (true)
        {
            System.out.println("player "+game[0]);
            System.out.println("agent "+game[1]);
            System.out.println("if you want to exit ==> enter e else you will play");
            String input = scanner.nextLine().toLowerCase(Locale.ROOT);
            if (input.equals("e"))
                break;

            playGame(game);
        }


        System.out.println("Farewell");

    }

    public static int[] playGame(int[] result)
    {
        int[][] map = new int[3][3];
        int turn = 1;

        printMyMap(map);
        while (true)
        {


            playerMove(map,turn);



            if (isEnd(map))
            {
                result[0]+=1;
                System.out.println("you win");
                break;
            }
            if (fullMap(map)) {
                System.out.println("draw");

                break;
            }

            printMyMap(map);

           int[] move = new int[2];
           max(Integer.MAX_VALUE ,map,turn*-1,move);
           map[move[0]][move[1]] = (turn*-1);




            if (isEnd(map))
            {
                System.out.println("you lost");
                result[1]+=1;
                break;
            }
            if (fullMap(map)) {
                System.out.println("draw");
                break;
            }


            printMyMap(map);

        }
        printMyMap(map);

        return result;

    }

    public static void playerMove(int[][] map,int turn)
    {
        while (true)
        {
            boolean rightInput = false;
            int row = -1;
            while (!rightInput)
            {
                try {
                    System.out.println("which row do you want to mark (1 or 2 or 3)");
                    row = Integer.parseInt(scanner.nextLine());
                    if (row >= 1 && row <=3 )
                    {
                        rightInput = true;
                        row -= 1;
                    }

                } catch (Exception e)
                {

                }
            }
            rightInput = false;
            int col = -1;
            while (!rightInput)
            {
                try {
                    System.out.println("which col do you want to mark (1 or 2 or 3) and if you want reset your move give 0");

                    col = Integer.parseInt(scanner.nextLine());
                    if (col == 0)
                    {
                        break;
                    }
                    else if (col >= 1 && col <=3 ){
                        rightInput = true;
                        col -= 1;
                    }

                } catch (Exception e)
                {

                }

            }

            if(!rightInput)
                continue;
            if (map[row][col] != 0)
            {
                System.out.println("sorry you can't place there");

            }else
            {
                map[row][col] = turn;
                break;
            }

        }

    }

    private static int min(int alpha,int[][] map,int turn,int[] pos)
    {
        if (isEnd(map))
            return 1;
        if (fullMap(map))
            return 0;
        int result = Integer.MAX_VALUE;

        for (int i = 0; i < map.length;i++)
        {
            for (int j = 0; j < map[i].length; j++)
            {
                if (map[i][j] == 0)
                {
                    map[i][j] = turn;
                    int maxVal = max(result,map,turn*-1,new int[2]);
                    map[i][j] = 0;
                    if (maxVal < result) {
                        pos[0] = i;
                        pos[1] = j;
                        result = maxVal;
                    }
                    if (result < alpha)// Alpha-Beta pruning
                        return alpha;


                }
            }
        }

        return result;
    }


    private static int  max(int beta,int[][] map,int turn,int[] pos)
    {
        if (isEnd(map))
            return -1;
        if (fullMap(map))
            return 0;
        int result = Integer.MIN_VALUE ;

        for (int i = 0; i < map.length;i++)
        {
            for (int j = 0; j < map[i].length; j++)
            {
                if (map[i][j] == 0)
                {
                    map[i][j] = turn;
                    int minaVal = min(result,map,turn*-1,new int[2]);
                    map[i][j] = 0;

                    if (minaVal > result){
                        pos[0] = i;
                        pos[1] = j;
                        result = minaVal;
                    }

                    if (result > beta)// Alpha-Beta pruning
                        return beta;


                }
            }
        }

        return result;
    }


    private static boolean  isEnd(int[][] map)
    {
        for (int i = 0; i < map.length;i++)
        {
            if (map[i][0] == map[i][1] && map[i][0] == map[i][2] && 0 != map[i][0])
                return true;
        }

        for (int i = 0; i < map[0].length;i++)
        {
            if (map[0][i] == map[1][i] && map[0][i] == map[2][i] && 0 != map[0][i])
                return true;
        }

        if (map[0][0] == map[1][1] && map[1][1] == map[2][2] && 0 != map[1][1])
            return true;

        if (map[0][2] == map[1][1] && map[1][1] == map[2][0] && 0 != map[1][1])
            return true;

        return false;
    }

    private static boolean fullMap(int[][] map)
    {
        for (int i = 0; i < map.length;i++)
        {
            for (int j = 0; j < map[i].length; j++)
            {
                if (map[i][j] == 0)
                    return false;
            }

        }



        return true;
    }

    private static void printMyMap(int[][] map)
    {

        System.out.println("\n    1   2   3");
        System.out.println("  -------------");
        for (int i = 0 ; i < map.length;i++)
        {

            System.out.print((i+1)+" ");
           for (int j = 0; j < map[i].length;j++)
           {
               int printChar = map[i][j];
               System.out.print("| ");
               if (printChar == 1)
                   System.out.print("O ");
               else if (printChar == -1)
                   System.out.print("X ");
               else
                   System.out.print("  ");
           }
            System.out.println("|");
            System.out.println("  -------------");
        }
        System.out.println();
    }

    private static int debugCount(int[][] map)
    {
        int result = 0;
        for (int i = 0; i < map.length;i++)
        {
            for (int j = 0; j < map[i].length; j++)
            {
                if (map[i][j] != 0)
                {
                    result ++;
                }
            }
        }

        return result;
    }




}
