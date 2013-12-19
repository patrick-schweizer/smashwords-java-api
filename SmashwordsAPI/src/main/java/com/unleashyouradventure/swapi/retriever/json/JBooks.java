package com.unleashyouradventure.swapi.retriever.json;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.unleashyouradventure.swapi.retriever.Book;

@Data
public class JBooks {
    private List<Book> addedBooks = new ArrayList<Book>();
    private List<Book> purchasedBooks = new ArrayList<Book>();
    private List<Book> giftedBooks = new ArrayList<Book>();
}
