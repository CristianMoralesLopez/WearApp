package com.example.myapplication;



public class CalcualdoraCalorias {


    public CalcualdoraCalorias (){

    }

    /**
     * Calculated the energy expenditure for an activity. Adapted from the following website https://sites.google.com/site/compendiumofphysicalactivities/corrected-mets
     *
     * @param height               The height in metres.
     * @param weight               The weight of the user.
     * @param gender               The gender of the user.
     * @param durationInSeconds    The duration of the activity in seconds.
     * @param stepsTaken           The steps taken.
     * @param strideLengthInMetres The stride length of the user
     * @return The number of calories burnt (kCal)
     */
    public static float calculateEnergyExpenditure(float height,  float weight, int gender, double durationInSeconds, int stepsTaken, float strideLengthInMetres) {

        float ageCalculated = 23;//getAgeFromDateOfBirth(age);

        float harrisBenedictRmR = convertKilocaloriesToMlKmin(harrisBenedictRmr(gender, weight, ageCalculated,   convertMetresToCentimetre(height)), weight);

        float kmTravelled = calculateDistanceTravelledInKM(stepsTaken, strideLengthInMetres);
        float hours = (float) (durationInSeconds / 3600);
        float milla = (float) 0.621371;
        float speedInMph = (kmTravelled * milla) / hours;
        float metValue = getMetForActivity(speedInMph);

        float constant = 3.5f;

        float correctedMets = metValue * (constant / harrisBenedictRmR);
        return correctedMets * hours * weight;
    }

    public static float convertKilocaloriesToMlKmin(float kilocalories, float weightKgs) {
        float kcalMin = kilocalories / 1440;
        kcalMin /= 5;

        return ((kcalMin / (weightKgs)) * 1000);
    }
    public static float convertMetresToCentimetre(float metres) {
        return metres * 100;
    }
    public static float calculateDistanceTravelledInKM(int stepsTaken, float entityStrideLength) {
        return (((float) stepsTaken * entityStrideLength) / 1000);
    }
    /**
     * Gets the MET value for an activity. Based on https://sites.google.com/site/compendiumofphysicalactivities/Activity-Categories/walking .
     *
     * @param speedInMph The speed in miles per hour
     * @return The met value.
     */
    private static float getMetForActivity(float speedInMph) {
        if (speedInMph < 2.0) {
            return 2.0f;
        } else if (Float.compare(speedInMph, 2.0f) == 0) {
            return 2.8f;
        } else if (Float.compare(speedInMph, 2.0f) > 0 && Float.compare(speedInMph, 2.7f) <= 0) {
            return 3.0f;
        } else if (Float.compare(speedInMph, 2.8f) > 0 && Float.compare(speedInMph, 3.3f) <= 0) {
            return 3.5f;
        } else if (Float.compare(speedInMph, 3.4f) > 0 && Float.compare(speedInMph, 3.5f) <= 0) {
            return 4.3f;
        } else if (Float.compare(speedInMph, 3.5f) > 0 && Float.compare(speedInMph, 4.0f) <= 0) {
            return 5.0f;
        } else if (Float.compare(speedInMph, 4.0f) > 0 && Float.compare(speedInMph, 4.5f) <= 0) {
            return 7.0f;
        } else if (Float.compare(speedInMph, 4.5f) > 0 && Float.compare(speedInMph, 5.0f) <= 0) {
            return 8.3f;
        } else if (Float.compare(speedInMph, 5.0f) > 0) {
            return 9.8f;
        }
        return 0;
    }

    /**
     * Calculates the Harris Benedict RMR value for an entity. Based on above calculation for Com
     *
     * @param gender   Users gender.
     * @param weightKg Weight in Kg.
     * @param age      Age in years.
     * @param heightCm Height in CM.
     * @return Harris benedictRMR value.
     */
    private static float harrisBenedictRmr(int gender, float weightKg, float age, float heightCm) {
        if (gender == 1) {
            return 655.0955f + (1.8496f * heightCm) + (9.5634f * weightKg) - (4.6756f * age);
        } else {
            return 66.4730f + (5.0033f * heightCm) + (13.7516f * weightKg) - (6.7550f * age);
        }

    }
}
