package com.unleashyouradventure.swapi.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.unleashyouradventure.swapi.retriever.Book;

@Data
public class SwBooks {
    private List<Book> addedBooks = new ArrayList<Book>();
    private List<Book> purchasedBooks = new ArrayList<Book>();
    private List<Book> giftedBooks = new ArrayList<Book>();
}
