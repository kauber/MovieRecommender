# Movie Recommender

### General Introduction

This a recommendation engine for movies created as part of the final assignment of the Coursera
specialization in Java Programming and Software Engineering.
The project has been refactored to be able to run without relying on helper packages provided specifically
for the specialization and not easily accessible for external users.

It uses rated movies information and ratings provided by raters to compute the best recommendations
based on a set of predetermined criteria.
The data used to generate the recommendations can be found [here](https://github.com/kauber/MovieRecommender/tree/main/data).

### Inner Workings

In order to come up with good recommendations for a user A, we cannot simply rely on average movie ratings.
This is because not all raters are equal: what we really want is to focus on raters who are similar to user A.
This idea of finding recommendations for a specific user, and not all users indistinctly, is called *collaborative filtering*.

Collaborative filtering implies weighting the averages differently, i.e. giving more importance to raters similar to me, based on their ratings.
So if rater B is more similar to me (in terms of preferences) than rater C is, then I should value the ratings of user B more than the ones
of rater C.

#### Calculating closeness

We can represent every rater as a vector of movie ratings.
<br>
Imagine Bruno rated 5 movies like so: [6,5,8,8,9], where 0 means no rating available.
<br/>
Pino rated the same 5 movies like so: [2,7,5,5,6].
<br>
I rated the 5 movies like so: [7,4,8,7,8]
<br/>
Clearly, here we can use a simple dot product and see that Bruno and my ratings
(6x7) + (5x4) + (8x8) + (8x7) + (9x8) = __254__ score higher than
(2x7) + (7x4) + (5x8) + (5x7) + (6x8) = __165__.
<br> We can therefore conclude that Bruno is more similar to me than Pino when it comes to 
movies.

#### Scaling the ratings ####

When comparing two raters, if two movies are scored, say, 1 and 2 or 8 and 9, the contribution to the similarity score should not vary. 
However that's not the case: 1 x 2 = 2 and 8 x 9 = 72. <br>
To normalize this, we scale the ratings by subtracting 5 to every rating. This will give us equal scores: (-4 x -3) = (3 x 4).
<br>
The code that actually computes dot products to estimate raters most similar to a given one is as follows:
```
private double dotProduct(Rater me, Rater r){
        double dotProd = 0.0;
        for (String movie: me.getItemsRated()){
            if ((me.getRating(movie) != -1) && (r.getRating(movie) != -1))
            {
                double currProd = (me.getRating(movie) - 5) * (r.getRating(movie) -5);
                dotProd += currProd;
     
            }
        }
        return dotProd;
    }

```

This method is called in the method *getSimilarities*, to return a list of raters who are
most similar to a rater with a particular id.

```
private ArrayList<Rating> getSimilarities(String id){
        ArrayList<Rating> list = new ArrayList<Rating>();
        Rater me = RaterDatabase.getRater(id);
        for (Rater rater: RaterDatabase.getRaters()){
            if (!rater.getID().equals(me.getID())){
                double dotProd = dotProduct(rater,me);
                list.add(new Rating(rater.getID(), dotProd));
            }
            //add dot_product(rater,me) to list if rater!=me
        }
        Collections.sort(list, Collections.reverseOrder());
        return list;
        
  ```


The scores obtained above are again used to generate recommendations using the below method, 
*getSimilarRatings*:

```
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
```

This method multiplies the scores of, say, the 10 most similar raters to myself, with movie ratings,
and returns a list of movies with the highest scores, i.e. the actual movie recommendations.

### How to run the recommendation engine 

From the [movieRunnerSimilarRatings](https://github.com/kauber/MovieRecommender/blob/main/src/MovieRunnerSimilarRatings.java) class,
we can run the *main* method.

The class contains other public static methods that can be called from *main* to, for instance, come up
with recommendations given particular filters.

