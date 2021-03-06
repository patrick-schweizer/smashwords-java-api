package com.unleashyouradventure.swapi.retriever;

public final class BookCategoryRetriever {

    private static final BookCategory cat1;

    static {
        // Generated start

        cat1 = new BookCategory(1, "All");
        BookCategory cat3 = new BookCategory(3, "Fiction");
        cat1.addChild(cat3);
        BookCategory cat892 = new BookCategory(892, "Adventure");
        cat3.addChild(cat892);
        BookCategory cat1248 = new BookCategory(1248, "African American fiction");
        cat3.addChild(cat1248);
        BookCategory cat1106 = new BookCategory(1106, "Alternative history");
        cat3.addChild(cat1106);
        BookCategory cat62 = new BookCategory(62, "Anthologies");
        cat3.addChild(cat62);
        BookCategory cat1105 = new BookCategory(1105, "Biographical");
        cat3.addChild(cat1105);
        BookCategory cat895 = new BookCategory(895, "Business");
        cat3.addChild(cat895);
        BookCategory cat61 = new BookCategory(61, "Children’s books");
        cat3.addChild(cat61);
        BookCategory cat1223 = new BookCategory(1223, "Christian");
        cat3.addChild(cat1223);
        BookCategory cat1348 = new BookCategory(1348, "Classics");
        cat3.addChild(cat1348);
        BookCategory cat1095 = new BookCategory(1095, "Coming of age");
        cat3.addChild(cat1095);
        BookCategory cat1332 = new BookCategory(1332, "Cultural & ethnic themes");
        cat3.addChild(cat1332);
        BookCategory cat896 = new BookCategory(896, "Educational");
        cat3.addChild(cat896);
        BookCategory cat59 = new BookCategory(59, "Erotica");
        cat3.addChild(cat59);
        BookCategory cat887 = new BookCategory(887, "Fairy tales");
        cat3.addChild(cat887);
        BookCategory cat1206 = new BookCategory(1206, "Fantasy");
        cat3.addChild(cat1206);
        BookCategory cat886 = new BookCategory(886, "Gay & lesbian fiction");
        cat3.addChild(cat886);
        BookCategory cat58 = new BookCategory(58, "Graphic novels & comics");
        cat3.addChild(cat58);
        BookCategory cat884 = new BookCategory(884, "Historical");
        cat3.addChild(cat884);
        BookCategory cat1067 = new BookCategory(1067, "Holiday");
        cat3.addChild(cat1067);
        BookCategory cat883 = new BookCategory(883, "Horror");
        cat3.addChild(cat883);
        BookCategory cat882 = new BookCategory(882, "Humor & comedy");
        cat3.addChild(cat882);
        BookCategory cat1527 = new BookCategory(1527, "Inspirational");
        cat3.addChild(cat1527);
        BookCategory cat4815 = new BookCategory(4815, "Literary collections");
        cat3.addChild(cat4815);
        BookCategory cat57 = new BookCategory(57, "Literature");
        cat3.addChild(cat57);
        BookCategory cat1350 = new BookCategory(1350, "Mashups");
        cat3.addChild(cat1350);
        BookCategory cat879 = new BookCategory(879, "Mystery & detective");
        cat3.addChild(cat879);
        BookCategory cat60 = new BookCategory(60, "Plays & Screenplays");
        cat3.addChild(cat60);
        BookCategory cat56 = new BookCategory(56, "Poetry");
        cat3.addChild(cat56);
        BookCategory cat877 = new BookCategory(877, "Religious");
        cat3.addChild(cat877);
        BookCategory cat1235 = new BookCategory(1235, "Romance");
        cat3.addChild(cat1235);
        BookCategory cat1213 = new BookCategory(1213, "Science fiction");
        cat3.addChild(cat1213);
        BookCategory cat1126 = new BookCategory(1126, "Sports");
        cat3.addChild(cat1126);
        BookCategory cat1337 = new BookCategory(1337, "Themes & motifs");
        cat3.addChild(cat1337);
        BookCategory cat874 = new BookCategory(874, "Thriller & suspense");
        cat3.addChild(cat874);
        BookCategory cat1001 = new BookCategory(1001, "Transgressional fiction");
        cat3.addChild(cat1001);
        BookCategory cat873 = new BookCategory(873, "Urban");
        cat3.addChild(cat873);
        BookCategory cat1091 = new BookCategory(1091, "Visionary & metaphysical");
        cat3.addChild(cat1091);
        BookCategory cat871 = new BookCategory(871, "Western");
        cat3.addChild(cat871);
        BookCategory cat870 = new BookCategory(870, "Women's fiction");
        cat3.addChild(cat870);
        BookCategory cat1018 = new BookCategory(1018, "Young adult or teen");
        cat3.addChild(cat1018);
        BookCategory cat4 = new BookCategory(4, "Non-Fiction");
        cat1.addChild(cat4);
        BookCategory cat96 = new BookCategory(96, "Antiques & Collectibles");
        cat4.addChild(cat96);
        BookCategory cat95 = new BookCategory(95, "Art, Architecture, Photography");
        cat4.addChild(cat95);
        BookCategory cat94 = new BookCategory(94, "Biography");
        cat4.addChild(cat94);
        BookCategory cat93 = new BookCategory(93, "Business & Economics");
        cat4.addChild(cat93);
        BookCategory cat92 = new BookCategory(92, "Career Guides");
        cat4.addChild(cat92);
        BookCategory cat91 = new BookCategory(91, "Children's Books");
        cat4.addChild(cat91);
        BookCategory cat4856 = new BookCategory(4856, "Comics (nonfictional)");
        cat4.addChild(cat4856);
        BookCategory cat90 = new BookCategory(90, "Computers and Internet");
        cat4.addChild(cat90);
        BookCategory cat89 = new BookCategory(89, "Cooking, Food, Wine, Spirits");
        cat4.addChild(cat89);
        BookCategory cat88 = new BookCategory(88, "Education and Study Guides");
        cat4.addChild(cat88);
        BookCategory cat2369 = new BookCategory(2369, "Engineering, trades, and technology");
        cat4.addChild(cat2369);
        BookCategory cat87 = new BookCategory(87, "Entertainment");
        cat4.addChild(cat87);
        BookCategory cat86 = new BookCategory(86, "Gay and Lesbian");
        cat4.addChild(cat86);
        BookCategory cat4816 = new BookCategory(4816, "General reference");
        cat4.addChild(cat4816);
        BookCategory cat85 = new BookCategory(85, "Health, wellbeing, & medicine");
        cat4.addChild(cat85);
        BookCategory cat84 = new BookCategory(84, "History");
        cat4.addChild(cat84);
        BookCategory cat83 = new BookCategory(83, "Home and Garden");
        cat4.addChild(cat83);
        BookCategory cat82 = new BookCategory(82, "Inspiration");
        cat4.addChild(cat82);
        BookCategory cat81 = new BookCategory(81, "Language Instruction");
        cat4.addChild(cat81);
        BookCategory cat80 = new BookCategory(80, "Law");
        cat4.addChild(cat80);
        BookCategory cat880 = new BookCategory(880, "Literary criticism");
        cat4.addChild(cat880);
        BookCategory cat79 = new BookCategory(79, "Music");
        cat4.addChild(cat79);
        BookCategory cat78 = new BookCategory(78, "New Age");
        cat4.addChild(cat78);
        BookCategory cat77 = new BookCategory(77, "Parenting");
        cat4.addChild(cat77);
        BookCategory cat76 = new BookCategory(76, "Philosophy");
        cat4.addChild(cat76);
        BookCategory cat75 = new BookCategory(75, "Politics and Current Affairs");
        cat4.addChild(cat75);
        BookCategory cat74 = new BookCategory(74, "Psychology");
        cat4.addChild(cat74);
        BookCategory cat1044 = new BookCategory(1044, "Publishing");
        cat4.addChild(cat1044);
        BookCategory cat73 = new BookCategory(73, "Reference");
        cat4.addChild(cat73);
        BookCategory cat72 = new BookCategory(72, "Relationships and Family");
        cat4.addChild(cat72);
        BookCategory cat71 = new BookCategory(71, "Religion and Spirituality");
        cat4.addChild(cat71);
        BookCategory cat70 = new BookCategory(70, "Science and Nature");
        cat4.addChild(cat70);
        BookCategory cat69 = new BookCategory(69, "Self-improvement");
        cat4.addChild(cat69);
        BookCategory cat68 = new BookCategory(68, "Sex and Relationships");
        cat4.addChild(cat68);
        BookCategory cat67 = new BookCategory(67, "Social Science");
        cat4.addChild(cat67);
        BookCategory cat66 = new BookCategory(66, "Sports & outdoor recreation");
        cat4.addChild(cat66);
        BookCategory cat65 = new BookCategory(65, "Transportation");
        cat4.addChild(cat65);
        BookCategory cat64 = new BookCategory(64, "Travel");
        cat4.addChild(cat64);
        BookCategory cat1049 = new BookCategory(1049, "True Crime");
        cat4.addChild(cat1049);
        BookCategory cat63 = new BookCategory(63, "Weddings");
        cat4.addChild(cat63);
        BookCategory cat898 = new BookCategory(898, "Essay");
        cat1.addChild(cat898);
        BookCategory cat1065 = new BookCategory(1065, "Literature");
        cat898.addChild(cat1065);
        BookCategory cat902 = new BookCategory(902, "Sociology");
        cat898.addChild(cat902);
        BookCategory cat900 = new BookCategory(900, "Business");
        cat898.addChild(cat900);
        BookCategory cat899 = new BookCategory(899, "Political");
        cat898.addChild(cat899);
        BookCategory cat1066 = new BookCategory(1066, "Author profile");
        cat898.addChild(cat1066);
        BookCategory cat901 = new BookCategory(901, "Technology");
        cat898.addChild(cat901);
        BookCategory cat903 = new BookCategory(903, "Legal");
        cat898.addChild(cat903);
        BookCategory cat2044 = new BookCategory(2044, "Plays");
        cat1.addChild(cat2044);
        BookCategory cat2064 = new BookCategory(2064, "American / African American");
        cat2044.addChild(cat2064);
        BookCategory cat2071 = new BookCategory(2071, "Ancient & Classical");
        cat2044.addChild(cat2071);
        BookCategory cat2070 = new BookCategory(2070, "Asian / Japanese");
        cat2044.addChild(cat2070);
        BookCategory cat2074 = new BookCategory(2074, "Australian & Oceanian");
        cat2044.addChild(cat2074);
        BookCategory cat2075 = new BookCategory(2075, "Canadian");
        cat2044.addChild(cat2075);
        BookCategory cat2076 = new BookCategory(2076, "Caribbean & Latin American");
        cat2044.addChild(cat2076);
        BookCategory cat2065 = new BookCategory(2065, "European / English, Irish, Scottish, Welsh");
        cat2044.addChild(cat2065);
        BookCategory cat2066 = new BookCategory(2066, "European / French");
        cat2044.addChild(cat2066);
        BookCategory cat2067 = new BookCategory(2067, "European / German");
        cat2044.addChild(cat2067);
        BookCategory cat2068 = new BookCategory(2068, "European / Italian");
        cat2044.addChild(cat2068);
        BookCategory cat2069 = new BookCategory(2069, "European / Spanish & Portuguese");
        cat2044.addChild(cat2069);
        BookCategory cat2078 = new BookCategory(2078, "Gay & Lesbian");
        cat2044.addChild(cat2078);
        BookCategory cat2079 = new BookCategory(2079, "Medieval");
        cat2044.addChild(cat2079);
        BookCategory cat2072 = new BookCategory(2072, "Religious & Liturgical");
        cat2044.addChild(cat2072);
        BookCategory cat2077 = new BookCategory(2077, "Russian & Former Soviet Union");
        cat2044.addChild(cat2077);
        BookCategory cat2073 = new BookCategory(2073, "Shakespeare");
        cat2044.addChild(cat2073);
        BookCategory cat2080 = new BookCategory(2080, "Women Authors");
        cat2044.addChild(cat2080);
        BookCategory cat2 = new BookCategory(2, "Screenplays");
        cat1.addChild(cat2);
        BookCategory cat55 = new BookCategory(55, "Action Adventure");
        cat2.addChild(cat55);
        BookCategory cat54 = new BookCategory(54, "Animation");
        cat2.addChild(cat54);
        BookCategory cat53 = new BookCategory(53, "Biographical");
        cat2.addChild(cat53);
        BookCategory cat52 = new BookCategory(52, "Comedy");
        cat2.addChild(cat52);
        BookCategory cat51 = new BookCategory(51, "Crime");
        cat2.addChild(cat51);
        BookCategory cat50 = new BookCategory(50, "Documentary");
        cat2.addChild(cat50);
        BookCategory cat49 = new BookCategory(49, "Drama");
        cat2.addChild(cat49);
        BookCategory cat48 = new BookCategory(48, "Film-Noir");
        cat2.addChild(cat48);
        BookCategory cat47 = new BookCategory(47, "History");
        cat2.addChild(cat47);
        BookCategory cat46 = new BookCategory(46, "Horror");
        cat2.addChild(cat46);
        BookCategory cat45 = new BookCategory(45, "Musical");
        cat2.addChild(cat45);
        BookCategory cat44 = new BookCategory(44, "Mystery");
        cat2.addChild(cat44);
        BookCategory cat893 = new BookCategory(893, "Period piece");
        cat2.addChild(cat893);
        BookCategory cat43 = new BookCategory(43, "Romance");
        cat2.addChild(cat43);
        BookCategory cat42 = new BookCategory(42, "Sci-Fi");
        cat2.addChild(cat42);
        BookCategory cat41 = new BookCategory(41, "Short");
        cat2.addChild(cat41);
        BookCategory cat40 = new BookCategory(40, "Thriller");
        cat2.addChild(cat40);
        BookCategory cat39 = new BookCategory(39, "War");
        cat2.addChild(cat39);
        BookCategory cat38 = new BookCategory(38, "Western");
        cat2.addChild(cat38);

        cat1.addChild(cat56);
        BookCategory cat831 = new BookCategory(831, "African Poetry");
        cat56.addChild(cat831);
        BookCategory cat830 = new BookCategory(830, "American poetry");
        cat56.addChild(cat830);
        BookCategory cat829 = new BookCategory(829, "Ancient Poetry");
        cat56.addChild(cat829);
        BookCategory cat826 = new BookCategory(826, "Asian poetry");
        cat56.addChild(cat826);
        BookCategory cat3638 = new BookCategory(3638, "Australian & Oceanian");
        cat56.addChild(cat3638);
        BookCategory cat825 = new BookCategory(825, "Biography");
        cat56.addChild(cat825);
        BookCategory cat824 = new BookCategory(824, "Canadian Poetry");
        cat56.addChild(cat824);
        BookCategory cat823 = new BookCategory(823, "Caribbean Poetry");
        cat56.addChild(cat823);
        BookCategory cat822 = new BookCategory(822, "Chinese poetry");
        cat56.addChild(cat822);
        BookCategory cat821 = new BookCategory(821, "Contemporary Poetry");
        cat56.addChild(cat821);
        BookCategory cat820 = new BookCategory(820, "Eastern European Poetry");
        cat56.addChild(cat820);
        BookCategory cat3639 = new BookCategory(3639, "Epic");
        cat56.addChild(cat3639);
        BookCategory cat3296 = new BookCategory(3296, "European poetry");
        cat56.addChild(cat3296);
        BookCategory cat3654 = new BookCategory(3654, "Female authors");
        cat56.addChild(cat3654);
        BookCategory cat819 = new BookCategory(819, "French Poetry");
        cat56.addChild(cat819);
        BookCategory cat3647 = new BookCategory(3647, "Gay & lesbian");
        cat56.addChild(cat3647);
        BookCategory cat818 = new BookCategory(818, "German Poetry");
        cat56.addChild(cat818);
        BookCategory cat817 = new BookCategory(817, "Japanese Poetry");
        cat56.addChild(cat817);
        BookCategory cat816 = new BookCategory(816, "Latin American Poetry");
        cat56.addChild(cat816);
        BookCategory cat3648 = new BookCategory(3648, "Medieval");
        cat56.addChild(cat3648);
        BookCategory cat815 = new BookCategory(815, "Middle Eastern poetry");
        cat56.addChild(cat815);
        BookCategory cat814 = new BookCategory(814, "Portuguese poetry");
        cat56.addChild(cat814);
        BookCategory cat3641 = new BookCategory(3641, "Russian & Former Soviet Union");
        cat56.addChild(cat3641);
        BookCategory cat813 = new BookCategory(813, "Scandinavian Poetry");
        cat56.addChild(cat813);
        BookCategory cat812 = new BookCategory(812, "South Asian and Indian poetry");
        cat56.addChild(cat812);
        BookCategory cat811 = new BookCategory(811, "Spanish Poetry");
        cat56.addChild(cat811);
        BookCategory cat1072 = new BookCategory(1072, "Spiritual");
        cat56.addChild(cat1072);
        BookCategory cat3295 = new BookCategory(3295, "Themes & motifs");
        cat56.addChild(cat3295);
        BookCategory cat810 = new BookCategory(810, "U.K. Poetry");
        cat56.addChild(cat810);


        // Generated end
    }

    public static BookCategory getRootCategory() {
        return cat1;
    }

}
