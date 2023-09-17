package com.pocolifo.holiday;

import org.wikidata.wdtk.datamodel.interfaces.*;

public class Utility {
    public static final String[] NUMBER_ABBREVIATIONS = {
            "",
            "K",
            "M",
            "B" // max is a billion because we're retrieving social profile follower counts as ints which max at 2^31-1
    };

    public static float roundToNearestHundredthPercentage(float n) {
        return Math.round(n * 10000) / 100f;
    }

    public static float roundToNearestHundredth(float n) {
        return Math.round(n * 100) / 100f;
    }

    public static float roundToNearestTenth(double n) {
        return Math.round(n * 10) / 10f;
    }

    public static String formatNumber(double n) {
        return String.format("%,f", n);
    }

    public static String formatNumber(long n) {
        return String.format("%,d", n);
    }

    public static String shortenNumber(double n) {
        int shortenLevel = 0;

        while (n / 1000 > 1 && NUMBER_ABBREVIATIONS.length > shortenLevel) {
            n /= 1000d;
            shortenLevel++;
        }

        return roundToNearestTenth(n) + NUMBER_ABBREVIATIONS[shortenLevel];
    }

    public static String shortenString(String str, int len) {
        return str.length() > len ? str.substring(0, len) + "..." : str;
    }

    public static class DefaultSnakVisitor<T> implements SnakVisitor<T> {
        @Override
        public T visit(ValueSnak snak) {
            return null;
        }

        @Override
        public T visit(SomeValueSnak snak) {
            return null;
        }

        @Override
        public T visit(NoValueSnak snak) {
            return null;
        }
    }

    public static class DefaultValueVisitor<T> implements ValueVisitor<T> {
        @Override
        public T visit(EntityIdValue value) {
            return null;
        }

        @Override
        public T visit(GlobeCoordinatesValue value) {
            return null;
        }

        @Override
        public T visit(MonolingualTextValue value) {
            return null;
        }

        @Override
        public T visit(QuantityValue value) {
            return null;
        }

        @Override
        public T visit(StringValue value) {
            return null;
        }

        @Override
        public T visit(TimeValue value) {
            return null;
        }

        @Override
        public T visit(UnsupportedValue value) {
            return null;
        }
    }
}
