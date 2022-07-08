package com.example.bookapi.repositoriy;

import com.example.bookapi.entity.Book;
import com.example.bookapi.entity.Group;
import com.example.bookapi.entity.enums.LanguageEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepositary extends JpaRepository<Book,Integer> {

    boolean existsBookByBookNameStartsWith(String bookName);
    List<Book>searchBooksByGroupNumberAndLanguage(Group groupNumber, LanguageEnum language);
    Book findByBookNameAndLanguageAndGroupNumber(String bookName, LanguageEnum language, Group groupNumber);

}
