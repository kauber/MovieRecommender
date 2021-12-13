import java.util.*;

public class MovieRunnerSimilarRatings {
    public void printAverageRatings(){
        FourthRatings moviesRated = new FourthRatings("ratings.csv");
        MovieDatabase.initialize("ratedmoviesfull.csv");
        ArrayList<Rating> ratingList = moviesRated.getAverageRatings(35);
        System.out.println("Found " + ratingList.size() + " movies");
        Collections.sort(ratingList);
        for (Rating rating : ratingList) {
            System.out.println(rating.getValue() + " " + MovieDatabase.getTitle(rating.getItem()));
        }
    }

    public void printAverageRatingsbyYearAfterAndGenre(){
        FourthRatings ratedMovies = new FourthRatings("data/ratings.csv");
        MovieDatabase.initialize("ratedmoviesfull.csv");

        AllFilters YearAfterAndGenre = new AllFilters();
        YearAfterAndGenre.addFilter(new YearAfterFilter(1990));
        YearAfterAndGenre.addFilter(new GenreFilter("Drama"));
        ArrayList<Rating> avgMovieRatings = ratedMovies.getAverageRatingsByFilter(8,YearAfterAndGenre);

        System.out.println("Found "+ avgMovieRatings.size() + " movies");

        Collections.sort(avgMovieRatings);

        for (Rating movie:avgMovieRatings){
            if (movie.getValue()>0){
                System.out.println(movie.getValue()+": "+ MovieDatabase.getTitle(movie.getItem()));
                System.out.println("Year: "+ MovieDatabase.getYear(movie.getItem()));
                System.out.println("Genres: "+ MovieDatabase.getGenres(movie.getItem()));
            }
        }

    }

    public void printSimilarRatings(){
        FourthRatings fr = new FourthRatings("ratings.csv");
        ArrayList<Rating> list = fr.getSimilarRatings("337",10,3);
        System.out.println("Found " + list.size() + " ratings");

        for (int i = 0; i < 3; i++) {
            //double val = list.get(i).getValue();
            System.out.println(list.get(i).getValue() + " " +  " " + MovieDatabase.getTitle(list.get(i).getItem()));

        }
    }

    public void printSimilarRatingsByGenre() {
        FourthRatings fr = new FourthRatings();
        GenreFilter gf = new GenreFilter("Mystery");
        ArrayList<Rating> list = fr.getSimilarRatingsByFilter("964", 20, 5, gf);
        System.out.println("Found " + list.size() + " ratings");
        for (int i = 0; i < 3; i++) {
            System.out.println(list.get(i).getValue() + " " +  " " +
                    MovieDatabase.getTitle(list.get(i).getItem()) + " " +
                    MovieDatabase.getGenres(list.get(i).getItem()));
        }

    }

    public void printSimilarRatingsByDirector() {
        FourthRatings fr = new FourthRatings();
        DirectorsFilter df = new DirectorsFilter("Clint Eastwood,J.J. Abrams,Alfred Hitchcock,Sydney Pollack,David Cronenberg,Oliver Stone,Mike Leigh");
        ArrayList<Rating> list = fr.getSimilarRatingsByFilter("1034", 10, 2, df);
        System.out.println("Found " + list.size() + " ratings");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getValue() + " " +  " " +
                    MovieDatabase.getTitle(list.get(i).getItem()) + " " +
                    MovieDatabase.getDirector(list.get(i).getItem()));
        }
    }


    public void printSimilarRatingsByGenreAndMinutes(){
        FourthRatings fr = new FourthRatings();
        AllFilters af = new AllFilters();
        af.addFilter(new GenreFilter("Drama"));
        af.addFilter(new MinutesFilter(80, 160));
        ArrayList<Rating> list = fr.getSimilarRatingsByFilter("168", 10, 3, af);
        System.out.println("Found " + list.size() + " ratings");
        for (int i = 0; i < 3; i++) {
            System.out.println(list.get(i).getValue() + " " +  " " +
                    MovieDatabase.getTitle(list.get(i).getItem()) + " " +
                    MovieDatabase.getMinutes(list.get(i).getItem()) + " " +
                    MovieDatabase.getGenres(list.get(i).getItem()));
        }
    }

    public void printSimilarRatingsByYearAfterAndMinutes(){
        FourthRatings fr = new FourthRatings();
        AllFilters af = new AllFilters();
        af.addFilter(new YearAfterFilter(1975));
        af.addFilter(new MinutesFilter(70, 200));
        ArrayList<Rating> list = fr.getSimilarRatingsByFilter("314", 10, 5, af);
        System.out.println("Found " + list.size() + " ratings");
        for (int i = 0; i < 3; i++) {
            System.out.println(list.get(i).getValue() + " " +  " " +
                    MovieDatabase.getTitle(list.get(i).getItem()) + " " +
                    MovieDatabase.getYear(list.get(i).getItem()) + " " +
                    MovieDatabase.getMinutes(list.get(i).getItem()));
        }
    }

}