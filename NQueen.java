import java.util.Random;

public class NQueen {

    private final int numberOfQueen;
    private final int[][] queenLocation;
    private int turn = 0;
    private int oldQueen = -1;

    public NQueen(int numberOfQueen) {
        this.numberOfQueen = numberOfQueen;
        queenLocation = new int[numberOfQueen][2];
        for (int i = 0 ; i < numberOfQueen;i++)
        {
            queenLocation[i][0] = i;
            queenLocation[i][1] = i;
        }

    }


    public void printLocation()
    {
        NQueen.print2DArray(queenLocation);
    }
    public void printGame() throws Exception
    {
        int[][] map = new int[numberOfQueen][numberOfQueen];
        for (int[] queen : queenLocation)
        {
            int row = queen[0];
            int col = queen[1];

            if (map[row][col] != 0)
                throw new Exception("there is two queen in same place");
            map[row][col] = 1;
        }

        for (int[] row : map)
        {
            System.out.print('|');
            for (int loc : row)
            {
                if (loc != 0)
                    System.out.print('Q');
                else
                    System.out.print('-');
            }
            System.out.println("|");
        }
        System.out.println();
    }

    public boolean allSafe()
    {
        for (int i = 0 ; i < this.numberOfQueen;i++)
            if (0 != numberOfThread(i))
                return false;

        return true;
    }

    private int nextQueen()
    {
        int queen = -1;
        boolean findQueen = true;
        while (findQueen)
        {
            queen = (this.turn % this.numberOfQueen);
            if(0 != numberOfThread(queen))
                findQueen = false;
            turn++;
        }

        return queen;
    }

    private int nextQueenGreedy()
    {
        int queen = -1;
        int numberOfThread = Integer.MIN_VALUE;
        for (int i = 0; i < this.numberOfQueen;i++)
        {
            int queenThread = numberOfThread(i);
            if( oldQueen != i && numberOfThread <=queenThread)
            {
                numberOfThread = queenThread;
                queen = i;
            }


        }
        oldQueen = queen;

        return queen;
    }

    private int nextQueenRandom()
    {
        Random rand = new Random();
        int queen = -1;
        boolean findQueen = true;
        while (findQueen)
        {
            queen = rand.nextInt(numberOfQueen);
            if(0 != numberOfThread(queen))
                findQueen = false;

        }

        return queen;
    }



    static void print2DArray(int[][] objects)
    {
        System.out.println();
        for (int i = 0 ; i <objects.length ; i++)
        {
            int rowSize = objects[i].length;
            for (int j = 0 ; j < rowSize ; j++)
            {
                System.out.print(" "+objects[i][j]);
            }
            System.out.println();

        }
    }

    private int numberOfThread(int queenNumber)
    {
        int result = 0;
        int[] queen = this.queenLocation[queenNumber];
       for (int[] otherQueen : queenLocation)
       {
           if (isQueenThreading(otherQueen,queen[0],queen[1]))
           {
               if (otherQueen != queen && (queen[0] != otherQueen[0] || queen[1] != otherQueen[1]) )
                   result++;
           }
       }

        return result;
    }

    private void setQueenToSafePlace(int queenNumber)
    {
        int[][] map = createThreadMap(queenNumber);

        int lessThread = Integer.MAX_VALUE;
        int row = -1;
        int col = -1;
        for (int i = 0; i < numberOfQueen;i++)
        {
            for (int j = 0; j < numberOfQueen; j++)
            {

                if (map[i][j] < lessThread || (lessThread == map[i][j] && (queenLocation[queenNumber][0] != i  || queenLocation[queenNumber][1] != j)))
                {
                    lessThread = map[i][j];
                    row = i;
                    col = j;
                }

            }
        }

        queenLocation[queenNumber][0] = row;
        queenLocation[queenNumber][1] = col;
    }

    private int[][] createThreadMap(int queenNumber)
    {
        int[][] map = new int[numberOfQueen][numberOfQueen];

        for (int i = 0; i < numberOfQueen;i++)
        {
            for (int j = 0; j < numberOfQueen;j++)
            {
                for (int q = 0 ;q <numberOfQueen;q++)
                {
                    if (q != queenNumber && (queenLocation[q][0] != i || queenLocation[q][1] != j) &&isQueenThreading(queenLocation[q],i,j ))
                    {
                        map[i][j] += 1;
                    }
                }
            }
        }

        for (int q = 0 ;q <numberOfQueen;q++)
        {
            if (q != queenNumber)
            {
                int[] other = queenLocation[q];
                map[other[0]][other[1]] = Integer.MAX_VALUE;
            }
        }

        return map;

    }

    private boolean isQueenThreading(int[] queen,int row , int col)
    {
        return ( queen[0] == row
                || queen[1] == col
                || ( ((double) (queen[0]-row) / (double)(queen[1]-col) )  == 1 )
                || ( ((double) (queen[0]-row) / (double)(queen[1]-col) )  == -1) ) ;
    }

    public void start()  {
        while (!allSafe())
        {
            int queen = nextQueenRandom();

            setQueenToSafePlace(queen);

        }

    }

    public static void main(String[] args) throws Exception {

        NQueen game = new NQueen(99);
        game.start();
        game.printGame();
        game.printLocation();

    }


}
