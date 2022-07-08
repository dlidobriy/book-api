package com.example.bookapi.entity;

import com.example.bookapi.entity.attachmant.AttachmantFile;
import com.example.bookapi.entity.attachmant.AttachmantFoto;
import com.example.bookapi.entity.enums.LanguageEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String bookName;

    @ManyToOne
    private Group groupNumber;

    @Enumerated(EnumType.STRING)
    private LanguageEnum language;
    @ManyToOne(cascade = CascadeType.ALL)
    private AttachmantFile bookFile;

    @ManyToOne(cascade = CascadeType.ALL)
    private AttachmantFoto bookFoto;

}
