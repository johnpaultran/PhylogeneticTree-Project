// class to store data for any given index within the distance array
// data for each index includes coordinates, distance, and cluster
public class Index
{
    // instance variables for data in each index
    int row;
    int column;
    Double distance;
    String cluster;
    boolean header;

    // empty constructor
    public Index() {}

    // constructor for an index with its value initialized to 0
    public Index(int row, int column, String cluster, boolean header)
    {
        this.row = row;
        this.column = column;
        distance = 0.0;
        this.cluster = cluster;
        this.header = header;
    }

    // constructor for an index in distance matrix
    public Index(int row, int column, Double distance, String cluster, boolean header)
    {
        this.row = row;
        this.column = column;
        this.distance = distance;
        this.cluster = cluster;
        this.header = header;
    }

    // following are get methods to retrieve the indicated values
    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public Double getDistance()
    {
        return distance;
    }

    public String getCluster()
    {
        return cluster;
    }

    // following are set methods to set each value
    public void setRow(int col)
    {
        this.row = col;
    }

    public void setColumn(int column)
    {
        this.column = column;
    }

    public void setCluster(String clusterIn)
    {
        cluster = clusterIn;
    }

    public void setDistance(Double distance)
    {
        this.distance = distance;
    }
}