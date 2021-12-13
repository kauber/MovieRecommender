import edu.duke.*;
import java.util.*;
import org.apache.commons.csv.*;
import java.util.Map.Entry;

public class FirstRatings {

    public ArrayList<Movie> loadMovies(String filename) {
        ArrayList<Movie> myMovies = new ArrayList<Movie>();
        FileResource fr = new FileResource(filename);
        CSVParser parser = fr.getCSVParser();
        for (CSVRecord record : parser) {
            String id = record.get("id");
            String title = record.get("title");
            String year = record.get("year");
            String genres = record.get("genre");
            String director = record.get("director");
            String country = record.get("country");
            String poster = record.get("poster");
            int minutes = Integer.parseInt(record.get("minutes"));
            Movie myMovie = new Movie(id, title, year, genres, director, country, poster, minutes);
            myMovies.add(myMovie);
        }
        return myMovies;
    }

    public ArrayList<EfficientRater> loadRatersOld(String filename) {
        // this method returns a non aggregated version of the arraylist<Rater>
        ArrayList<EfficientRater> raterArray = new ArrayList<EfficientRater>();
        FileResource fr = new FileResource(filename);
        CSVParser parser = fr.getCSVParser();
        for (CSVRecord record : parser) {
            String raterId = record.get("rater_id");
            String movieId = record.get("movie_id");
            double rating = Double.parseDouble(record.get("rating"));
            System.out.println(rating);
            EfficientRater myRater = new EfficientRater(raterId);
            myRater.addRating(movieId, rating);
            //System.out.println(myRater.getRating(movieId));
            raterArray.add(myRater);
            //raterArray.add(myRater);
            //System.out.println(raterId);
        }
        return raterArray;
    }

    //helper method to determine whether the arraylist Rater already contains a rater
    public boolean isinRaters(EfficientRater rater, ArrayList<EfficientRater> raters) {
        for (EfficientRater er : raters) {
            if (er.getID().equals(rater.getID())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<EfficientRater> loadRaters(String filename) {
        ArrayList<EfficientRater> raters = new ArrayList<EfficientRater>();
        //HashMap<String,Rating> raters= new HashMap<String,Rating>();
        FileResource fr = new FileResource(filename);
        CSVParser parser = fr.getCSVParser();
        for (CSVRecord record : parser) {
            String raterId = record.get("rater_id");
            String movieId = record.get("movie_id");
            double rating = Double.parseDouble(record.get("rating"));

            EfficientRater er = new EfficientRater(raterId); // using helper method isinRaters to determine whether a rater is already
            // in the raters arraylist
            if (!isinRaters(er, raters)) {
                er.addRating(movieId, rating);
                raters.add(er);
            } else {
                for (EfficientRater r : raters) {
                    if (r.getID().equals(raterId)) {
                        r.addRating(movieId, rating);
                    }
                }
            }
        }
        //System.out.println("raters size: " + raters.size());
        return raters;
    }

    public void testLoadMovies() {
        ArrayList<Movie> theMovies = loadMovies("D:/Java_Projects/StepOneStarterProgram/data/ratedmoviesfull.csv");
        System.out.println("Number of movies in the file: " + theMovies.size());
        /*for (Movie myMovie: theMovies){
            System.out.println(myMovie);
        } */
        int comedyCount = 0;
        for (Movie myMovie : theMovies) {
            if (myMovie.getGenres().contains("Comedy")) {
                comedyCount += 1;
            }
        }
        System.out.println("Comedy movies in the file: " + comedyCount);

        int longerMovies = 0;
        for (Movie myMovie : theMovies) {
            if (myMovie.getMinutes() > 150) {
                longerMovies += 1;
            }
        }
        System.out.println("Movies longer than 150 minutes in the file: " + longerMovies);

        //maximum number of movies by any director

        HashMap<String, Integer> directors = new HashMap<String, Integer>();
        for (int k = 0; k < theMovies.size(); k++) {
            //int count = 0;
            if (!directors.containsKey(theMovies.get(k).getDirector())) {
                directors.put(theMovies.get(k).getDirector(), 1);
            } else {
                int currentCount = directors.get(theMovies.get(k).getDirector());
                currentCount++;
                directors.put(theMovies.get(k).getDirector(), currentCount);
            }
        }
        System.out.println(directors);

        int maxMoviesDirected = 0;
        for (String key : directors.keySet()) {
            int currentMovieCount = directors.get(key);
            if (currentMovieCount > maxMoviesDirected) {
                maxMoviesDirected = currentMovieCount;
            }
        }

        System.out.println("Max movies directed by director: " + maxMoviesDirected);
        for (Map.Entry<String, Integer> entry : directors.entrySet()) {
            if (Objects.equals(entry.getValue(), maxMoviesDirected)) {
                System.out.println("Director that directed most films: " + entry.getKey());
            }
        }
    }

    public void testLoadRaters() {
        ArrayList<EfficientRater> theRaters = loadRatersOld("D:/Java_Projects/StepOneStarterProgram/data/ratings_short.csv");
        // Print the total number of raters

        ArrayList<String> raters = new ArrayList<String>();
        for (EfficientRater r : theRaters) {
            if (!raters.contains(r.getID())) {
                raters.add(r.getID());
            }
        }

        System.out.println("Total number of raters: " + raters.size());
        //for each rater, print the rater’s ID and the number of ratings they did on one line,
        //followed by each rating (both the movie ID and the rating given) on a separate line.
        //If you run your program on the file ratings_short.csv you will see there are five raters.
        HashMap<String, Integer> raterRatings = new HashMap<String, Integer>();
        for (int k = 0; k < theRaters.size(); k++) {
            if (!raterRatings.containsKey(theRaters.get(k).getID())) {
                raterRatings.put(theRaters.get(k).getID(), 1);
            } else {
                int currentCount = raterRatings.get(theRaters.get(k).getID());
                currentCount++;
                raterRatings.put(theRaters.get(k).getID(), currentCount);
            }
        }
        System.out.println("Raters' ratings: " + raterRatings);

        String myRater = "193";

        for (String key : raterRatings.keySet()) {
            if (key.equals(myRater)) {
                System.out.println("Movies rated by " + myRater + ": " + raterRatings.get(key));
            }
        }

        // ITERATE through hashmap and print key, value plus movie and rating
        /*for (String key : raterRatings.keySet()) {
            System.out.println("Rater: " + key + " " + "Ratings: " + raterRatings.get(key));
            for (Rater rater : theRaters) {
            if (rater.getID().equals(key)) {
                ArrayList<String> ratings = rater.getItemsRated();
                for (String rating : ratings) {
                    System.out.print("movie_id: " + rating + " ");
                    System.out.println("Rating: " + rater.getRating(rating));
                }
            }
        }
        } */


        // look up specific rater and get all their data

        /*for (Rater rater : theRaters) {
            if (rater.getID().equals("2")) {
                System.out.println("Rater " + rater.getID() + " Ratings: " + rater.numRatings());
                ArrayList<String> ratings = rater.getItemsRated();
                for (String rating : ratings) {
                    System.out.print("movie_id: " + rating + " ");
                    System.out.println("Rating: " + rater.getRating(rating));
                }
            }
        }

        } */

        //TODO: last 3 bullet points of assignment

        // Add code to find the maximum number of ratings by any rater
        int maxRatings = 0;
        for (String key : raterRatings.keySet()) {
            int currentRatings = raterRatings.get(key);
            if (currentRatings > maxRatings) {
                maxRatings = currentRatings;
            }
        }

        System.out.println("Max number of ratings: " + maxRatings);
        ArrayList<String> maxRaters = new ArrayList<String>();
        // raterRatings is a hashmap, we need to get key by value now to know which rater has max number of ratings.
        // We will put all raters with maxRatings in a new arraylist

        for (Map.Entry<String, Integer> entry : raterRatings.entrySet()) {
            if (Objects.equals(entry.getValue(), maxRatings)) {
                maxRaters.add(entry.getKey());
            }
        }
        System.out.println("Max raters are: " + maxRaters);

        //Add code to find the number of ratings a particular movie has. Can we use the getItemsRated() method?
        // create a hashmap of movies and number of times it was rated.
        HashMap<ArrayList<String>, Integer> numMovieRatings = new HashMap<ArrayList<String>, Integer>();
        for (EfficientRater r : theRaters) {
            if (!numMovieRatings.containsKey(r.getItemsRated())) {
                numMovieRatings.put(r.getItemsRated(), 1);
            } else {
                int currentCount = numMovieRatings.get(r.getItemsRated());
                currentCount++;
                numMovieRatings.put(r.getItemsRated(), currentCount);
            }
        }

        // Use it to see ho many movies were rated:
        System.out.println("Total number of movies rated: " + numMovieRatings.size());

        // Get number of ratings for a specific movie:
        //ArrayList<String> moviesToLook =

        String movieToLookUp = "1798709";
        for (ArrayList<String> s : numMovieRatings.keySet()) {
            if (s.contains(movieToLookUp)) {
                System.out.println("Movie " + movieToLookUp + " has been rated " + numMovieRatings.get(s) + " times");
            }
        }

        // test getRating method
        //for (Rater rater : theRaters) {
        //System.out.println("Ratings for 1798709: " + rater.getRating(movieToLookUp));
        //}
    }

    public void testMyLoadRaters() {
        ArrayList<EfficientRater> theRaters = loadRaters("D:/Java_Projects/StepOneStarterProgram/data/ratings_short.csv");
        for (EfficientRater r : theRaters) {
            System.out.println(r.getID());
            System.out.println(r.getItemsRated());
            ArrayList<String> movies = r.getItemsRated();
            for (String movie : movies) {
                System.out.println(r.getRating(movie));
            }
        }
    }
}