public class TourGuide {
    public static int getBestSightseeingPairScore(int[] places){
        if (places == null) {
            return -1;
        }
        int sum,temp;
        if (places.length>1){
        sum=places[0]+places[1]-1;}
        else {return -1;}
        for (int i = 0; i < places.length; i++) {
            for (int j = i+1; j < places.length; j++) {
                temp=places[i] + places[j] + i - j;
                if (temp>sum) {
                    sum=temp;
                }
            }
        }
        return sum;
    }
    public static void main(String[] args) {
        System.out.println(getBestSightseeingPairScore(new int[]{8, 1, 5, 2, 6}));
    }
}