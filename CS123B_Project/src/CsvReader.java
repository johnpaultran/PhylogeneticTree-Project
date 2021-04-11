import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

// class to read in CSV file for UPGMA algorithm
public class CsvReader
{
    private ArrayList<ArrayList<Index>> distanceMatrix; // to hold distance matrix

    // constructor for CSV Reader and creates new array list
    public CsvReader()
    {
        distanceMatrix = new ArrayList<ArrayList<Index>>();
    }

    // function to read distance matrix from CSV file and store it into array list of array lists of Indexes
    public ArrayList<ArrayList<Index>> CsvReader(String csvFile) throws FileNotFoundException
    {
        File file = new File(csvFile+".csv");
        int columnNum = 0;     // initialize column number to 0

        try (Scanner scanner = new Scanner(file, "UTF-8")) // scan CSV file
        {
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                distanceMatrix.add(readLine(line, columnNum));  // add line to distance matrix
                columnNum++;                                    // increment the column number
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return distanceMatrix;  // return the distance matrix
    }

    // helper function to split a line from the distance matrix into array list of Indexes
    private ArrayList<Index> readLine(String line, int columnNum)
    {
        ArrayList<Index> indexArrayList = new ArrayList<Index>();   // array list to be returned
        int rowNum = 0;         // initialize row number to 0
        Index newIndex;         // new Index to be added to the array list

        try (Scanner scanner = new Scanner(line))
        {
            scanner.useDelimiter(",");      // delimit commas from CSV file
            while (scanner.hasNext())
            {
                String nextLine = scanner.next();
                // if first index, then set header to true
                if(rowNum == 0 || columnNum == 0)
                {
                    newIndex = new Index(rowNum, columnNum, nextLine, true);
                }
                else
                {
                    String cluster = "(" + distanceMatrix.get(0).get(rowNum).getCluster() + " " + distanceMatrix.get(0).get(columnNum).getCluster() + ")";
                    newIndex = new Index(rowNum, columnNum, Double.parseDouble(nextLine), cluster,false);
                }

                indexArrayList.add(newIndex);   // add new index to the array list
                rowNum++;                       // increment the row number
            }
        }
        return indexArrayList;  // return the array list
    }
}