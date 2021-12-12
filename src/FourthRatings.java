import java.util.*;

public class FourthRatings {

    public FourthRatings() {
        // default constructor
        this("ratings.csv");
    }

    public FourthRatings(String ratingsfile){
        //FourthRatings ratedMovies = new FourthRatings();
        RaterDatabase.initialize(ratingsfile);
        //myRaters = fr.loadRaters(ratingsfile);
    }

    private double getAverageByID(String id, int minimalRaters){
        double rating = 0.0;
        int raterCount = 0;
        for (Rater rater: RaterDatabase.getRaters()){
            //System.out.println(r.getID());
            //System.out.println(r.getItemsRated());
            ArrayList<String> movies = rater.getItemsRated();
            for (String movie: movies){
                if (movie.equals(id)){
                    rating += rater.getRating(movie);
                    raterCount ++;
                }
            }
        }
        if (raterCount >= minimalRaters){
            double avgRating = rating/raterCount;
            return avgRating;
        }
        return 0.0;
    }

    public ArrayList<Rating> getAverageRatings(int minimalRaters){
        ArrayList<String> movies = MovieDatabase.filterBy(new TrueFilter());
        ArrayList<Rating> avgRatings = new ArrayList<Rating>();
        for (String movie: movies){
            //String currMovieID = m.getID();
            double avgRating = getAverageByID(movie, minimalRaters);
            if (avgRating>0.0){
                avgRatings.add(new Rating(movie, avgRating));
            }
        }
        return avgRatings;
    }

    public ArrayList<Rating> getAverageRatingsByFilter(int minimalRaters, Filter filterCriteria){
        ArrayList<Rating> ratings = new ArrayList<Rating>();
        Filter trueF = new TrueFilter();
        ArrayList<String> movies = MovieDatabase.filterBy(filterCriteria);
        for (String movie : movies) {
            if (trueF.satisfies(movie)) {
                double avgRating = getAverageByID(movie, minimalRaters);
                if (avgRating > 0.0)
                    ratings.add(new Rating(movie, avgRating));
            }
        }
        return ratings;
    }

    private double dotProduct(Rater me, Rater r){
        double dotProd = 0.0;
        for (String movie: me.getItemsRated()){
            if ((me.getRating(movie) != -1) && (r.getRating(movie) != -1)){
                //System.out.println("movie matched: " + movie);
                double currProd = (me.getRating(movie) - 5) * (r.getRating(movie) -5);
                dotProd += currProd;
                //System.out.println("dot prod for: " + movie + " " + dotProd);
            }
        }
        return dotProd;
    }

    private ArrayList<Rating> getSimilarities(String id){
        //this method computes a similarity rating for each rater in the RaterDatabase
        //(except the rater with the ID given by the parameter) to see how similar they are to the Rater whose ID
        //is the parameter to getSimilarities. This method returns an ArrayList
        //of type Rating sorted by ratings from highest to lowest rating with the highest rating first
        //and only including those raters who have a positive similarity rating since those with negative
        //values are not similar in any way. Note that in each Rating object the item field is a rater’s ID,
        //and the value field is the dot product comparison between that rater and the rater whose
        //ID is the parameter to getSimilarities. Be sure not to use the dotProduct method with parameter id and itself!
        ArrayList<Rating> list = new ArrayList<Rating>();
        Rater me = RaterDatabase.getRater(id);
        for (Rater rater: RaterDatabase.getRaters()){
            //System.out.println(rater);
            if (!rater.getID().equals(me.getID())){
                double dotProd = dotProduct(rater,me);
                list.add(new Rating(rater.getID(), dotProd));
            }
            //add dot_product(rater,me) to list if rater!=me
        }
        Collections.sort(list, Collections.reverseOrder());
        return list;
    }

    public ArrayList<Rating> getSimilarRatings(String id, int numSimilarRaters, int minimalRaters){
        ArrayList<Rating> list = new ArrayList<>();
        Filter trueFilter = new TrueFilter();
        for (String movie : MovieDatabase.filterBy(trueFilter)) {
            double avg = 0.0;
            ArrayList<Rating> similaritiesList = getSimilarities(id);
            int count = 0;
            double total = 0.0;
            int weightedSimilaritiesTot = 0;
            for (int i = 0; i < numSimilarRaters; i++) {
                double rating = RaterDatabase.getRater(similaritiesList.get(i).getItem()).getRating(movie);
                if (rating != -1) {
                    count++;
                    total += (rating * similaritiesList.get(i).getValue());
                    weightedSimilaritiesTot += similaritiesList.get(i).getValue();
                }
            }
            if (count >= minimalRaters)
                avg = (total / count);
            if (avg > 0)
                list.add(new Rating(movie, avg));
        }
        Collections.sort(list, Collections.reverseOrder());
        return list;
    }

    public ArrayList<Rating> getSimilarRatingsByFilter (String id, int numSimilarRaters, int minimalRaters, Filter filterCriteria){
        ArrayList<Rating> list = new ArrayList<>();
        Filter trueFilter = new TrueFilter();
        //ArrayList<String> movies = MovieDatabase.filterBy(filterCriteria);
        for (String movie : MovieDatabase.filterBy(filterCriteria)) {
            double avg = 0.0;
            ArrayList<Rating> similaritiesList = getSimilarities(id);
            int count = 0;
            double total = 0.0;
            int weightedSimilaritiesTot = 0;
            for (int i = 0; i < numSimilarRaters; i++) {
                double rating = RaterDatabase.getRater(similaritiesList.get(i).getItem()).getRating(movie);
                if (rating != -1) {
                    count++;
                    total += (rating * similaritiesList.get(i).getValue());
                    weightedSimilaritiesTot += similaritiesList.get(i).getValue();
                }
            }
            if (count >= minimalRaters)
                avg = (total / count);
            if (avg > 0)
                list.add(new Rating(movie, avg));
        }
        Collections.sort(list, Collections.reverseOrder());
        return list;
    }

    public void testDotProd(){
        Rater me = RaterDatabase.getRater("1");
        Rater r = RaterDatabase.getRater("3");
        System.out.println(dotProduct(me, r));
    }

    public void testGetSimilarities(){
        ArrayList<Rating> letsSee = getSimilarities("1");
        for (Rating r: letsSee){
            System.out.println(r);
        }
    }


}
