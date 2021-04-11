// main class is the driver code of the program
public class Main
{
    public static void main(String[] args)
    {
        // calls to UPGMA with file names to execute
        UPGMA HBB = new UPGMA("basic-HBB");
        UPGMA basicHBA = new UPGMA("basic-HBA");
        UPGMA relatedHBA = new UPGMA("related-HBA");
    }
}