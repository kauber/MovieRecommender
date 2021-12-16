//import edu.duke.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
//import org.apache.commons.csv.*;

public class RaterDatabase {
    private static HashMap<String,Rater> ourRaters;

    private static void initialize() {
        // this method is only called from addRatings
        if (ourRaters == null) {
            ourRaters = new HashMap<String,Rater>();
        }
    }

    public static void initialize(String filename) {
        if (ourRaters == null) {
            ourRaters= new HashMap<String,Rater>();
            addRatings("data/" + filename);
        }
    }

    public static void addRatings(String filename) {
        initialize();

        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                //List<String> lineData = Arrays.asList(line.split(","));
                String[] columns = line.split(",");
                String id = columns[0];
                String item = columns[1];
                String rating = columns[2];
                addRaterRating(id,item,Double.parseDouble(rating));
            }
        } catch (IOException e) {
            System.out.println("File not Found");
//            e.printStackTrace();


        }

//        FileResource fr = new FileResource(filename);
//        CSVParser csvp = fr.getCSVParser();
//        for(CSVRecord rec : csvp) {
//            String id = rec.get("rater_id");
//            String item = rec.get("movie_id");
//            String rating = rec.get("rating");
//            addRaterRating(id,item,Double.parseDouble(rating));
//        }
    }

    public static void addRaterRating(String raterID, String movieID, double rating) {
        initialize();
        Rater rater =  null;
        if (ourRaters.containsKey(raterID)) {
            rater = ourRaters.get(raterID);
        }
        else {
            rater = new EfficientRater(raterID);
            ourRaters.put(raterID,rater);
        }
        rater.addRating(movieID,rating);
    }

    public static Rater getRater(String id) {
        initialize();

        return ourRaters.get(id);
    }

    public static ArrayList<Rater> getRaters() {
        initialize();
        ArrayList<Rater> list = new ArrayList<Rater>(ourRaters.values());

        return list;
    }

    public static int size() {
        return ourRaters.size();
    }

}
