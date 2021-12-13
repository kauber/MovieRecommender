import java.util.*;

public class RecommendationRunner implements Recommender {
    public ArrayList<String> getItemsToRate(){
        ArrayList<String> moviesToRate = new ArrayList<>();
        ArrayList<String> movies = MovieDatabase.filterBy(new TrueFilter());
        int numMovies = 0;
        while (numMovies < 20){
            Random rand = new Random();
            int random = rand.nextInt(movies.size());
            if (!moviesToRate.contains(movies.get(random))){
                moviesToRate.add(movies.get(random));
            }
            numMovies ++;
            //System.out.println(numMovies);
        }
        return moviesToRate;
    }

    public void printRecommendationsFor(String webRaterID){
        FourthRatings fr = new FourthRatings();
        ArrayList<Rating> list = fr.getSimilarRatings(webRaterID,5,1);
        if(list.size()==0){
            System.out.println("No recommendations available");
        }

        else{

            for (Rating r: list){
                String URL = MovieDatabase.getPoster(r.getItem());
                //System.out.println(URL);
                String title = MovieDatabase.getTitle(r.getItem());
                String director = MovieDatabase.getDirector(r.getItem());
                String country = MovieDatabase.getCountry(r.getItem());
                int year = MovieDatabase.getYear(r.getItem());
                String genre = MovieDatabase.getGenres(r.getItem());
                int minutes = MovieDatabase.getMinutes(r.getItem());

                System.out.println("<td><table><tr><td class = \"pic\">");

                if(URL.length()>3){
                    System.out.println("<img src = \""+URL+"\" target=_blank></td>");
                }

                System.out.println("<td><h3>"+ title +"</h3>");
                System.out.println("<b>by "+ director +"</b><br>");
                System.out.println(genre +"<br>");
                System.out.println(year +"<br>");
                System.out.println(country +"<br>");
                System.out.println(minutes +" minutes</td></tr></table></td></tr>");
            }
        }
    }
    public void testGetTimesToRate(){
        ArrayList<String> myItems = getItemsToRate();
        for (String s: myItems){
            System.out.println(s);
        }
    }

    public void testPrintRec(){
        printRecommendationsFor("51");
    }


}
