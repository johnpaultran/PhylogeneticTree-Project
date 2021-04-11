import java.io.FileNotFoundException;
import java.util.ArrayList;

// class to create line representation of phylo tree using unweighted pair group method with arithmetic mean
public class UPGMA
{
    // instance variables
    String file;
    CsvReader csvReader;
    int step;
    ArrayList<ArrayList<Index>> distanceMatrix = new ArrayList<ArrayList<Index>>();
    Index minimum;
    String phyloTree;

    // creates line representation of the tree from given CSV file
    public UPGMA(String csvFile)
    {
        try
        {
            // initialize instance variables
            file = csvFile;
            csvReader = new CsvReader();
            step = 0;
            distanceMatrix = csvReader.CsvReader(file);

            // call to UPGMA functions
            printDistanceMatrix(step);
            runUPGMA();
            phyloTree = minimum.getCluster();

            System.out.println("\nLine Representation of "+ file + " Phylogenetic Tree: "+ phyloTree);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    // function to print the current distance matrix for each step of UPGMA
    private void printDistanceMatrix(int step)
    {
        System.out.println("\nStep " + step + ":"); // print current step
        for(ArrayList<Index> i : distanceMatrix)    // for each array list in distance matrix
        {
            for(Index j : i)                        // for each Index in array list
            {
                if(j.header) {
                    System.out.printf("%-10s", j.getCluster());     // print out header of distance matrix
                }
                else {
                    System.out.printf("%-10.4f", j.getDistance());  // print out distance in each index
                }
            }
            System.out.println();   // new line for clarity
        }
    }

    // function to run UPGMA until matrix is down to only one row of distance values
    public void runUPGMA()
    {
        while(distanceMatrix.size() > 2)
        {
            findMinimum();
            executeUPGMA();
        }
    }

    // function to find the index of the minimum distance in the matrix
    public Index findMinimum()
    {
        Index current;              // variable for current index in distance matrix
        Index min = null;           // variable for the minimum distance to be returned

        if(distanceMatrix.size() > 2)
        {
            // iterate through array list
            for(int column = 1; column < distanceMatrix.size(); column++)
            {
                for(int row = column + 1; row < distanceMatrix.size(); row++)
                {
                    current = distanceMatrix.get(column).get(row);                  // get current index
                    if(min == null || current.getDistance() < min.getDistance())    // check for minimum distance
                    {
                        min = current;  // if current is smaller than minimum, set current to minimum
                    }
                }
            }
        }

        minimum = min;  // set minimum
        return min;     // return the minimum
    }

    // function to execute each step of UPGMA by combining clusters based on minimum distance
    public void executeUPGMA()
    {
        ArrayList<ArrayList<Index>> newDistanceMatrix = new ArrayList<ArrayList<Index>>();  // new matrix from each step
        Index tempIndex;    // variable for Index manipulation
        String newCluster;  // new cluster to be put into distance matrix

        // create new distance matrix 1 size smaller
        for(int i = 0; i < distanceMatrix.size() - 1; i++)
        {
            newDistanceMatrix.add(new ArrayList<Index>());
            for(int j = 0; j < distanceMatrix.size() - 1; j++)
            {
                newDistanceMatrix.get(i).add(new Index());
            }
        }

        // iterate through distance matrix
        for(int column = 0, newColumnNum = 0; column <= distanceMatrix.size(); column++)
        {
            while(column == minimum.getColumn() || column == minimum.getRow())
            {
                column++;
            }
            for(int row = 0, newRowNum = 0; row <= distanceMatrix.size(); row++)
            {
                while(row == minimum.getRow() || row == minimum.getColumn())
                {
                    row++;
                }

                // horizontal clusters
                if(column == 0)
                {
                    if(row != minimum.getRow() && row != minimum.getColumn() && row <= newDistanceMatrix.size())
                    {
                        // copy from initial distance matrix
                        newCluster = distanceMatrix.get(column).get(row).getCluster();
                        tempIndex = new Index(newRowNum,newColumnNum,newCluster,true);
                        newDistanceMatrix.get(0).set(newRowNum, tempIndex);
                        newRowNum++;  // increment new row number
                    }
                    else
                    {
                        // create new horizontal cluster
                        newCluster = "(" + distanceMatrix.get(0).get(minimum.getRow()).getCluster() + " " + distanceMatrix.get(0).get(minimum.getColumn()).getCluster() + ")";
                        tempIndex = new Index(newRowNum, newColumnNum, newCluster,true);
                        newDistanceMatrix.get(0).set(newRowNum, tempIndex);
                        newRowNum++;    // increment new row number
                        newColumnNum++; // increment new column number
                    }
                }
                // vertical clusters
                else if(row == 0 && column > 0)
                {
                    newCluster = newDistanceMatrix.get(0).get(newColumnNum).getCluster();
                    tempIndex = new Index(newRowNum, newColumnNum, newCluster,true);
                    newDistanceMatrix.get(newColumnNum).set(0, tempIndex);
                    newRowNum++;    // increment new row number
                }
                else if(column >= row)
                {
                    tempIndex = new Index(newRowNum,newColumnNum,0.0,null,false);
                    newDistanceMatrix.get(newColumnNum).set(newRowNum, tempIndex);
                    newRowNum++;    // increment new row number
                }
                else if(column <= newDistanceMatrix.size() && row <= newDistanceMatrix.size()) //copy
                {
                    newCluster = newDistanceMatrix.get(0).get(newRowNum).getCluster() + newDistanceMatrix.get(0).get(newColumnNum).getCluster();
                    tempIndex = new Index(newRowNum, newColumnNum, distanceMatrix.get(column).get(row).getDistance(), newCluster, false);
                    newDistanceMatrix.get(newColumnNum).set(newRowNum, tempIndex);
                    newRowNum++;    // increment new row number
                }
                else if(row > newDistanceMatrix.size() && column <= newDistanceMatrix.size() && column != 0)
                {
                    // compute new cluster row
                    Double distance1 = distanceMatrix.get(column).get(minimum.getRow()).getDistance();
                    Double distance2 = distanceMatrix.get(minimum.getColumn()).get(column).getDistance();
                    Double distance3 = distanceMatrix.get(minimum.getRow()).get(column).getDistance();
                    Double distance4 = distanceMatrix.get(column).get(minimum.getColumn()).getDistance();
                    // calculate arithmetic mean
                    Double newDistance = (distance1 + distance2 + distance3 + distance4) / 2;
                    newCluster = "(" +newDistanceMatrix.get(0).get(newRowNum).getCluster() + " " + newDistanceMatrix.get(0).get(newColumnNum).getCluster() + ")";
                    tempIndex = new Index(newDistanceMatrix.size() - 1, newColumnNum, newDistance, newCluster, false);
                    // set new values into new distance matrix
                    newDistanceMatrix.get(newColumnNum).set(newDistanceMatrix.size() - 1, tempIndex);
                    newRowNum++;        // increment new row number
                    newColumnNum++;     // increment new column number
                }
            }
            // new cluster
            if(column == distanceMatrix.size())
            {
                newCluster = newDistanceMatrix.get(0).get(newDistanceMatrix.size() - 1).getCluster();
                tempIndex = new Index(distanceMatrix.size() - 2, distanceMatrix.size() - 2, 0.0, newCluster, false);
                // set new values into new distance matrix
                newDistanceMatrix.get(distanceMatrix.size() - 2).set(distanceMatrix.size() - 2, tempIndex);
                newColumnNum++;         // increment new column number
            }
        }

        distanceMatrix = newDistanceMatrix; // set distanceMatrix to newDistanceMatrix that has shrunk by 1
        ++step;                             // increment step

        printDistanceMatrix(step);          // print out current distance matrix
    }
}