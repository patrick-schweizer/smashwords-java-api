package com.unleashyouradventure.swapi;

import com.unleashyouradventure.swapi.retriever.Book;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BookListValidator {

    private final List<Book> books;

    public void validateBookFromListPage(){
        for(Book book : books){
            new BookValidator(book).validateBookFromListPage();
        }
    }
}
