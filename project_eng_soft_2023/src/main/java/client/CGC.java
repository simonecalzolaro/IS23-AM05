package client;

public enum CGC {

    CGC1("Six groups each containing at least\n" +
            "2 tiles of the same type (not necessarily\n"+
            "in the depicted shape).\n" +
            "The tiles of one group can be different\n" +
            "from those of another group.\n"),

    CGC2("Four groups each containing at least\n" +
            "4 tiles of the same type (not necessarily\n" +
            "in the depicted shape).\n" +
            "The tiles of one group can be different\n" +
            "from those of another group."),

    CGC3("Four tiles of the same type in the four\n" +
            "corners of the bookshelf."),

    CGC4("Two groups each containing 4 tiles of\n" +
            "the same type in a 2x2 square. The tiles\n" +
            "of one square can be different from\n" +
            "those of the other square."),

    CGC5("Three columns each formed by 6 tiles\n"+
            "column can show the same or a different\n" +
            "combination of another column."),

    CGC6("Eight tiles of the same type. There’s no\n" +
            "restriction about the position of these\n" +
            "tiles."),

    CGC7("Five tiles of the same type forming a\n" +
            "diagonal."),

    CGC8("Four lines each formed by 5 tiles of\n" +
            "maximum three different types. One\n" +
            "line can show the same or a different\n" +
            "combination of another line."),

    CGC9("Two columns each formed by 6\n" +
            "different types of tiles."),

    CGC10("Two lines each formed by 5 different\n" +
            "types of tiles. One line can show the\n" +
            "same or a different combination of the\n" +
            "other line."),

    CGC11("Five tiles of the same type forming an X."),

    CGC12("Five columns of increasing or decreasing\n" +
            "height. Starting from the first column on\n" +
            "the left or on the right, each next column\n" +
            "must be made of exactly one more tile.\n" +
            "Tiles can be of any type.");

    private final String description;

    CGC(String s) {
        this.description=s;
    }

    public String getDescription(){
        return description;
    }

    public static CGC getCGC(int cgc1Num){

        switch (cgc1Num % 12) {
            case 1 : return CGC.CGC1;
            case 2 : return CGC.CGC2;
            case 3 : return CGC.CGC3;
            case 4 : return CGC.CGC4;
            case 5 : return CGC.CGC5;
            case 6 : return CGC.CGC6;
            case 7 : return CGC.CGC7;
            case 8 : return CGC.CGC8;
            case 9 : return CGC.CGC9;
            case 10 : return CGC.CGC10;
            case 11 : return CGC.CGC11;
            case 12 : return CGC.CGC12;
            default: return CGC.CGC1;
        }
    }


    @Override
    public String toString() {
        return super.toString();
    }


}
