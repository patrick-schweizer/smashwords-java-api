package com.unleashyouradventure.swapi.retriever;

public class BookCategoryRetriever {

    private final static BookCategory cat1;

    static {
        // Generated start
        cat1 = new BookCategory(1, "All");
        BookCategory cat3 = new BookCategory(3, "Fiction");
        cat1.addChild(cat3);
        BookCategory cat892 = new BookCategory(892, "Adventure");
        cat3.addChild(cat892);
        BookCategory cat1248 = new BookCategory(1248, "African American");
        cat3.addChild(cat1248);
        BookCategory cat62 = new BookCategory(62, "Anthologies");
        cat3.addChild(cat62);
        BookCategory cat61 = new BookCategory(61, "Children's Books");
        cat3.addChild(cat61);
        BookCategory cat1223 = new BookCategory(1223, "Christian");
        cat3.addChild(cat1223);
        BookCategory cat60 = new BookCategory(60, "Drama");
        cat3.addChild(cat60);
        BookCategory cat1206 = new BookCategory(1206, "Fantasy");
        cat3.addChild(cat1206);
        BookCategory cat886 = new BookCategory(886, "Gay & Lesbian");
        cat3.addChild(cat886);
        BookCategory cat58 = new BookCategory(58, "Graphic Novels/Comics");
        cat3.addChild(cat58);
        BookCategory cat884 = new BookCategory(884, "Historical");
        cat3.addChild(cat884);
        BookCategory cat1067 = new BookCategory(1067, "Holiday");
        cat3.addChild(cat1067);
        BookCategory cat883 = new BookCategory(883, "Horror");
        cat3.addChild(cat883);
        BookCategory cat882 = new BookCategory(882, "Humor & Comedy");
        cat3.addChild(cat882);
        BookCategory cat881 = new BookCategory(881, "Literary");
        cat3.addChild(cat881);
        BookCategory cat879 = new BookCategory(879, "Mystery & Detective");
        cat3.addChild(cat879);
        BookCategory cat56 = new BookCategory(56, "Poetry");
        cat3.addChild(cat56);
        BookCategory cat1235 = new BookCategory(1235, "Romance");
        cat3.addChild(cat1235);
        BookCategory cat1213 = new BookCategory(1213, "Science Fiction");
        cat3.addChild(cat1213);
        BookCategory cat1126 = new BookCategory(1126, "Sports");
        cat3.addChild(cat1126);
        BookCategory cat874 = new BookCategory(874, "Thriller & Suspense");
        cat3.addChild(cat874);
        BookCategory cat871 = new BookCategory(871, "Western");
        cat3.addChild(cat871);
        BookCategory cat870 = new BookCategory(870, "Women's Fiction");
        cat3.addChild(cat870);
        BookCategory cat1018 = new BookCategory(1018, "Young Adult/Teen");
        cat3.addChild(cat1018);
        BookCategory cat4 = new BookCategory(4, "Non-Fiction");
        cat1.addChild(cat4);
        BookCategory cat94 = new BookCategory(94, "Biography");
        cat4.addChild(cat94);
        BookCategory cat93 = new BookCategory(93, "Business & Economics");
        cat4.addChild(cat93);
        BookCategory cat91 = new BookCategory(91, "Childrens' books");
        cat4.addChild(cat91);
        BookCategory cat89 = new BookCategory(89, "Cooking");
        cat4.addChild(cat89);
        BookCategory cat87 = new BookCategory(87, "Entertainment");
        cat4.addChild(cat87);
        BookCategory cat85 = new BookCategory(85, "Health & Wellbeing");
        cat4.addChild(cat85);
        BookCategory cat84 = new BookCategory(84, "History");
        cat4.addChild(cat84);
        BookCategory cat82 = new BookCategory(82, "Inspiration");
        cat4.addChild(cat82);
        BookCategory cat77 = new BookCategory(77, "Parenting");
        cat4.addChild(cat77);
        BookCategory cat73 = new BookCategory(73, "Reference");
        cat4.addChild(cat73);
        BookCategory cat71 = new BookCategory(71, "Religion");
        cat4.addChild(cat71);
        BookCategory cat69 = new BookCategory(69, "Self Improvement");
        cat4.addChild(cat69);
        BookCategory cat66 = new BookCategory(66, "Sports");
        cat4.addChild(cat66);
        BookCategory cat64 = new BookCategory(64, "Travel");
        cat4.addChild(cat64);

        // Generated end
    }

    public static BookCategory getRootCategory() {
        return cat1;
    }

}
